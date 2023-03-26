package at.technikum_wien.polzert.newsclassic.viewmodels

import androidx.lifecycle.*
import at.technikum_wien.polzert.newsclassic.data.download.NewsDownloader
import at.technikum_wien.polzert.newsclassic.data.NewsItem
import kotlinx.coroutines.launch

class NewsListViewModel() : ViewModel() {
    private val _newsItems = MutableLiveData(listOf<NewsItem>())
    private val _error = MutableLiveData(false)
    private val _busy = MutableLiveData(true)

    private var count = 0

    init {
        reload()
    }

    val newsItems : LiveData<List<NewsItem>>
        get() = _newsItems
    val error : LiveData<Boolean>
        get() = _error
    val busy : LiveData<Boolean>
        get() = _busy

    private fun downloadNewsItems(newsFeedUrl: String) {
        _error.value = false
        _newsItems.value = listOf()
        _busy.value = true
        viewModelScope.launch {
            val value = NewsDownloader().load(newsFeedUrl)
            if (value == null)
                _error.value = true
            else
                _newsItems.value = value
            _busy.value = false
        }
    }

    fun reload() {
        if (count % 2 == 0) {
            downloadNewsItems("https://www.engadget.com/rss.xml")
        } else {
            downloadNewsItems("https://www.derstandard.at/rss")
        }

        count++
    }
}
