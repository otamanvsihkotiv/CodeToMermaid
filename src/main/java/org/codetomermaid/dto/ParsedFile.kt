package org.codetomermaid.dto

data class ParsedFile(
    val className: String? = null, // todo: A file can contain multiple classes
    val methods: List<ParsedMethod> = listOf(),
)
