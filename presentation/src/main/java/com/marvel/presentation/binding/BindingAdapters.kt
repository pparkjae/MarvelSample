package com.marvel.presentation.binding

import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.marvel.domain.entity.Result
import com.marvel.domain.entity.ResultStatus
import com.marvel.domain.entity.Urls
import com.marvel.presentation.view.detail.ItemType
import com.marvel.presentation.view.detail.item.ItemTextType

@BindingAdapter("imgUrl")
fun ImageView.loadThumbnail(thumbnail: String?) {
    thumbnail?.run {
        load(thumbnail) {
            crossfade(true)
            transformations(RoundedCornersTransformation(25f))
        }
    }
}

@BindingAdapter("showProgressBar")
fun ProgressBar.bindProgressBar(boolean: Boolean) {
    visibility = if (boolean) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("showToast")
fun View.bindToast(resultStatus: ResultStatus<*>) {
    if (resultStatus is ResultStatus.Error) {
        Toast.makeText(context, resultStatus.throwable?.message, Toast.LENGTH_SHORT).show()
    }
}

@BindingAdapter("items")
fun <T> RecyclerView.setItems(resultStatus: ResultStatus<*>) {
    if (resultStatus is ResultStatus.Success) {
        (adapter as? ListAdapter<T, *>)?.submitList((resultStatus.data as MutableList<T>?)?.toMutableList())
    }
}

@BindingAdapter(value = ["itemType", "result"], requireAll = true)
fun RecyclerView.setItems(itemType: ItemType, result: ResultStatus<*>) {
    if (result is ResultStatus.Success) {
        when (itemType) {
            ItemType.COMICS -> {
                (adapter as ListAdapter<Any, *>).submitList((result.data as Result).comics.items)
            }
            ItemType.STORIES -> {
                (adapter as ListAdapter<Any, *>).submitList((result.data as Result).stories.items)
            }
            ItemType.EVENTS -> {
                (adapter as ListAdapter<Any, *>).submitList((result.data as Result).events.items)
            }
            ItemType.SERIES -> {
                (adapter as ListAdapter<Any, *>).submitList((result.data as Result).series.items)
            }
        }
    }
}

@BindingAdapter("bindWebView")
fun WebView.bindWebView(result: ResultStatus<*>) {
    if (result is ResultStatus.Success<*>) {
        loadUrl((result.data as Result).urls.run {
            val url: Urls = find {
                "wiki" == it.type
            } ?: this[0]

            url.url
        })
    }
}

@BindingAdapter("android:visibleIf")
fun View.setVisibleIf(value: Boolean) {
    isVisible = value
}

@BindingAdapter(value = ["itemType", "itemTextType", "resultStatus"], requireAll = true)
fun TextView.setCharacterName(
    itemType: ItemType,
    itemTextType: ItemTextType,
    resultStatus: ResultStatus<*>
) {
    if (resultStatus is ResultStatus.Success) {
        text = when (itemTextType) {
            ItemTextType.AVAILABLE -> {
                when (itemType) {
                    ItemType.COMICS -> {
                        "AVAILABLE : " + (resultStatus.data as Result).comics.available
                    }
                    ItemType.STORIES -> {
                        "AVAILABLE : " + (resultStatus.data as Result).stories.available
                    }
                    ItemType.EVENTS -> {
                        "AVAILABLE : " + (resultStatus.data as Result).events.available
                    }
                    ItemType.SERIES -> {
                        "AVAILABLE : " + (resultStatus.data as Result).series.available
                    }
                }
            }
            ItemTextType.NAME -> (resultStatus.data as Result).name + " - " + itemType.name
            ItemTextType.COLLECTION_URL -> {
                when (itemType) {
                    ItemType.COMICS -> {
                        "COLLECTION_URL : " + (resultStatus.data as Result).comics.collectionURI
                    }
                    ItemType.STORIES -> {
                        "COLLECTION_URL : " + (resultStatus.data as Result).stories.collectionURI
                    }
                    ItemType.EVENTS -> {
                        "COLLECTION_URL : " + (resultStatus.data as Result).events.collectionURI
                    }
                    ItemType.SERIES -> {
                        "COLLECTION_URL : " + (resultStatus.data as Result).series.collectionURI
                    }
                }
            }
            ItemTextType.RETURNED -> {
                when (itemType) {
                    ItemType.COMICS -> {
                        "RETURNED : " + (resultStatus.data as Result).comics.returned
                    }
                    ItemType.STORIES -> {
                        "RETURNED : " + (resultStatus.data as Result).stories.returned
                    }
                    ItemType.EVENTS -> {
                        "RETURNED : " + (resultStatus.data as Result).events.returned
                    }
                    ItemType.SERIES -> {
                        "RETURNED : " + (resultStatus.data as Result).series.returned
                    }
                }
            }
        }
    }
}