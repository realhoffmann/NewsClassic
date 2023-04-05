package at.technikum_wien.polzert.newsclassic.viewmodels

import android.app.Application
import androidx.lifecycle.*
import at.technikum_wien.polzert.newsclassic.activity.MainActivity
import at.technikum_wien.polzert.newsclassic.data.download.NewsDownloader
import at.technikum_wien.polzert.newsclassic.data.NewsItem
import at.technikum_wien.polzert.newsclassic.data.download.NewsItemRepository
import kotlinx.coroutines.launch

class NewsListViewModel(val newsItemRepository: NewsItemRepository) : ViewModel() {

    private val _error = MutableLiveData(false)
    private val _busy = MutableLiveData(true)
    val newsItems = newsItemRepository.newsItems
    private var count = 0
    var url = ""

    init {
        reload(url)
    }

    val error : LiveData<Boolean>
        get() = _error
    val busy : LiveData<Boolean>
        get() = _busy

    private fun downloadNewsItems(newsFeedUrl: String) {
        _error.value = false
        _busy.value = true
        viewModelScope.launch {
            val value = newsItemRepository.loadNewsItems(newsFeedUrl, true)
            if (value) {
                _error.value = true
            } else {
                _busy.value = false
            }
        }
    }

    fun reload(url: String) {
        if(url != ""){
            downloadNewsItems(url)
        }else {

            if (count % 2 == 0) {
                downloadNewsItems("https://www.engadget.com/rss.xml")
            } else {
                downloadNewsItems("https://www.derstandard.at/rss")
            }
            count++
        }
    }
}