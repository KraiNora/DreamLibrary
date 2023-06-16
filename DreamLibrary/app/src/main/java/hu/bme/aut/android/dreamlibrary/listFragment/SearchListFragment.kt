package hu.bme.aut.android.dreamlibrary.listFragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.dreamlibrary.R

class SearchListFragment : AbstractListFragment() {

    private val args: SearchListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarTitle.setText(R.string.searchlist)
    }

    override fun getBooksLiveData() = bookItemDao.searchByString(args.searchString)

    override fun actionType(id: Long): NavDirections {
        return SearchListFragmentDirections.actionSearchListFragmentToBookItemDetailsFragment(id)
    }
}