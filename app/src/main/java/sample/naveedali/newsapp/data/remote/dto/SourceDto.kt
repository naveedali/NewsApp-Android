package sample.naveedali.newsapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SourceDto(
    val id: String? = null,
    val name: String? = null,
)
