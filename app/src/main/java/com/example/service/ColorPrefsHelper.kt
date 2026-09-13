package com.example.service

import androidx.compose.ui.graphics.Color

fun Color.toLongValue(): Long = this.value.toLong()
fun Long.toColor(): Color = Color(this.toULong())
