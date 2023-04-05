package at.technikum_wien.polzert.newsclassic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import at.technikum_wien.polzert.newsclassic.data.NewsItem
import at.technikum_wien.polzert.newsclassic.R
import at.technikum_wien.polzert.newsclassic.data.download.ImageDownloader
import com.bumptech.glide.Glide

class ListAdapter(items: List<NewsItem> = listOf()) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {
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
                if(index == 0) {
                    itemView.setBackgroundColor(itemView.context.getColor(R.color.purple_200))
                }
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



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val context = parent.context
        val layoutIdForListItem: Int = R.layout.list_item
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