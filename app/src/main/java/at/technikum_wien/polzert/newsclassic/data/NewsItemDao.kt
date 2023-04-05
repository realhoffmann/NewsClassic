package at.technikum_wien.polzert.newsclassic.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsItemDao {

        @Query("SELECT * FROM news_item ORDER BY publicationDate DESC")
        fun fetchAllNewsItems(): LiveData<List<NewsItem>>

        @Query("SELECT * FROM news_item WHERE title = :titleFromArg")
        fun findNewsItemsByTitle(titleFromArg: String): LiveData<List<NewsItem>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun addNewsItem(newsItem: NewsItem)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun addNewsItems(newsItems: List<NewsItem>) : List<Long>

        @Query("DELETE FROM news_item")
        suspend fun deleteAllNewsItems()
}