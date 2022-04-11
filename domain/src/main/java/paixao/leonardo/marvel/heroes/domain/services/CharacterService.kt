package paixao.leonardo.marvel.heroes.domain.services

import paixao.leonardo.marvel.heroes.domain.models.MarvelCharacter

interface CharacterService {
    suspend fun retrieveCharacters(isRefreshing: Boolean): List<MarvelCharacter>
}
