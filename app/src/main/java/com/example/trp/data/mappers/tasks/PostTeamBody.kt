package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class PostTeamBody(
    @SerializedName("disciplineId") val disciplineId: Int? = null,
    @SerializedName("groupId") val groupId: Int? = null,
    @SerializedName("studentIds") val studentIds: List<Int>? = null,
    @SerializedName("leaderId") val leaderId: Int? = null
)