package example_mod

import kds.api.IModReference
import kds.api.Scripting
import net.fabricmc.api.ModInitializer

@Suppress("unused")
class ExampleMod : ModInitializer {

    companion object {
        lateinit var ref: IModReference
    }

    override fun onInitialize() {
        Scripting.registerScript("example_mod/scripts/Mod.kts")
    }
}