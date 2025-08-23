package org.codetomermaid.dto

import com.github.javaparser.ast.expr.MethodCallExpr
import java.nio.file.Path

data class MethodCall(
    val method: MethodCallExpr,
    val name: String,
    val pathToClass: Path? = null,
)
