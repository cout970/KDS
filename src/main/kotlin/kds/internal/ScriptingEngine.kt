package kds.internal

import net.minecraft.client.MinecraftClient
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayDeque
import javax.script.*
import kotlin.script.experimental.jsr223.KOTLIN_JSR223_RESOLVE_FROM_CLASSLOADER_PROPERTY

const val EXTENSION = "kts"

object ScriptingEngine {
    init {
        // IMPORTANT!!!
        // This must be executed before ScriptEngineManager().getEngineByExtension(extension)
        System.setProperty(KOTLIN_JSR223_RESOLVE_FROM_CLASSLOADER_PROPERTY, "true")
    }

    private var engine = ScriptEngineManager().getEngineByExtension(EXTENSION)
    private val loadedFiles = mutableSetOf<LoadedFile>()
    var currentMain: File? = null
    private var basePath = "../src/main/kotlin/scripts"
    private val queue = ArrayDeque<Pair<File, Array<Pair<String, Any>>>>()

    private data class LoadedFile(val file: File, val mainFile: File) {
        var lastModified: Long = file.lastModified()
    }

    fun initialize() {
        println("Initializing Scripting Engine...")
        engine.eval("println(\"Scripting Engine Initialized\")")
        engine.put("ScriptingEngine", ScriptingEngine)
    }

    fun initScript(path: String) {
        loadScript(File("$basePath/$path"))
    }

    fun loadScript(file: File) {
        if (!file.exists()) return
        loadedFiles += LoadedFile(file, file)
        currentMain = file
        execute(file)
        
        while (queue.isNotEmpty()) {
            val (file, params) = queue.pop()
            execute(file, params)
        }
    }

    fun includeFile(path: String, params: Array<Pair<String, Any>> = emptyArray()) {
        val file = File("$basePath/$path.kts")
        if (!file.exists()) return

        loadedFiles += LoadedFile(file, currentMain!!)
        queue.addLast(file to params)
    }

    fun tick() {
        // Run pending tasks
        while (queue.isNotEmpty()) {
            val (file, params) = queue.pop()
            execute(file, params)
        }

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
        while (queue.isNotEmpty()) {
            val (file, params) = queue.pop()
            execute(file, params)
        }
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
                ?.addChatMessage(LiteralText(msg), false)

            println("Script error$location: \n${(e.message ?: "").split("\n").joinToString("\n")}")
        }
    }
}
