package com.example.trp.data.tasks

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("year") var year: Int? = null,
    @SerializedName("halfYear") var halfYear: String? = null
)