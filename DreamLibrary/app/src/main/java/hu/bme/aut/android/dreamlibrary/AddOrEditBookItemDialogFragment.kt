package hu.bme.aut.android.dreamlibrary

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.dreamlibrary.data.BookItem
import hu.bme.aut.android.dreamlibrary.data.BookItemDao
import hu.bme.aut.android.dreamlibrary.data.BookListDatabase
import hu.bme.aut.android.dreamlibrary.databinding.DialogNewBookBinding
import kotlin.concurrent.thread

class AddOrEditBookItemDialogFragment : DialogFragment() {

    private lateinit var binding: DialogNewBookBinding
    private lateinit var bookItemDao: BookItemDao

    private val args: AddOrEditBookItemDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewBookBinding.inflate(layoutInflater)
        bookItemDao = BookListDatabase.getDatabase(requireContext()).bookItemDao()

        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.genre_items)
        )

        binding.swReadItAlready.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.idRBRating.setIsIndicator(false)
                binding.etReview.isEnabled = true
            } else {
                binding.idRBRating.setIsIndicator(true)
                binding.etReview.isEnabled = false
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.button_save_book) { _, _ ->
                if (isValid()) {
                    val bookItem = if (isEditingMode()) {
                        editBookItem(args.bookId)
                    } else {
                        createBookItem()
                    }
                    setFragmentResult(args.requestKey, bookItem.toBatyu())
                } else {
                    Toast.makeText(requireContext(), "Add title and author to the book", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isEditingMode()) {
            val bookId = args.bookId
            val bookItemLiveData = bookItemDao.getById(bookId)

            bookItemLiveData.observe(viewLifecycleOwner) { bookItem ->
                binding.etAuthor.setText(bookItem.author)
                binding.etTitle.setText(bookItem.title)
                binding.etDesc.setText(bookItem.description)
                binding.etReview.setText(bookItem.review)
                binding.etCover.setText(bookItem.pictureUrl)
                binding.spCategory.setSelection(bookItem.genre.ordinal)
                binding.idRBRating.rating = bookItem.rating?.toFloat() ?: 0f
                binding.swReadItAlready.isChecked = bookItem.isRead
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    private fun isValid()= (binding.etAuthor.text.isNotEmpty() && binding.etTitle.text.isNotEmpty())

    private fun isEditingMode() = args.bookId != -1L

    private fun createBookItem() = BookItem(
        author = binding.etAuthor.text.toString(),
        title = binding.etTitle.text.toString(),
        description = binding.etDesc.text.toString(),
        genre = BookItem.Genre.getByOrdinal(binding.spCategory.selectedItemPosition)!!,
        pictureUrl = binding.etCover.text.toString(),
        isRead = binding.swReadItAlready.isChecked
    )

    private fun editBookItem(id: Long) = BookItem(
        id = id,
        author = binding.etAuthor.text.toString(),
        title = binding.etTitle.text.toString(),
        description = binding.etDesc.text.toString(),
        genre = BookItem.Genre.getByOrdinal(binding.spCategory.selectedItemPosition)!!,
        pictureUrl = binding.etCover.text.toString(),
        rating = binding.idRBRating.rating.toInt(),
        review = binding.etReview.text.toString(),
        isRead = binding.swReadItAlready.isChecked
    )
}