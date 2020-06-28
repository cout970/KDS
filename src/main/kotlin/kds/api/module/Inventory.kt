package kds.api.module

import kds.api.block.BlockEntityBuilder
import kds.api.block.blockentity.ModuleState
import net.minecraft.inventory.SimpleInventory
import net.minecraft.util.Identifier

class InventoryModuleDSL {
    var slots: Int = 1
}

data class InventoryState(val inventory: SimpleInventory) : ModuleState

fun BlockEntityBuilder.inventory(dsl: InventoryModuleDSL.() -> Unit) {
    val config = InventoryModuleDSL().apply(dsl)

    module<InventoryState> {
        id = Identifier("kds", "inventory")
        onCreate = { InventoryState(SimpleInventory(config.slots)) }
    }
}