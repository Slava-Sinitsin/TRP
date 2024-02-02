package com.example.trp.data.mappers

data class CheckBoxState(
    val isSelected: Boolean = false,
    val isEnable: Boolean = true,
    val enableAlpha: Float = if (isEnable) 1.0f else 0.6f
)