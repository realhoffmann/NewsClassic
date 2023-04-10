package at.technikum_wien.polzert.newsclassic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.technikum_wien.polzert.newsclassic.data.NewsItem
import at.technikum_wien.polzert.newsclassic.R
import com.bumptech.glide.Glide

class ListAdapter(items: List<NewsItem> = listOf()) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

   companion object {
       private const val TYPE_TOP = 0
       private const val TYPE_OTHERS = 1
    }
    var showImages: Boolean= true
    var items = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var itemClickListener : ((NewsItem)->Unit)? = null



    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.item_image)
        private val itemTextView = itemView.findViewById<TextView>(R.id.item_title)
        private val authorTextView = itemView.findViewById<TextView>(R.id.item_author)
        private val dateTextView = itemView.findViewById<TextView>(R.id.item_date)
        init {
            itemView.setOnClickListener { itemClickListener?.invoke(items[absoluteAdapterPosition]) }
        }

        fun bind(index: Int, showImages: Boolean) {
                itemTextView.text = items[index].title
                authorTextView.text = items[index].author
                dateTextView.text = items[index].publicationDate.toString()

                Glide
                    .with(itemView.context)
                    .load(items[index].imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView)

                if (showImages) {
                    imageView.visibility = View.VISIBLE
                } else {
                    imageView.visibility = View.GONE
                }



        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_TOP
        } else {
            TYPE_OTHERS
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val context = parent.context
        val layoutIdForListItem: Int = if (viewType == TYPE_TOP) {
            R.layout.list_item_first
        } else {
            R.layout.list_item
        }
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position, showImages)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}