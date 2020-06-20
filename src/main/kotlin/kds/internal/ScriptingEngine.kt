package kds.internal

import kds.api.util.Promise
import net.minecraft.client.MinecraftClient
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException

const val EXTENSION = "kts"

object ScriptingEngine {

    private var engine: ScriptEngine
    private val loadedFiles = mutableSetOf<LoadedFile>()
    private var currentMain: File? = null
    private var basePath = "../src/main/kotlin"
    private val queue = ArrayDeque<QueuedTask>()

    init {
        // IMPORTANT!!!
        // This must be executed before ScriptEngineManager().getEngineByExtension(extension)
        // KOTLIN_JSR223_RESOLVE_FROM_CLASSLOADER_PROPERTY
        System.setProperty("kotlin.jsr223.experimental.resolve.dependencies.from.context.classloader", "true")
        System.setProperty("kotlin.java.stdlib.jar", "/var/lib/snapd/snap/kotlin/current/lib/kotlin-stdlib.jar")
        engine = ScriptEngineManager().getEngineByExtension(EXTENSION)

        println("Initializing Scripting Engine...")
        engine.eval("println(\"Scripting Engine Initialized\")")
        engine.put("ScriptingEngine", ScriptingEngine)
    }

    fun initScript(path: String): Promise<Unit> {
        return loadScript(File("$basePath/$path"))
    }

    fun loadScript(file: File): Promise<Unit> {
        if (!file.exists()) return Promise.reject(FileNotFoundException(file.absolutePath))
        loadedFiles += LoadedFile(file, file)
        currentMain = file
        execute(file)
        clearQueue()
        return Promise.resolve(Unit)
    }

    fun includeFile(path: String, params: Array<Pair<String, Any>> = emptyArray()): Promise<Unit> {
        val file = File("$basePath/$path.kts")
        if (!file.exists()) return Promise.reject(FileNotFoundException(file.absolutePath))

        loadedFiles += LoadedFile(file, currentMain!!)
        return Promise { fulfill, _ ->
            queue.addLast(QueuedTask(file, params, fulfill))
        }
    }

    private fun clearQueue() {
        while (queue.isNotEmpty()) {
            val task = queue.pop()
            execute(task.script, task.args)
            task.onComplete(Unit)
        }
    }

    fun tick() {
        // Run pending tasks
        clearQueue()

        val toUpdate = mutableSetOf<File>()

        // Check every file for modifications
        // Not the most efficient way to detect changes but it's quick
        loadedFiles.forEach { loadedFile ->
            val current = loadedFile.file.lastModified()

            // If the files has changed re-execute it's code
            if (current != loadedFile.lastModified) {
                loadedFile.lastModified = current

                toUpdate.add(loadedFile.mainFile)
            }
        }

        // For each modified mod, re-execute it's main script
        toUpdate.forEach { loadScript(it) }

        // Run pending tasks
        clearQueue()
    }

    fun getParameter(name: String): Any? {
        return engine.get(name)
    }

    private fun execute(file: File, params: Array<Pair<String, Any>> = emptyArray()) {
        try {
            val text = file.readText() // processImports(file.readText())
            params.forEach { engine.put(it.first, it.second) }

            engine.eval(text)
        } catch (e: Throwable) {
            e.cause?.printStackTrace()

            val location = if (e is ScriptException && e.lineNumber != -1) {
                " at ${file.nameWithoutExtension} ${e.lineNumber}:${e.columnNumber}"
            } else {
                " at ${file.nameWithoutExtension}"
            }

            val errors = (e.message ?: "").split("\n").joinToString("\n") { "${Formatting.RED}  $it" }
            val time = DateTimeFormatter.ISO_TIME.format(LocalDateTime.now())

            val msg = "${Formatting.RED}[$time] Script error$location: \n${errors}"

            MinecraftClient.getInstance().player
                ?.sendMessage(LiteralText(msg), false)

            println("Script error$location: \n${(e.message ?: "").split("\n").joinToString("\n")}")
        }
    }

    private class QueuedTask(
        val script: File,
        val args: Array<Pair<String, Any>>,
        val onComplete: (Unit) -> Unit)

    private data class LoadedFile(
        val file: File,
        val mainFile: File
    ) {
        var lastModified: Long = file.lastModified()
    }
}
