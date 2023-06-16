package hu.bme.aut.android.dreamlibrary.listFragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import hu.bme.aut.android.dreamlibrary.R


class BookListFragment : AbstractListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarTitle.setText(R.string.bookList)
    }

    override fun actionType(id: Long): NavDirections {
        return BookListFragmentDirections.actionBookListFragmentToBookItemDetailsFragment(id)
    }

    override fun getBooksLiveData() = bookItemDao.getAll(true)
}