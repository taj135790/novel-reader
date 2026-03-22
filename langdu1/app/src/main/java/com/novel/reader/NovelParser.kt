package com.novel.reader

import java.util.regex.Pattern

object NovelParser {
    
    private val chapterPatterns = listOf(
        Pattern.compile("^\\s*第[零一二三四五六七八九十百千万亿]+章\\s+(.*)$"),
        Pattern.compile("^\\s*第\\d+章\\s+(.*)$"),
        Pattern.compile("^\\s*\\d+\\s+(.*)$"),
        Pattern.compile("^\\s*[卷卷]\\s*[零一二三四五六七八九十百千万亿]+\\s+(.*)$"),
        Pattern.compile("^\\s*[卷卷]\\s*\\d+\\s+(.*)$")
    )
    
    fun parseText(text: String): Novel {
        val lines = text.lines()
        val chapters = mutableListOf<Chapter>()
        var currentChapterTitle = "第一章"
        val currentContent = StringBuilder()
        
        for (line in lines) {
            val trimmedLine = line.trim()
            
            if (trimmedLine.isEmpty()) {
                if (currentContent.isNotEmpty()) {
                    currentContent.append("\n")
                }
                continue
            }
            
            val chapterTitle = detectChapterTitle(trimmedLine)
            
            if (chapterTitle != null) {
                // 保存前一章
                if (currentContent.isNotEmpty()) {
                    chapters.add(Chapter(currentChapterTitle, currentContent.toString().trim()))
                    currentContent.clear()
                }
                
                currentChapterTitle = chapterTitle
            } else {
                currentContent.append(trimmedLine).append("\n")
            }
        }
        
        // 添加最后一章
        if (currentContent.isNotEmpty()) {
            chapters.add(Chapter(currentChapterTitle, currentContent.toString().trim()))
        }
        
        // 如果没有检测到章节，将整个文本作为一章
        if (chapters.isEmpty()) {
            chapters.add(Chapter("第一章", text))
        }
        
        // 提取书名（使用第一行非空文本）
        val title = lines.firstOrNull { it.trim().isNotEmpty() }?.trim() ?: "未知小说"
        
        return Novel(title, chapters)
    }
    
    private fun detectChapterTitle(line: String): String? {
        for (pattern in chapterPatterns) {
            val matcher = pattern.matcher(line)
            if (matcher.matches()) {
                val title = matcher.group(1)?.trim() ?: ""
                return if (title.isNotEmpty()) line.trim() else null
            }
        }
        
        // 检查常见的章节标识符
        val commonChapterIndicators = listOf(
            "前言", "序章", "楔子", "尾声", "后记", "番外"
        )
        
        commonChapterIndicators.forEach { indicator ->
            if (line.trim().startsWith(indicator)) {
                return line.trim()
            }
        }
        
        return null
    }
    
    fun splitContentIntoPages(content: String, charsPerPage: Int): List<String> {
        val pages = mutableListOf<String>()
        var currentPage = StringBuilder()
        var charCount = 0
        
        val paragraphs = content.split("\n\n")
        
        for (paragraph in paragraphs) {
            val trimmedParagraph = paragraph.trim()
            if (trimmedParagraph.isEmpty()) continue
            
            // 如果当前段落加上当前页面的字符数超过限制，开始新页面
            if (charCount + trimmedParagraph.length > charsPerPage && currentPage.isNotEmpty()) {
                pages.add(currentPage.toString())
                currentPage.clear()
                charCount = 0
            }
            
            currentPage.append(trimmedParagraph).append("\n\n")
            charCount += trimmedParagraph.length
        }
        
        if (currentPage.isNotEmpty()) {
            pages.add(currentPage.toString())
        }
        
        return pages
    }
}