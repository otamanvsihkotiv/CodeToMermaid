package org.codetomermaid.diagram.sequence

import com.github.javaparser.StaticJavaParser
import org.codetomermaid.javaparser.SeqDVisitor
import org.codetomermaid.javaparser.createTypeResolver
import java.nio.file.Files
import java.nio.file.Path

fun toSequenceDiagram(
    filePath: Path,
    entryMethod: String,
    sourceFolder: Path,
    genFileName: String
) {
    createTypeResolver(sourceFolder)

    val lines = mutableListOf<String>()
    lines.add("```mermaid")
    lines.add("sequenceDiagram")

    parseAndDescribe(
        path = filePath,
        targetMethod = entryMethod,
        callerClassName = null,
        lines = lines,
    )

    lines.add("```")

    // create file
    val saveTo = Path.of("target", "generated-diagrams", "sequencediagram", "$genFileName.md")
    Files.createDirectories(saveTo.parent)

    Files.write(saveTo, lines)
}

private fun parseAndDescribe(
    path: Path,
    targetMethod: String,
    callerClassName: String?,
    lines: MutableList<String>,
) {
    // parse the file defined by path
    val visitor = SeqDVisitor()
    StaticJavaParser.parse(path).accept(visitor, null)
    val parsedFile = visitor.parsedFile
    val className = parsedFile.className

    // describe call from prev class to this class if not the entry method
    if (callerClassName != null) {
        lines.add("$TAB$callerClassName $MESSAGE_ARROW ${className}: $targetMethod()")
    }

    // find method decl and look through called methods in it
    val method = parsedFile.methods.find { it.name == targetMethod }

    requireNotNull(method) {
        "No method named $targetMethod in class ${className}.\n" +
                "Methods found in class ${className}: ${parsedFile.methods.forEach { println(it.name) }}"
    }

    // continue recursively until we find a method that doesn't call any (known) methods
    method.methodCalls.forEach {
        if (it.pathToClass != null) {
            parseAndDescribe(
                it.pathToClass,
                it.name,
                callerClassName = className ?: "???",
                lines = lines,
            )
        }
    }

    // describe reply messages
    if (callerClassName != null) {
        lines.add("$TAB${parsedFile.className} $RETURN_ARROW $callerClassName: ${method.returnType}")
    }

}