package com.example.trp.data.mappers.disciplines

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DisciplineData(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("halfYear") val halfYear: String? = null,
    @SerializedName("deprecated") val deprecated: Boolean? = null
)