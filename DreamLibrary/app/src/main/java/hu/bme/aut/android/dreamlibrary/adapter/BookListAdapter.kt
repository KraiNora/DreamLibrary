package hu.bme.aut.android.dreamlibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import hu.bme.aut.android.dreamlibrary.R
import hu.bme.aut.android.dreamlibrary.data.BookItem
import hu.bme.aut.android.dreamlibrary.databinding.ItemBookListBinding

class BookListAdapter : ListAdapter<BookItem, BookListAdapter.BookViewHolder>(DiffCallback) {

    lateinit var onItemDeleteClickedListener: (BookItem) -> Unit
    lateinit var onItemClickedListener: (BookItem) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BookViewHolder(
        ItemBookListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val bookItem = getItem(position)

        holder.binding.root.setOnClickListener {
            onItemClickedListener(bookItem)
        }

        holder.binding.tvTitle.text = bookItem.title
        holder.binding.tvAuthor.text = bookItem.author

        val categoryNames = holder.binding.root.resources.getStringArray(R.array.genre_items)
        holder.binding.tvGenre.text = categoryNames[bookItem.genre.ordinal]
        holder.binding.ivIcon.load(bookItem.pictureUrl)

        holder.binding.ibRemove.setOnClickListener {
            onItemDeleteClickedListener(bookItem)
        }
    }

    class BookViewHolder(val binding: ItemBookListBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<BookItem>() {
        override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
            return oldItem == newItem
        }
    }
}
