package hu.bme.aut.android.dreamlibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import hu.bme.aut.android.dreamlibrary.data.BookItem
import hu.bme.aut.android.dreamlibrary.data.BookItemDao
import hu.bme.aut.android.dreamlibrary.data.BookListDatabase
import hu.bme.aut.android.dreamlibrary.databinding.BookItemDetailsBinding
import kotlin.concurrent.thread

class BookItemDetailsFragment : Fragment() {

    private lateinit var binding: BookItemDetailsBinding
    private lateinit var bookItemDao: BookItemDao

    private val args: BookItemDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookItemDao = BookListDatabase.getDatabase(requireContext()).bookItemDao()
        setFragmentResultListener(REQUEST_KEY_EDIT_BOOK) { _, batyu ->
            val newBookItem = BookItem(batyu)
            thread {
                bookItemDao.update(newBookItem)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Book updated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BookItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookId = args.bookItemId
        val bookItemLiveData = bookItemDao.getById(bookId)

        bookItemLiveData.observe(viewLifecycleOwner) { bookItem ->
            binding.idTVAuthor.text = bookItem.author
            binding.idTVTitle.text = bookItem.title
            binding.checkBox.isChecked = bookItem.isRead
            val categoryNames = resources.getStringArray(R.array.genre_items)
            binding.idTVCategory.text = categoryNames[bookItem.genre.ordinal]
            binding.idRBRating.rating = bookItem.rating?.toFloat() ?: 0f
            binding.tvDescription.text = bookItem.description
            binding.tvReview.text = bookItem.review
            binding.idIVBook.load(bookItem.pictureUrl)
        }


        binding.btnEdit.setOnClickListener {
            val action = BookItemDetailsFragmentDirections.actionBookItemDetailsFragmentToAddOrEditBookItemDialogFragment(
                requestKey = REQUEST_KEY_EDIT_BOOK,
                bookId = args.bookItemId
            )
            findNavController().navigate(action)
        }

    }


    companion object {
        private const val REQUEST_KEY_EDIT_BOOK = "REQUEST_KEY_EDIT_BOOK"
    }

}