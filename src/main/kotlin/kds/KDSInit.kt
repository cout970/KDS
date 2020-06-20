package kds

import kds.api.API
import net.fabricmc.api.ModInitializer

@Suppress("unused")
class KDSInit : ModInitializer {

    override fun onInitialize() {
        println("Hello Fabric world!")
        API.registerScript("scripts/Mod.kts")
        println("Initialization done")
    }
}