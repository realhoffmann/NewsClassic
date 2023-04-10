package at.technikum_wien.polzert.newsclassic.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import at.technikum_wien.polzert.newsclassic.R
import at.technikum_wien.polzert.newsclassic.data.NewsItem
import at.technikum_wien.polzert.newsclassic.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val ITEM_KEY = "item"
    }

    private lateinit var binding: ActivityDetailsBinding
    var showImages: Boolean= true

    // create view and show data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val item = intent?.extras?.getSerializable(ITEM_KEY) as? NewsItem
        if (item != null) {

            // show/hide image
            if (showImages) {
                binding.imageViewDetail.load(item.imageUrl)
            }
            binding.tvTitle.text = item.title
            binding.tvAuthor.text = item.author
            binding.tvPublicationDate.text = item.publicationDate.toString()
            binding.tvDescription.text = Html.fromHtml(item.description, Html.FROM_HTML_MODE_COMPACT)

        }

        // Full Story Button -> opens link in browser
        val fullStory = findViewById<TextView>(R.id.fullStoryButton)
        fullStory.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item?.link))
            startActivity(intent)
        }
    }
}
//load image with Glide
private fun ImageView.load(imageUrl: String?) {
    if (imageUrl != null) {
        Glide.with(this)
            .load(imageUrl)
            .into(this)
    }

}
