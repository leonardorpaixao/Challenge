package paixao.leonardo.marvel.heroes.feature.character.screens.listing.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.consumeAsFlow
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import paixao.leonardo.marvel.heroes.domain.models.MarvelCharacter
import paixao.leonardo.marvel.heroes.feature.R
import paixao.leonardo.marvel.heroes.feature.character.screens.listing.CharacterViewModel
import paixao.leonardo.marvel.heroes.feature.character.screens.listing.entries.CharacterItemEntry
import paixao.leonardo.marvel.heroes.feature.core.exceptions.MarvelException
import paixao.leonardo.marvel.heroes.feature.core.stateMachine.StateMachineEvent
import paixao.leonardo.marvel.heroes.feature.core.utils.ktx.collectIn
import paixao.leonardo.marvel.heroes.feature.core.utils.lifecycleScope
import paixao.leonardo.marvel.heroes.feature.core.utils.viewModel
import paixao.leonardo.marvel.heroes.feature.databinding.ItemFavoriteCharacterListBinding

class FavoriteCharactersView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), DIAware {
    override val di by closestDI()

    private val viewModel by viewModel<CharacterViewModel>()

    private val binding
        get() = ItemFavoriteCharacterListBinding.bind(this)

    private var isInitializing: Boolean = true

    private val gridAdapter by lazy {
        GroupAdapter<GroupieViewHolder>().apply {
            spanCount = ADAPTER_COLUMNS
        }
    }

    var handleError: (MarvelException, OnRetry) -> Unit = { _, _ -> }
    var handleLoading: (Boolean) -> Unit = {}
    var navigateToDetails: (MarvelCharacter, AppCompatImageView) -> Unit = {_, _ ->}

    override fun onFinishInflate() {
        super.onFinishInflate()
        initializeAdapter()
        retrieveFavoriteCharacters()
        handleDataChange()
    }

    private fun retrieveFavoriteCharacters() {
        viewModel.retrieveFavoriteCharacter().collectIn(lifecycleScope) { event ->
            when (event) {
                is StateMachineEvent.Start -> handleInitializingLoading()
                is StateMachineEvent.Success -> populateCharacterRv(event.value)
                is StateMachineEvent.Failure ->
                    handleError(event.exception, ::retrieveFavoriteCharacters)
                else -> Unit
            }
        }
    }

    private fun handleDataChange() {
        viewModel.listenFavoriteCharactersChange().collectIn(lifecycleScope) {
            gridAdapter.clear()
            retrieveFavoriteCharacters()
        }
    }

    private fun handleInitializingLoading() {
        if (isInitializing) {
            isInitializing = false
        } else {
            handleLoading(true)
        }
    }

    private fun populateCharacterRv(characters: List<MarvelCharacter>) {
        handleLoading(false)
        val items = characters.map { character ->
            CharacterItemEntry(
                character = character,
                favoriteAction = { removedFavoriteCharacter, _ ->
                    removeFavoriteChar(removedFavoriteCharacter)
                },
                navigateToDetailsAction = navigateToDetails
            )
        }
        gridAdapter.addAll(items)
    }

    private fun removeFavoriteChar(
        character: MarvelCharacter
    ) {
        viewModel.saveOrRemoveFavoriteCharacter(character).collectIn(lifecycleScope) { event ->
            when (event) {
                is StateMachineEvent.Success -> {}
                is StateMachineEvent.Failure -> showErrorToast()
                else -> Unit
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(context, R.string.error_removing_favorites, Toast.LENGTH_LONG).show()
    }

    private fun initializeAdapter() {
        binding.charactersRv.apply {
            layoutManager = GridLayoutManager(context, gridAdapter.spanCount)
            adapter = gridAdapter
        }
    }

    private companion object {
        private const val ADAPTER_COLUMNS = 2
    }

}
