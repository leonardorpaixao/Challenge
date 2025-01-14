package paixao.leonardo.marvel.heroes.feature.character.screens.listing.entries

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.xwray.groupie.viewbinding.BindableItem
import paixao.leonardo.marvel.heroes.domain.models.MarvelCharacter
import paixao.leonardo.marvel.heroes.feature.R
import paixao.leonardo.marvel.heroes.feature.character.screens.listing.customviews.OnRetry
import paixao.leonardo.marvel.heroes.feature.core.exceptions.MarvelException
import paixao.leonardo.marvel.heroes.feature.databinding.ItemFavoriteCharacterListBinding

class FavoriteCharactersEntry(
    private val handleError: (MarvelException, OnRetry) -> Unit = { _, _ -> },
    private val handleLoading: (Boolean) -> Unit,
    private val navigateToDetails: (MarvelCharacter, AppCompatImageView) -> Unit
) : BindableItem<ItemFavoriteCharacterListBinding>() {
    override fun bind(viewBinding: ItemFavoriteCharacterListBinding, position: Int) {
        viewBinding.root.handleError = handleError
        viewBinding.root.handleLoading = handleLoading
        viewBinding.root.navigateToDetails = navigateToDetails
    }

    override fun getLayout(): Int = R.layout.item_favorite_character_list

    override fun initializeViewBinding(view: View): ItemFavoriteCharacterListBinding =
        ItemFavoriteCharacterListBinding.bind(view)

}