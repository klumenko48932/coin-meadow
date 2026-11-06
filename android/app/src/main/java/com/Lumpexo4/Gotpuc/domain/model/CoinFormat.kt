package com.Lumpexo4.Gotpuc.domain.model

fun formatCoins(value: Int): String {
    val digits = value.coerceAtLeast(0).toString()
    if (digits.length <= 3) return digits
    val builder = StringBuilder()
    val lead = digits.length % 3
    if (lead > 0) builder.append(digits, 0, lead)
    var index = lead
    while (index < digits.length) {
        if (builder.isNotEmpty()) builder.append(' ')
        builder.append(digits, index, index + 3)
        index += 3
    }
    return builder.toString()
}

fun formatMultiplier(value: Float): String {
    val rounded = (value * 10f).toInt()
    val whole = rounded / 10
    val fraction = rounded % 10
    return "$whole.$fraction"
}
