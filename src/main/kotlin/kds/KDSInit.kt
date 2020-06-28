package kds

import kds.internal.ScriptingEngine
import net.fabricmc.api.ModInitializer

@Suppress("unused")
class KDSInit : ModInitializer {

    override fun onInitialize() {
        // Force initialization
        ScriptingEngine
    }
}