package hu.bme.aut.android.dreamlibrary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.dreamlibrary.data.BookItem
import hu.bme.aut.android.dreamlibrary.data.BookItemDao
import hu.bme.aut.android.dreamlibrary.data.BookListDatabase
import hu.bme.aut.android.dreamlibrary.databinding.FragmentMenuBinding
import kotlin.concurrent.thread

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var bookItemDao: BookItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookItemDao = BookListDatabase.getDatabase(requireContext()).bookItemDao()

        setFragmentResultListener(REQUEST_KEY_ADD_BOOK) { _, batyu ->
            val newBookItem = BookItem(batyu)
            onBookItemCreated(newBookItem)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.books.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_bookListFragment)
        }
        binding.wishlist.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_wishListFragment)
        }
        binding.toplist.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_topListFragment)
        }

        binding.fab.setOnClickListener{
            val action = MenuFragmentDirections
                .actionMenuFragmentToAddOrEditBookItemDialogFragment(REQUEST_KEY_ADD_BOOK)
            findNavController().navigate(action)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?) = false

            override fun onQueryTextSubmit(p0: String?): Boolean {
                val action = MenuFragmentDirections.actionMenuFragmentToSearchListFragment(binding.searchView.query.toString())
                findNavController().navigate(action)
                return true
            }
        })
    }


    private fun onBookItemCreated(newItem: BookItem) {
        thread {
            val insertId = bookItemDao.insert(newItem)
            newItem.id = insertId
            requireActivity().runOnUiThread{
                Toast.makeText(requireContext(), "Book added to DB", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val REQUEST_KEY_ADD_BOOK = "REQUEST_KEY_ADD_BOOK"
    }
}