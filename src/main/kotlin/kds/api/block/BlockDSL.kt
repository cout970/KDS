package kds.api.block

import kds.api.item.ItemBuilder
import kds.api.model.BlockStateBuilder
import kds.api.module.Module

interface IBlockDSL {
    /**
     * Defines a single block to be added into the game
     */
    fun block(definition: BlockBuilder.() -> Unit)
}

class BlockBuilder {
    /**
     * The internal name of the block
     */
    var name: String? = null

    /**
     * The material of the block
     */
    var material: String? = null

    /**
     * Item that represents the block in inventories
     */
    var blockItem: ItemBuilder? = null

    var blockEntityConfig: BlockEntityBuilder? = null

    var blockStateConfig: BlockStateBuilder? = null

    fun item(func: ItemBuilder.() -> Unit) {
        val builder = blockItem ?: ItemBuilder()
        blockItem = builder.apply(func)
    }

    /**
     * Defines a BlockEntity for this block
     */
    fun blockEntity(func: BlockEntityBuilder.() -> Unit) {
        val builder = blockEntityConfig ?: BlockEntityBuilder()
        blockEntityConfig = builder.apply(func)
    }

    /**
     * Defines a blockstate json
     */
    fun blockstate(func: BlockStateBuilder.() -> Unit) {
        val builder = blockStateConfig ?: BlockStateBuilder()
        blockStateConfig = builder.apply(func)
    }
}

class BlockEntityBuilder {
    val modules = mutableListOf<Module<*, *>>()

    /**
     * Register a module for this BlockEntity
     *
     * Normally you would use the builder function in the module instead of this method
     */
    fun withModule(mod: Module<*, *>) {
        if (modules.any { it.name == mod.name }) error("There is already an module with id ${mod.name}")
        modules.add(mod)
    }
}
