package com.mobileweb3.cosmostools.crypto

fun List<String>.buildMnemonic(): String = joinToString(" ")

fun String.splitMnemonic(): List<String> = split(" ")