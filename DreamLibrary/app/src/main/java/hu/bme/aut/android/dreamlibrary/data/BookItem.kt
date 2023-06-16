package hu.bme.aut.android.dreamlibrary.data

import android.os.Bundle
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

typealias Batyu = Bundle

@Entity(tableName = "bookItem")
data class BookItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "genre") var genre: Genre,
    @ColumnInfo(name = "pictureUrl") var pictureUrl: String,
    @ColumnInfo(name = "rating", defaultValue = "") var rating: Int? = null,
    @ColumnInfo(name = "review", defaultValue = "") var review: String? = null,
    @ColumnInfo(name = "isRead") var isRead: Boolean = false
) {

    constructor(batyu: Batyu) : this(
        id = if (batyu.containsKey("id")) batyu.getLong("id") else null,
        author = batyu.getString("author")!!,
        title = batyu.getString("title")!!,
        description = batyu.getString("description")!!,
        genre = Genre.getByOrdinal(batyu.getInt("genre"))!!,
        pictureUrl = batyu.getString("pictureUrl")!!,
        rating = batyu.getInt("rating"),
        review = batyu.getString("review"),
        isRead = batyu.getBoolean("isRead")
    )

    fun toBatyu(): Batyu {
        return Bundle().apply {
            id?.let { 
                putLong("id", it)
            }
            putString("author", author)
            putString("title", title)
            putString("description", description)
            putInt("genre", Genre.toInt(genre))
            putString("pictureUrl", pictureUrl)
            rating?.let {
                putInt("rating", it)
            }
            putString("review", review)
            putBoolean("isRead", isRead)
        }
    }
    
    enum class Genre {
        ADVENTURE, BIOGRAPHY, CLASSICS, CRIME,
        FANTASY, HISTORICAL_FICTION,
        HUMOUR, MYSTERY, POETRY, PLAYS,
        ROMANCE, SCIENCE_FICTION, SELF_HELP, THRILLER;

        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int) = values().getOrNull(ordinal)

            @JvmStatic
            @TypeConverter
            fun toInt(genre: Genre) = genre.ordinal
        }
    }
}