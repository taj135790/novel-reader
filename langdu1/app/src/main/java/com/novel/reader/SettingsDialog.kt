package com.novel.reader

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.novel.reader.databinding.DialogSettingsBinding

class SettingsDialog(
    private val onSettingsChanged: (Float, Float, Float) -> Unit
) : DialogFragment() {
    
    private var _binding: DialogSettingsBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogSettingsBinding.inflate(LayoutInflater.from(context))
        
        val viewModel = ViewModelProvider(requireActivity())[NovelViewModel::class.java]
        val settings = viewModel.getReadingSettings()
        
        // 初始化设置值
        binding.sbFontSize.progress = (settings.fontSize - 12).toInt()
        binding.sbLineSpacing.progress = ((settings.lineSpacing - 1.0) * 10).toInt()
        binding.sbLetterSpacing.progress = (settings.letterSpacing * 20).toInt()
        
        updateSettingsDisplay()
        
        setupSeekBarListeners()
        
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_settings)
            .setView(binding.root)
            .setPositiveButton("确定") { _, _ ->
                val fontSize = 12 + binding.sbFontSize.progress
                val lineSpacing = 1.0f + binding.sbLineSpacing.progress / 10.0f
                val letterSpacing = binding.sbLetterSpacing.progress / 20.0f
                onSettingsChanged(fontSize, lineSpacing, letterSpacing)
            }
            .setNegativeButton("取消", null)
        
        return builder.create()
    }
    
    private fun setupSeekBarListeners() {
        binding.sbFontSize.setOnSeekBarChangeListener(object : 
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                updateSettingsDisplay()
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
        
        binding.sbLineSpacing.setOnSeekBarChangeListener(object : 
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                updateSettingsDisplay()
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
        
        binding.sbLetterSpacing.setOnSeekBarChangeListener(object : 
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                updateSettingsDisplay()
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
    }
    
    private fun updateSettingsDisplay() {
        val fontSize = 12 + binding.sbFontSize.progress
        val lineSpacing = 1.0f + binding.sbLineSpacing.progress / 10.0f
        val letterSpacing = binding.sbLetterSpacing.progress / 20.0f
        
        binding.tvFontSizeValue.text = "${fontSize.toInt()}sp"
        binding.tvLineSpacingValue.text = "${lineSpacing}x"
        binding.tvLetterSpacingValue.text = "${letterSpacing}"
        
        // 预览文本
        val previewText = "预览文本：这是一个示例文本，用于展示当前的字体设置效果。"
        val styledText = createStyledText(previewText, fontSize, lineSpacing, letterSpacing)
        binding.tvPreview.text = styledText
    }
    
    private fun createStyledText(text: String, fontSize: Float, lineSpacing: Float, letterSpacing: Float): android.text.SpannableString {
        val spannableString = android.text.SpannableString(text)
        
        spannableString.setSpan(
            android.text.style.AbsoluteSizeSpan(fontSize.toInt(), true),
            0, text.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        spannableString.setSpan(
            android.text.style.ScaleXSpan(letterSpacing),
            0, text.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        return spannableString
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}