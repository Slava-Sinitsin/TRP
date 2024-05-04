package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class CloseCodeReviewResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("error") var error: String? = null
)