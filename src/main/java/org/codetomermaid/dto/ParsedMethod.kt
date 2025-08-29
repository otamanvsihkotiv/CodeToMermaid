package org.codetomermaid.dto

data class ParsedMethod(
    val name: String,
    val signature: String,
    val returnType: String,
    val methodCalls: List<MethodCall>,
)
