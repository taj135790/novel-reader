package com.novel.reader

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.LeadingMarginSpan
import android.text.style.LineHeightSpan
import android.text.style.ScaleXSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.novel.reader.databinding.FragmentReaderBinding

class ReaderFragment : Fragment() {
    
    private var _binding: FragmentReaderBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: NovelViewModel
    private var chapter: Chapter? = null
    
    companion object {
        private const val ARG_CHAPTER = "chapter"
        
        fun newInstance(chapter: Chapter): ReaderFragment {
            val fragment = ReaderFragment()
            val args = Bundle()
            args.putParcelable(ARG_CHAPTER, chapter)
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NovelViewModel::class.java]
        chapter = arguments?.getParcelable(ARG_CHAPTER)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateContent()
    }
    
    private fun updateContent() {
        chapter?.let { chapter ->
            binding.tvChapterTitle.text = chapter.title
            
            val settings = viewModel.getReadingSettings()
            val styledContent = createStyledText(chapter.content, settings)
            binding.tvContent.text = styledContent
            
            // 显示页码信息
            val chapterIndex = viewModel.novelData.value?.chapters?.indexOf(chapter) ?: 0
            val totalChapters = viewModel.novelData.value?.chapters?.size ?: 1
            binding.tvPageInfo.text = "${chapterIndex + 1}/$totalChapters"
        }
    }
    
    private fun createStyledText(content: String, settings: ReadingSettings): SpannableString {
        val spannableString = SpannableString(content)
        
        // 设置字体大小
        spannableString.setSpan(
            AbsoluteSizeSpan(settings.fontSize.toInt(), true),
            0,
            content.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        // 设置字间距
        spannableString.setSpan(
            ScaleXSpan(settings.letterSpacing),
            0,
            content.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        // 设置行间距（通过自定义Span实现）
        spannableString.setSpan(
            object : LineHeightSpan {
                override fun chooseHeight(
                    text: CharSequence?,
                    start: Int,
                    end: Int,
                    spanstartv: Int,
                    v: Int,
                    fm: android.graphics.Paint.FontMetricsInt?
                ) {
                    fm?.let { fontMetrics ->
                        val originalHeight = fontMetrics.descent - fontMetrics.ascent
                        val newHeight = (originalHeight * settings.lineSpacing).toInt()
                        val extraSpace = newHeight - originalHeight
                        fontMetrics.descent += extraSpace
                    }
                }
            },
            0,
            content.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        // 设置首行缩进
        spannableString.setSpan(
            object : LeadingMarginSpan.Standard(settings.fontSize.toInt() * 2) {},
            0,
            content.indexOf('\n').takeIf { it > 0 } ?: content.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        return spannableString
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}