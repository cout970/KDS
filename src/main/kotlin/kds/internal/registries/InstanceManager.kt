package kds.internal.registries

import kds.api.block.BlockBuilder
import kds.api.block.BlockEntityBuilder
import kds.api.item.ItemBuilder
import kds.internal.block.KDSBlock
import kds.internal.block.KDSBlockWithEntity
import kds.internal.block.blockentity.KDSBlockEntity
import kds.internal.block.blockentity.KDSTickableBlockEntity
import kds.internal.item.KDSBlockItem
import kds.internal.item.KDSItem
import kds.internal.registries.InstanceManager.blockOverrides
import net.minecraft.block.AbstractBlock
import net.minecraft.item.Item
import net.minecraft.util.Identifier

/**
 * This object controls the creation of instances for Blocks, Items, BlockEntities, etc.
 * If the provided API is not enough for you needs you can bypass the API using this object.
 *
 * You can make subclasses of the KDS classes and register a constructor function.
 * For example, to use a custom block class you need:
 * - Create a Class that extends [KDSBlock]
 * - Add a constructor function to [blockOverrides] like this:
 * ```kotlin
 * InstanceManager.blockOverrides[id("my_mod", "block_name")] = { builder, settings -> MyCustomBlock(builder, settings) }
 * ```
 */
object InstanceManager {

    val blockOverrides = mutableMapOf<Identifier, (BlockBuilder, AbstractBlock.Settings) -> KDSBlock>()
    val blockEntityOverrides = mutableMapOf<Identifier, (BlockEntityBuilder) -> KDSBlockEntity>()
    val itemOverrides = mutableMapOf<Identifier, (ItemBuilder, Item.Settings) -> KDSItem>()
    val blockItemOverrides = mutableMapOf<Identifier, (KDSBlock, ItemBuilder, Item.Settings) -> KDSBlockItem>()

    fun newKDSBlock(
        id: Identifier,
        builder: BlockBuilder,
        settings: AbstractBlock.Settings,
        withBlockEntity: Boolean
    ): KDSBlock {
        blockOverrides[id]?.let { func ->
            return func(builder, settings)
        }

        KDSBlock._constructor_config_ = builder
        return if (withBlockEntity)
            KDSBlockWithEntity(settings)
        else
            KDSBlock(settings)
    }

    fun newKDSBlockEntity(
        id: Identifier,
        config: BlockEntityBuilder,
        tickable: Boolean
    ): KDSBlockEntity {
        blockEntityOverrides[id]?.let { func ->
            return func(config)
        }

        return if (tickable)
            KDSTickableBlockEntity(config)
        else
            KDSBlockEntity(config)
    }

    fun newKDSItem(
        id: Identifier,
        builder: ItemBuilder,
        settings: Item.Settings
    ): KDSItem {
        itemOverrides[id]?.let { func ->
            return func(builder, settings)
        }

        return KDSItem(settings)
    }

    fun newKDSBlockItem(
        id: Identifier,
        block: KDSBlock,
        builder: ItemBuilder,
        settings: Item.Settings
    ): KDSBlockItem {
        blockItemOverrides[id]?.let { func ->
            return func(block, builder, settings)
        }

        return KDSBlockItem(block, settings)
    }
}