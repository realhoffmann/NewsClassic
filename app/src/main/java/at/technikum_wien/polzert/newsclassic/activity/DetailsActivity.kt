package at.technikum_wien.polzert.newsclassic.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import at.technikum_wien.polzert.newsclassic.R
import at.technikum_wien.polzert.newsclassic.data.NewsItem
import at.technikum_wien.polzert.newsclassic.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val ITEM_KEY = "item"
    }

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val item = intent?.extras?.getSerializable(ITEM_KEY) as? NewsItem
        if (item != null) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
            binding.tvAuthor.text = item.author
            binding.tvImageUrl.text = item.imageUrl
            binding.tvPublicationDate.text = item.publicationDate.toString()
            binding.tvKeywords.text = item.keywords.joinToString("\n")
        }
    }
}
