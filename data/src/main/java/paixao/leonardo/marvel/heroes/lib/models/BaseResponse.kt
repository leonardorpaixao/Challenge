package paixao.leonardo.marvel.heroes.lib.models

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val results: T,
    val count: Int,
    val total: Int
)