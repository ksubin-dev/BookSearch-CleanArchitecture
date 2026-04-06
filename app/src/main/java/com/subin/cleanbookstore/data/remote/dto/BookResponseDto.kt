package com.subin.cleanbookstore.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookSearchResponse(
    @SerializedName("items") val items: List<BookDto>?
)

data class BookDto(
    @SerializedName("id") val id: String?,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfoDto?,
    @SerializedName("saleInfo") val saleInfo: SaleInfoDto?
)

data class VolumeInfoDto(
    @SerializedName("title") val title: String?,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("imageLinks") val imageLinks: ImageLinksDto?
)

data class ImageLinksDto(
    @SerializedName("thumbnail") val thumbnail: String?
)

data class SaleInfoDto(
    @SerializedName("buyLink") val buyLink: String?
)