package hu.bme.aut.android.dreamlibrary.listFragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import hu.bme.aut.android.dreamlibrary.R

class TopListFragment : AbstractListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarTitle.setText(R.string.top_5_favourite_books)
    }

    override fun getBooksLiveData() = bookItemDao.getTop5Read()

    override fun actionType(id: Long): NavDirections {
        return TopListFragmentDirections.actionTopListFragmentToBookItemDetailsFragment(id)
    }
}