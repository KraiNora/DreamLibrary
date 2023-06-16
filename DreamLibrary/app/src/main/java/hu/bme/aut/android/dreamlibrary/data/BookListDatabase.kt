package hu.bme.aut.android.dreamlibrary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BookItem::class], version = 1)
@TypeConverters(value = [BookItem.Genre::class])
abstract class BookListDatabase : RoomDatabase() {
    abstract fun bookItemDao(): BookItemDao

    companion object {
        fun getDatabase(applicationContext: Context): BookListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                BookListDatabase::class.java,
                "book-list"
            ).build()
        }
    }
}