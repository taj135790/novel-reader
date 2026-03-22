package com.novel.reader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.novel.reader.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NovelViewModel
    private lateinit var viewPagerAdapter: ReaderPagerAdapter
    private var textToSpeech: TextToSpeech? = null
    private var isTtsInitialized = false
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            importNovel()
        } else {
            Toast.makeText(this, "需要存储权限才能导入小说", Toast.LENGTH_SHORT).show()
        }
    }
    
    private val importFileLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.importNovelFromUri(this, it)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[NovelViewModel::class.java]
        
        setupViews()
        setupObservers()
        setupTextToSpeech()
        
        // 处理从外部打开TXT文件
        handleIntent(intent)
    }
    
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }
    
    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.data?.let { uri ->
                    viewModel.importNovelFromUri(this, uri)
                }
            }
        }
    }
    
    private fun setupViews() {
        setSupportActionBar(binding.toolbar)
        
        viewPagerAdapter = ReaderPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        
        // 设置左右翻页动画
        binding.viewPager.setPageTransformer { page, position ->
            page.translationX = -position * page.width
        }
        
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentPage(position)
            }
        })
        
        binding.fabTts.setOnClickListener {
            toggleTts()
        }
        
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_import -> {
                    checkPermissionAndImport()
                    true
                }
                R.id.action_directory -> {
                    showChapterList()
                    true
                }
                R.id.action_settings -> {
                    showSettings()
                    true
                }
                R.id.action_tts -> {
                    toggleTts()
                    true
                }
                else -> false
            }
        }
    }
    
    private fun setupObservers() {
        viewModel.novelData.observe(this) { novel ->
            if (novel != null) {
                viewPagerAdapter.submitList(novel.chapters)
                binding.viewPager.setCurrentItem(viewModel.currentPage, false)
            }
        }
        
        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage()
            }
        }
    }
    
    private fun setupTextToSpeech() {
        textToSpeech = TextToSpeech(this, this)
    }
    
    private fun checkPermissionAndImport() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                importNovel()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
    
    private fun importNovel() {
        importFileLauncher.launch("text/plain")
    }
    
    private fun showChapterList() {
        // 实现章节列表对话框
        val chapters = viewModel.novelData.value?.chapters ?: return
        ChapterListDialog(chapters) { chapterIndex ->
            viewModel.setCurrentPage(chapterIndex)
            binding.viewPager.setCurrentItem(chapterIndex, true)
        }.show(supportFragmentManager, "chapter_list")
    }
    
    private fun showSettings() {
        SettingsDialog {
            fontSize, lineSpacing, letterSpacing ->
            viewModel.updateReadingSettings(fontSize, lineSpacing, letterSpacing)
            viewPagerAdapter.notifyDataSetChanged()
        }.show(supportFragmentManager, "settings")
    }
    
    private fun toggleTts() {
        if (!isTtsInitialized) {
            Toast.makeText(this, R.string.msg_tts_not_available, Toast.LENGTH_SHORT).show()
            return
        }
        
        val currentChapter = viewModel.getCurrentChapter()
        currentChapter?.let { chapter ->
            if (viewModel.isTtsPlaying) {
                stopTts()
            } else {
                startTts(chapter.content)
            }
        }
    }
    
    private fun startTts(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "novel_reader")
        viewModel.setTtsPlaying(true)
        binding.fabTts.setImageResource(R.drawable.ic_pause)
    }
    
    private fun stopTts() {
        textToSpeech?.stop()
        viewModel.setTtsPlaying(false)
        binding.fabTts.setImageResource(R.drawable.ic_tts)
    }
    
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale.CHINESE)
            isTtsInitialized = result != TextToSpeech.LANG_MISSING_DATA && 
                              result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }
    
    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}