package hu.bme.aut.android.dreamlibrary.listFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.room.ForeignKey
import hu.bme.aut.android.dreamlibrary.adapter.BookListAdapter
import hu.bme.aut.android.dreamlibrary.data.BookItem
import hu.bme.aut.android.dreamlibrary.data.BookItemDao
import hu.bme.aut.android.dreamlibrary.data.BookListDatabase
import hu.bme.aut.android.dreamlibrary.databinding.FragmentBookListBinding
import kotlin.concurrent.thread

abstract class AbstractListFragment : Fragment() {

    protected lateinit var binding: FragmentBookListBinding
    private lateinit var adapter: BookListAdapter
    protected lateinit var bookItemDao: BookItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookItemDao = BookListDatabase.getDatabase(requireContext()).bookItemDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = BookListAdapter().apply {
            onItemDeleteClickedListener = {
                thread {
                    bookItemDao.deleteItem(it)
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Book removed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            onItemClickedListener = { bookItem ->
                val action = actionType(bookItem.id!!)
                findNavController().navigate(action)
            }
        }
        binding.rvMain.adapter = adapter
        loadBooks()
    }


    private fun loadBooks() {
        val bookLiveData = getBooksLiveData()
        bookLiveData.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books)
        }
    }

    protected abstract fun actionType(id: Long): NavDirections

    protected abstract fun getBooksLiveData(): LiveData<List<BookItem>>
}