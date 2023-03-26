package at.technikum_wien.polzert.newsclassic.data

import java.io.Serializable
import java.util.*

data class NewsItem(var identifier : String, var title : String, var link : String?, var description : String?, var imageUrl : String?, var author : String?, var publicationDate : Date, var keywords : Set<String>) : Serializable
