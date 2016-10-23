package com.soywiz.util

import java.io.File

operator fun File.get(child: String) = File(this, child)
operator fun File.set(child: String, contents: ByteArray) {
    this[child].writeBytes(contents)
}

fun File.listdirRecursively(): List<File> {
    val out = arrayListOf<File>()
    this.listdirRecursively { out += it }
    return out
}

fun File.listdirRecursively(emit: (file: File) -> Unit) {
    for (child in this.listFiles()) {
        emit(child)
        if (child.isDirectory) {
            child.listdirRecursively(emit)
        }
    }
}