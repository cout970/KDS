package kds.api

import kds.api.registries.IRegistries
import kds.internal.ModReference
import kds.internal.registries.Registries

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
     * List of game registries
     */
    fun registries(): IRegistries = Registries
}