package com.example.androidwithkotlin.extension

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter

@Suppress("UNCHECKED_CAST")
class DefaultViewHolder<T>(private val view: View) :
    RecyclerView.ViewHolder(view) {
    fun bind(type: T, bind: (binding: View, type: T) -> Unit) {
        bind(view, type)
    }
}

fun <T> createDefaultComparator() = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(
        oldItem: T,
        newItem: T
    ): Boolean = oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: T,
        newItem: T
    ): Boolean = oldItem == newItem
}

fun <T> createDefaultViewHolder(
    parent: ViewGroup,
    rId: Int
): DefaultViewHolder<T> {
    val layoutInflater = LayoutInflater.from(parent.context)

    val view = layoutInflater.inflate(rId, parent, false)

    return DefaultViewHolder(view)
}

class DefaultListAdapter<T>(private val rId: Int, private val bind: (View, T) -> Unit) :
    ListAdapter<T, DefaultViewHolder<T>>(createDefaultComparator<T>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder<T> =
        createDefaultViewHolder(
            parent,
            rId
        )

    override fun onBindViewHolder(holder: DefaultViewHolder<T>, position: Int) {
        holder.bind(getItem(position), bind)
    }
}

fun <T> createRecycleViewListAdapter(
    rId: Int,
    bind: (View, T) -> Unit
) = DefaultListAdapter(
    rId,
    bind
)