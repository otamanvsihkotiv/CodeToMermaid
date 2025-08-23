package org.codetomermaid.javaparser

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.symbolsolver.JavaSymbolSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver
import java.nio.file.Path

fun createTypeResolver(lookUpFolder: Path) {
    val typeSolver = CombinedTypeSolver()
    typeSolver.add(ReflectionTypeSolver())
    typeSolver.add(JavaParserTypeSolver(lookUpFolder))
    val javaSymbolSolver = JavaSymbolSolver(typeSolver)
    StaticJavaParser.getParserConfiguration().setSymbolResolver(javaSymbolSolver)
}