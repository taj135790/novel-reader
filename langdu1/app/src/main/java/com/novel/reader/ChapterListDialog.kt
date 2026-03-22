package com.novel.reader

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novel.reader.databinding.DialogChapterListBinding

class ChapterListDialog(
    private val chapters: List<Chapter>,
    private val onChapterSelected: (Int) -> Unit
) : DialogFragment() {
    
    private var _binding: DialogChapterListBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChapterListBinding.inflate(LayoutInflater.from(context))
        
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_chapters)
            .setView(binding.root)
            .setNegativeButton("关闭", null)
        
        setupRecyclerView()
        setupSearch()
        
        return builder.create()
    }
    
    private fun setupRecyclerView() {
        val adapter = ChapterListAdapter(chapters) { position ->
            onChapterSelected(position)
            dismiss()
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }
    
    private fun setupSearch() {
        binding.etSearch.hint = getString(R.string.hint_search_chapter)
        
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterChapters(s.toString())
            }
        })
    }
    
    private fun filterChapters(query: String) {
        val filteredChapters = if (query.isBlank()) {
            chapters
        } else {
            chapters.filter { chapter ->
                chapter.title.contains(query, ignoreCase = true)
            }
        }
        
        (binding.recyclerView.adapter as? ChapterListAdapter)?.updateList(filteredChapters)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ChapterListAdapter(
    private var chapters: List<Chapter>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ChapterListAdapter.ViewHolder>() {
    
    class ViewHolder(val binding: com.novel.reader.databinding.ItemChapterBinding) : 
        RecyclerView.ViewHolder(binding.root)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = com.novel.reader.databinding.ItemChapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.binding.tvChapterTitle.text = chapter.title
        holder.binding.tvChapterPreview.text = 
            if (chapter.content.length > 50) chapter.content.substring(0, 50) + "..." 
            else chapter.content
        
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }
    
    override fun getItemCount(): Int = chapters.size
    
    fun updateList(newList: List<Chapter>) {
        chapters = newList
        notifyDataSetChanged()
    }
}