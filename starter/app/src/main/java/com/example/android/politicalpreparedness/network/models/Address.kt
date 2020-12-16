package com.example.android.politicalpreparedness.network.models

data class Address(
        val line1: String? = null,
        val line2: String? = null,
        val city: String? = null,
        val state: String? = null,
        val zip: String? = null
) {
    fun toFormattedString(): String {
        var output = line1.plus("\n")
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")
        output = output.plus("$city, $state $zip")
        return output
    }
}