package paixao.leonardo.marvel.heroes.feature.character.screens.listing.entries

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem
import paixao.leonardo.marvel.heroes.domain.models.MarvelCharacter
import paixao.leonardo.marvel.heroes.feature.R
import paixao.leonardo.marvel.heroes.feature.databinding.ItemCharacterBinding

class CharacterItemEntry(
    private val character: MarvelCharacter,
    private val favoriteAction: (MarvelCharacter, AppCompatImageView) -> Unit = { _, _ -> },
    private val navigateToDetailsAction: (MarvelCharacter, AppCompatImageView) -> Unit = { _, _ -> }
) : BindableItem<ItemCharacterBinding>() {
    override fun bind(viewBinding: ItemCharacterBinding, position: Int) {
        viewBinding.apply {
            characterName.text = character.name

            Glide.with(viewBinding.root.context)
                .load(character.imageUrl)
                .into(viewBinding.imageView)

            viewBinding.imageView.setOnClickListener {
                navigateToDetailsAction(character, viewBinding.starImg)
            }

            viewBinding.starImg.apply {
                val starImgDrawable = context.retrieveStarImgDrawable(character)
                starImgDrawable?.let(::setImageDrawable)
                setOnClickListener {
                    favoriteAction(character, viewBinding.starImg)
                }
            }

        }
    }

    private fun Context.retrieveStarImgDrawable(character: MarvelCharacter): Drawable? {
        val imgRes = if (character.isFavorite)
            R.drawable.ic_filled_star
        else R.drawable.ic_star

        return ResourcesCompat.getDrawable(resources, imgRes, theme)
    }

    override fun getLayout(): Int = R.layout.item_character

    override fun initializeViewBinding(view: View): ItemCharacterBinding =
        ItemCharacterBinding.bind(view)

}