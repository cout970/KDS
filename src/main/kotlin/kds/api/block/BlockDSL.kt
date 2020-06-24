package kds.api.block

import kds.api.KDS
import kds.api.block.blockentity.ModuleBuilder
import kds.api.item.ItemBuilder
import net.minecraft.util.Identifier

interface IBlockDSL {
    /**
     * Defines a single block to be added into the game
     */
    fun block(definition: BlockBuilder.() -> Unit): Identifier
}

@KDS
class BlockBuilder {
    /**
     * The internal name of the block
     */
    var name: String? = null

    /**
     * The material of the block
     */
    var material: Identifier? = null

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
    fun blockState(func: BlockStateBuilder.() -> Unit) {
        val builder = blockStateConfig ?: BlockStateBuilder()
        blockStateConfig = builder.apply(func)
    }
}

@KDS
class BlockEntityBuilder {

    val modules = mutableMapOf<Identifier, ModuleBuilder>()
    var renderDistance: Double = 64.0
    var type: Identifier? = null

    fun module(func: ModuleBuilder.() -> Unit) {
        val dsl = ModuleBuilder().apply(func)
        if (dsl.id == null) {
            error("Module defined without id")
        }
        modules[dsl.id!!] = dsl
    }
}
