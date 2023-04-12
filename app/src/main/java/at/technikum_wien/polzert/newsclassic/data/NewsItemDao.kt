package at.technikum_wien.polzert.newsclassic.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsItemDao {

        //Adds Item, if the item does not exist
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun addNewsItems(newsItems: List<NewsItem>) : List<Long>

        //Orders Items by publication date
        @Query("SELECT * FROM news_item ORDER BY publicationDate DESC")
        fun orderNewsItems(): LiveData<List<NewsItem>>


        //Deletes all Items
        @Query("DELETE FROM news_item")
        suspend fun deleteAllNewsItems()
}