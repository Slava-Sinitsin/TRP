package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class TestsInfo(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("stdout") var stdout: String? = null,
    @SerializedName("stderr") var stderr: String? = null,
    @SerializedName("open") var open: Boolean? = null,
    @SerializedName("input") var input: String? = null,
    @SerializedName("output") var output: String? = null,
    @SerializedName("expected") var expected: String? = null
)