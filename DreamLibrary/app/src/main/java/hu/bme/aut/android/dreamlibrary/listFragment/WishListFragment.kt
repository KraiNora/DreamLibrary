package hu.bme.aut.android.dreamlibrary.listFragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import hu.bme.aut.android.dreamlibrary.R


class WishListFragment : AbstractListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarTitle.setText(R.string.wishlist)
    }

    override fun actionType(id: Long): NavDirections {
        return WishListFragmentDirections.actionWishListFragmentToBookItemDetailsFragment(id)
    }

    override fun getBooksLiveData()= bookItemDao.getAll(false)
}