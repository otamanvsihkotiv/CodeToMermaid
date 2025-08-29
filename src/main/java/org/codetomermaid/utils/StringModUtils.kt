package org.codetomermaid.utils

fun String.stripPackageNames(): String {
    return this.replace("([a-zA-Z0-9_]+\\.)+".toRegex(), "")
}

// for tests
fun normalizeEOL(text: String): String = text.replace("\r\n", "\n").trimEnd()