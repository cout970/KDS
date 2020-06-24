package kds.api.module

import kds.api.block.BlockEntityBuilder
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

data class InventoryState(val slots: List<ItemStack>)

fun BlockEntityBuilder.inventory(dsl: InventoryModuleDSL.() -> Unit) {
    val config = InventoryModuleDSL().apply(dsl)

    module {
        id = Identifier("kds", "inventory")
        onInit = { mod ->
            mod.persistentState = SimpleInventory(config.slots)
        }
    }
}

class InventoryModuleDSL {
    var slots: Int = 1
}