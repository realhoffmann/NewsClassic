package at.technikum_wien.polzert.newsclassic

import at.technikum_wien.polzert.newsclassic.data.AppRoomDatabase
import android.app.Application

class NewsItemApplication : Application(){
    val appRoomDatabase: AppRoomDatabase by lazy { AppRoomDatabase.getDatabase(applicationContext) }
}