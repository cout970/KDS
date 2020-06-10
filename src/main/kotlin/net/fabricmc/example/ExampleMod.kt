package net.fabricmc.example

import com.google.gson.JsonParser
import kds.internal.ScriptingEngine
import java.io.File

@Suppress("unused")
fun init() {
    println("Hello Fabric world!")

    ScriptingEngine.initialize()

    // TODO
    val config = JsonParser().parse(File("../src/main/kotlin/scripts/mod.json").reader()).asJsonObject
    val base = config["base_script"]?.asString

    base?.let { ScriptingEngine.initScript(it) }
}
