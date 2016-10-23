package com.soywiz.util

fun String.parseInt(): Int = this.parseInt { 0 }

inline fun String.parseInt(default: (() -> Int)): Int = try {
	this.toInt()
} catch (e: Throwable) {
	default()
}


fun Char.parseInt(): Int {
	if ((this >= '0') && (this <= '9')) return (this - '0')
	if ((this >= 'a') && (this <= 'z')) return (this - 'a') + 10
	if ((this >= 'A') && (this <= 'Z')) return (this - 'A') + 10
	return -1
}

fun String.parseHex(): ByteArray {
	val out = ByteArray(this.length / 2)
	var p = 0
	var pos = 0
	while (p < this.length) {
		val c = this[p++]
		when (c) {
			' ' -> Unit
			else -> {
				val c2 = this[p++]
				out[pos++] = ((c.parseInt() shl 4) or (c2.parseInt())).toByte()
			}
		}
	}
	return out.copyOf(pos)
}
