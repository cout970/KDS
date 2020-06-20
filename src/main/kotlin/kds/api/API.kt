package kds.api

import kds.api.registries.IRegistries
import kds.api.util.Promise
import kds.internal.ModReference
import kds.internal.Registries
import kds.internal.ScriptingEngine

object API {

    /**
     * Defines a mod
     *
     * This mod is an internal representation for KDS and will not be recognized by Fabric nor by other native mods
     * The returned reference allows to register content owned by the mod
     */
    fun mod(dsl: ModDsl.() -> Unit): IModReference {
        val config = ModDsl().apply(dsl)

        if (config.modid == null) {
            error("Mod script didn't define a modid, it is required!")
        }

        return ModReference(
            modid = config.modid!!,
            name = config.name ?: config.modid!!,
            description = config.description ?: ""
        )
    }

    /**
     * Register a file as a main script, any changes to it or any subscript will trigger a re-execution
     */
    fun registerScript(relativePath: String): Promise<Unit> {
        return ScriptingEngine.initScript(relativePath)
    }

    /**
     * Schedules the execution of another script
     *
     * NOTE: the script execution will start after the current script finish executing
     *
     * The path is relative to the location of the mod.json file
     * A set of parameters can be sent to the script, use API.get(name) to retrieve it
     */
    fun include(path: String, vararg variables: Pair<String, Any>): Promise<Unit> {
        @Suppress("UNCHECKED_CAST")
        return ScriptingEngine.includeFile(path, variables as Array<Pair<String, Any>>)
    }

    /**
     * Retrieves a parameter variable
     */
    fun <T> get(name: String): T {
        return getOrNull<T>(name) ?: error("Parameter not found")
    }

    /**
     * Retrieves a parameter variable or null if the variable isn't defined
     */
    fun <T> getOrNull(name: String): T? {
        @Suppress("UNCHECKED_CAST")
        return ScriptingEngine.getParameter(name) as T?
    }

    /**
     * List of game registries
     */
    fun registries(): IRegistries = Registries
}