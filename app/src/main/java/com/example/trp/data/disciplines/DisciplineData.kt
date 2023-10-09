package com.example.trp.data.disciplines

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DisciplineData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("year") var year: Int? = null,
    @SerializedName("halfYear") var halfYear: String? = null
)