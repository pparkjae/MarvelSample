package com.marvel.domain.entity

data class MarvelCharacter(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val data: Data,
)

data class Data(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Result>,
)

data class Result(
    val id: Int,
    val name: String,
    val description: String,
    val modified: String,
    val resourceURI: String,
    val thumbnail: ThumbNail,
    val comics: Comics,
    val series: Series,
    val stories: Stories,
    val events: Events,
    val urls: List<Urls>,
    var isBookMark: Boolean
)

data class ThumbNail(
    val path: String,
    val extension: String
)

data class Comics(
    override val available: Int,
    override val collectionURI: String,
    override val returned: Int,
    val items: List<Item>,
) : IResultItem

data class Series(
    override val available: Int,
    override val collectionURI: String,
    override val returned: Int,
    val items: List<Item>,
) : IResultItem

data class Stories(
    override val available: Int,
    override val collectionURI: String,
    override val returned: Int,
    val items: List<StoryItem>,
) : IResultItem

data class Events(
    override val available: Int,
    override val collectionURI: String,
    override val returned: Int,
    val items: List<Item>,
) : IResultItem

data class Urls(
    val type: String,
    val url: String
)

data class StoryItem(
    val name: String,
    val resourceURI: String,
    val type: String
)

data class Item(
    val name: String,
    val resourceURI: String,
)

interface IResultItem {
    val available: Int

    val collectionURI: String

    val returned: Int
}