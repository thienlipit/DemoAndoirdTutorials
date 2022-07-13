package com.example.demolistadapterkotlin.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demolistadapterkotlin.databinding.ItemBinding
import com.example.demolistadapterkotlin.models.User

class UserAdapter: ListAdapter<User, UserAdapter.ViewHolder>(SearchResultDiffCallback()) {
    class SearchResultDiffCallback: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.age == newItem.age
        }

    }
    inner class ViewHolder(val binding: ItemBinding):
            RecyclerView.ViewHolder(binding.root){

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate((parent.context as Activity).layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = getItem(position)
    }

    override fun submitList(list: MutableList<User>?) {
        val i = if(itemCount == 0) 0 else itemCount
        super.submitList(list?: listOf())
        notifyItemRangeInserted(i, list?.size?:0)
    }
    companion object{
        @JvmStatic
        @BindingAdapter("UserAdapter")
        fun getSearchResultList(recyclerView: RecyclerView, list: MutableList<User>?){
            val adapter = if(recyclerView.adapter != null && recyclerView.adapter is UserAdapter){
                recyclerView.adapter as UserAdapter
            } else {
                UserAdapter()
            }
            adapter.submitList(list)
        }
    }

}