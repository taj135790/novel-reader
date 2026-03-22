package com.novel.reader

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ReaderPagerAdapter(fragmentActivity: FragmentActivity) : 
    FragmentStateAdapter(fragmentActivity) {
    
    private var chapters: List<Chapter> = emptyList()
    
    fun submitList(newChapters: List<Chapter>) {
        chapters = newChapters
        notifyDataSetChanged()
    }
    
    override fun getItemCount(): Int = chapters.size
    
    override fun createFragment(position: Int): Fragment {
        return ReaderFragment.newInstance(chapters[position])
    }
}