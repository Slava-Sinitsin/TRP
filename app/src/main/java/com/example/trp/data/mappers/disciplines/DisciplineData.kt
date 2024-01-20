package com.example.trp.data.mappers.disciplines

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class DisciplineData(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("halfYear") val halfYear: String? = null,
    @SerializedName("deprecated") val deprecated: Boolean? = null
)