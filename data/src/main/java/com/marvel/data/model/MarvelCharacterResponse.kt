package com.marvel.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarvelCharacterResponse(
    @SerialName("code") val code: Int,
    @SerialName("status") val status: String,
    @SerialName("copyright") val copyright: String,
    @SerialName("attributionText") val attributionText: String,
    @SerialName("attributionHTML") val attributionHTML: String,
    @SerialName("data") val data: Data,
)

@Serializable
data class Data(
    @SerialName("offset") val offset: Int,
    @SerialName("limit") val limit: Int,
    @SerialName("total") val total: Int,
    @SerialName("count") val count: Int,
    @SerialName("results") val results: List<Result>,
)

@Serializable
data class Result(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("modified") val modified: String,
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("thumbnail") val thumbnail: ThumbNail,
    @SerialName("comics") val comics: Comics,
    @SerialName("series") val series: Series,
    @SerialName("stories") val stories: Stories,
    @SerialName("events") val events: Events,
    @SerialName("urls") val urls: List<Urls>,
)

@Serializable
data class ThumbNail(
    @SerialName("path") val path: String,
    @SerialName("extension") val extension: String
)

@Serializable
data class Comics(
    @SerialName("available") val available: Int,
    @SerialName("collectionURI") val collectionURI: String,
    @SerialName("returned") val returned: Int,
    @SerialName("items") val items: List<ComicsItem>,
)

@Serializable
data class Series(
    @SerialName("available") val available: Int,
    @SerialName("collectionURI") val collectionURI: String,
    @SerialName("returned") val returned: Int,
    @SerialName("items") val items: List<SeriesItem>,
)

@Serializable
data class Stories(
    @SerialName("available") val available: Int,
    @SerialName("collectionURI") val collectionURI: String,
    @SerialName("returned") val returned: Int,
    @SerialName("items") val items: List<StoriesItem>,
)

@Serializable
data class Events(
    @SerialName("available") val available: Int,
    @SerialName("collectionURI") val collectionURI: String,
    @SerialName("returned") val returned: Int,
    @SerialName("items") val items: List<EventsItem>,
)

@Serializable
data class Urls(
    @SerialName("type") val type: String,
    @SerialName("url") val url: String
)

@Serializable
data class ComicsItem(
    @SerialName("name") val name: String,
    @SerialName("resourceURI") val resourceURI: String,
)

@Serializable
data class SeriesItem(
    @SerialName("name") val name: String,
    @SerialName("resourceURI") val resourceURI: String,
)

@Serializable
data class StoriesItem(
    @SerialName("name") val name: String,
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("type") val type: String
)

@Serializable
data class EventsItem(
    @SerialName("name") val name: String,
    @SerialName("resourceURI") val resourceURI: String,
)