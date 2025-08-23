package org.codetomermaid.utils

import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration
import java.nio.file.Path

fun getPathFromType(mce: MethodCallExpr): Path? {
    try {
        val scope = mce.scope.orElse(null)
        return if (scope is NameExpr) {
            val resolvedType = scope.resolve().type.asReferenceType()
            val decl = resolvedType.getTypeDeclaration().get()
            (decl as? JavaParserClassDeclaration)?.wrappedNode?.findCompilationUnit()
                ?.flatMap { it.storage }
                ?.get()
                ?.path
        } else {
            null
        }
    } catch (_: Exception) {
        return null
    }
}