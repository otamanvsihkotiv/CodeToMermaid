package org.codetomermaid.diagram.sequence

import com.github.javaparser.StaticJavaParser
import org.codetomermaid.dto.ParsedMethod
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
    val visitor = SeqDVisitor()

    // parse entry file
    val entryFileCU = StaticJavaParser.parse(filePath)
    entryFileCU.accept(visitor, null)
    val parsedFileEntry = visitor.parsedFile

    // find entry method
    val methods = parsedFileEntry.methods
    val pm: ParsedMethod? = methods.find { it.name == entryMethod }
    requireNotNull(pm) {
        "No method named $entryMethod in class ${parsedFileEntry.className}.\n" +
                "Methods found in class ${parsedFileEntry.className}: ${methods.forEach { println(it.name) }}"
    }

    val lines = mutableListOf<String>()
    lines.add("```mermaid")
    lines.add("sequenceDiagram")

    pm.methodCalls.forEach {
        if (it.pathToClass != null) {
            parseAndAppend(
                it.pathToClass,
                it.name,
                callerClassName = parsedFileEntry.className ?: "WTF",
                lines = lines,
            )
        }
    }
    lines.add("```")

    // create file
    val saveTo = Path.of("target", "generated-diagrams", "sequencediagram", "$genFileName.md")
    Files.createDirectories(saveTo.parent)

    Files.write(saveTo, lines)
}

fun parseAndAppend(
    path: Path,
    targetMethod: String,
    callerClassName: String,
    lines: MutableList<String>
) {
    // parse next file and save it to visited so we don't have to parse it again (just in case)
    val visitor = SeqDVisitor()
    StaticJavaParser.parse(path).accept(visitor, null)
    val parsedFile = visitor.parsedFile

    // describe call from prev class to this class
    lines.add("$TAB$callerClassName $MESSAGE_ARROW ${parsedFile.className}: $targetMethod()")

    // find method decl and look through called methods in it
    val method = parsedFile.methods.find { it.name == targetMethod }
    requireNotNull(method) { "$targetMethod not found in ${parsedFile.className}" }

    // continue recursively until we find a method that doesn't call any (known) methods
    method.methodCalls.forEach {
        if (it.pathToClass != null) {
            parseAndAppend(
                it.pathToClass,
                it.name,
                callerClassName = parsedFile.className ?: "???",
                lines = lines,
            )
        }
    }

    // describe reply messages
    lines.add("$TAB${parsedFile.className} $RETURN_ARROW $callerClassName: ${method.returnType}")

}