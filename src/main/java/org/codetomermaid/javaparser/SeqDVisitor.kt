package org.codetomermaid.javaparser

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import org.codetomermaid.dto.MethodCall
import org.codetomermaid.dto.ParsedFile
import org.codetomermaid.dto.ParsedMethod
import org.codetomermaid.utils.getPathFromType
import org.codetomermaid.utils.stripPackageNames
import java.util.function.Consumer

/*
 * SequenceDiagramVisitor
 * We start with assuming one class per file
 * */
class SeqDVisitor : VoidVisitorAdapter<Void?>() {
    var parsedFile = ParsedFile()

    override fun visit(n: ClassOrInterfaceDeclaration, collector: Void?) {
        parsedFile = parsedFile.copy(className = n.name.toString())
        super.visit(n, null)
    }

    override fun visit(md: MethodDeclaration, collector: Void?) {
        val rmdec = md.resolve()

        val methodCallExprs = mutableListOf<MethodCallExpr>()
        md.body.ifPresent(Consumer { body: BlockStmt ->
            methodCallExprs.addAll(body.findAll(MethodCallExpr::class.java))
        })

        // resolve type and get the path to the relevant class
        val methodCalls = methodCallExprs.map {
            MethodCall(
                method = it,
                name = it.name.asString(),
                pathToClass = getPathFromType(it)
            )
        }

        val updatedMethodsList = parsedFile.methods.toMutableList()
        updatedMethodsList.addLast(
            ParsedMethod(
                name = rmdec.name,
                signature = rmdec.signature,
                returnType = rmdec.returnType.describe().stripPackageNames(),
                methodCalls = methodCalls
            )
        )
        parsedFile = parsedFile.copy(methods = updatedMethodsList)

        super.visit(md, null)
    }
}
