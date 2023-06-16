package hu.bme.aut.android.dreamlibrary.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookItemDao {
    @Query("SELECT * FROM bookItem WHERE isRead = :isRead")
    fun getAll(isRead: Boolean): LiveData<List<BookItem>>

    @Query("SELECT * FROM bookItem WHERE id = :id")
    fun getById(id: Long): LiveData<BookItem>

    @Query("SELECT * FROM bookItem WHERE title LIKE '%' || :string || '%' OR author LIKE '%' || :string || '%'")
    fun searchByString(string: String): LiveData<List<BookItem>>

    @Insert
    fun insert(bookItem: BookItem): Long

    @Query("UPDATE bookItem SET review = :review, rating = :rating, isRead = 1 WHERE id = :id")
    fun updateRatingAndReview(id: Long, review: String, rating: Int)

    @Update
    fun update(bookItem: BookItem)

    @Query("SELECT * FROM bookItem WHERE isRead = 1 ORDER BY rating DESC, title ASC LIMIT 5")
    fun getTop5Read(): LiveData<List<BookItem>>

    @Delete
    fun deleteItem(bookItem: BookItem)
}