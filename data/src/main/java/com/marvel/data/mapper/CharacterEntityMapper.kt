package com.marvel.data.mapper

import com.marvel.data.model.CharacterTable
import com.marvel.data.model.MarvelCharacterResponse
import com.marvel.domain.entity.*

object CharacterEntityMapper {
    fun mapperToCharacter(characterResponse: MarvelCharacterResponse): MarvelCharacter =
        MarvelCharacter(
            characterResponse.code,
            characterResponse.status,
            characterResponse.copyright,
            characterResponse.attributionText,
            characterResponse.attributionHTML,
            Data(
                characterResponse.data.offset,
                characterResponse.data.limit,
                characterResponse.data.total,
                characterResponse.data.count,
                characterResponse.data.results.map { result ->
                    Result(
                        result.id,
                        result.name,
                        result.description,
                        result.modified,
                        result.resourceURI,
                        ThumbNail(
                            result.thumbnail.path,
                            result.thumbnail.extension
                        ),
                        Comics(
                            result.comics.available,
                            result.comics.collectionURI,
                            result.comics.returned,
                            result.comics.items.map {
                                Item(
                                    it.name,
                                    it.resourceURI
                                )
                            }
                        ),
                        Series(
                            result.series.available,
                            result.series.collectionURI,
                            result.series.returned,
                            result.series.items.map {
                                Item(
                                    it.name,
                                    it.resourceURI
                                )
                            }
                        ),
                        Stories(
                            result.stories.available,
                            result.stories.collectionURI,
                            result.stories.returned,
                            result.stories.items.map {
                                StoryItem(
                                    it.name,
                                    it.resourceURI,
                                    it.type
                                )
                            }
                        ),
                        Events(
                            result.events.available,
                            result.events.collectionURI,
                            result.events.returned,
                            result.events.items.map {
                                Item(
                                    it.name,
                                    it.resourceURI
                                )
                            }
                        ),
                        result.urls.map {
                            Urls(
                                it.type,
                                it.url
                            )
                        },
                        false
                    )
                }
            )
        )

    fun mapperToCharacterTable(marvelCharacterLocal: MarvelCharacterLocal): CharacterTable =
        CharacterTable(
            marvelCharacterLocal.id,
            marvelCharacterLocal.name,
            marvelCharacterLocal.image
        )

    fun mapperToCharacterLocal(result: List<CharacterTable>): List<MarvelCharacterLocal> =
        result.map {
            MarvelCharacterLocal(it.id, it.name, it.image)
        }

}