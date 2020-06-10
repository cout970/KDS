package kds.api.module

import kds.api.block.BlockEntityBuilder
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

data class InventoryState(val slots: List<ItemStack>)
data class InventoryConfig(var slots: Int = 1)

fun BlockEntityBuilder.inventoryModule(dsl: InventoryConfig.() -> Unit) {
    val mod = Module<InventoryState, Unit>()
    val config = InventoryConfig().apply(dsl)

    mod.name = Identifier("kds", "Inventory")
    mod.initPersistentState = { InventoryState(List(config.slots) { ItemStack.EMPTY }) }
    mod.initTempState = { Unit }

    withModule(mod)
}