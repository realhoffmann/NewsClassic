package at.technikum_wien.polzert.newsclassic.data.download

import at.technikum_wien.polzert.newsclassic.data.NewsItemDao

class NewsItemRepository (private val newsItemDao: NewsItemDao,
                          private val newsDownloader: NewsDownloader) {
    private val logTag = "NewsItemRepository"

    val newsItems by lazy { newsItemDao.orderNewsItems() }

    suspend fun loadNewsItems(newsFeedUrl: String, deleteOldItems: Boolean): Boolean {
        val value = newsDownloader.load(newsFeedUrl)
        return if (value == null) {
            true
        }else {

            if(deleteOldItems) {
                newsItemDao.deleteAllNewsItems()
            }
            newsItemDao.addNewsItems(value)
            false
        }
    }


}