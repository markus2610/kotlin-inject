package me.tatarka.inject

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessors
import me.tatarka.inject.compiler.Profiler
import me.tatarka.inject.compiler.kapt.InjectCompiler
import me.tatarka.inject.compiler.ksp.InjectProcessor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class ProjectCompiler(private val target: Target, private val root: File? = null, private val profiler: Profiler? = null) {

    private val sourceFiles = mutableListOf<SourceFile>()

    fun source(fileName: String, source: String): ProjectCompiler {
        sourceFiles.add(SourceFile.kotlin(fileName, source))
        return this
    }

    fun compile() {
        val output = ByteArrayOutputStream()
        val printOut = PrintStream(output)
        val oldErr = System.err
        System.setErr(printOut)
        val result = KotlinCompilation().apply {
            sources = sourceFiles
            messageOutputStream = printOut
            root?.let { workingDir = it }
            inheritClassPath = true
            when (target) {
                Target.kapt -> {
                    annotationProcessors = listOf(InjectCompiler(profiler))
                }
                Target.ksp -> {
                    symbolProcessors = listOf(InjectProcessor(profiler))
                }
            }
        }.compile()
        System.setErr(oldErr)

        if (result.exitCode != KotlinCompilation.ExitCode.OK) {
            throw Exception(output.toString())
        }
    }
}

enum class Target {
    kapt,
    ksp
}