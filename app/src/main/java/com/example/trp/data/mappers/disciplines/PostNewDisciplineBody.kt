package com.example.trp.data.mappers.disciplines

import com.google.gson.annotations.SerializedName

data class PostNewDisciplineBody(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("halfYear") val halfYear: String? = null,
    @SerializedName("deprecated") val deprecated: Boolean? = null
)