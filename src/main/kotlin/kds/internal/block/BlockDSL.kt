package kds.internal.block

import kds.api.block.BlockBuilder
import kds.api.block.BlockEntityBuilder
import kds.api.block.IBlockDSL
import kds.api.item.ItemBuilder
import kds.api.model.BlockstateVariantBuilder
import kds.internal.ModReference
import kds.internal.block.blockentity.KDSBlockEntity
import kds.internal.block.blockentity.KDSTickableBlockEntity
import kds.internal.client.ModelManager
import kds.internal.client.TranslationManager
import kds.internal.item.KDSBlockItem
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.model.ModelRotation
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.ModelVariant
import net.minecraft.client.render.model.json.WeightedUnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Supplier

class BlockDSL(private val ref: ModReference) : IBlockDSL {

    override fun block(definition: BlockBuilder.() -> Unit): Identifier {
        val builder = BlockBuilder()
        builder.apply(definition)

        if (builder.name == null || builder.material == null) {
            error("Block definition incomplete: name and material are required")
        }

        val id = Identifier(ref.modid, builder.name)

        if (!Registry.BLOCK.ids.contains(id)) {
            val settings = AbstractBlock.Settings.of(Material.STONE)

            KDSBlock._constructor_config_ = builder
            val block = if (builder.blockEntityConfig == null)
                KDSBlock(builder, settings)
            else
                KDSBlockWithEntity(builder, settings)

            Registry.register(Registry.BLOCK, id, block)
            ref.logger().info("Registering block ${builder.name}")
        }

        val block = Registry.BLOCK[id] as KDSBlock

        builder.blockStateConfig?.let { config ->

            if (config.customModel != null) {
                ModelManager.registerBlockstate(id, config.customModel!!)

            } else if (config.modelVariants != null) {

                ModelManager.registerBlockstate(id, getterFromVariants(id, config.modelVariants!!))
            }
        }

        builder.blockEntityConfig?.let { config ->
            registerBlockEntity(block, id, config)
        }

        val itemBlock = builder.blockItem ?: return id
        registerBlockItem(block, itemBlock)
        ref.logger().info("Registering blockitem ${builder.name}")

        return id
    }

    private fun registerBlockEntity(block: Block, id: Identifier, config: BlockEntityBuilder) {
        val typeId = config.type ?: id
        config.type = typeId
        if (Registry.BLOCK_ENTITY_TYPE[typeId] != null) return

        val tickable = config.modules.any { it.value.onTick != null }

        val type = BlockEntityType.Builder.create(
            Supplier { if (tickable) KDSTickableBlockEntity(config) else KDSBlockEntity(config) }, block
        ).build(null)

        Registry.register(Registry.BLOCK_ENTITY_TYPE, typeId, type)
    }

    private fun getterFromVariants(
        id: Identifier,
        variants: Map<String, MutableList<BlockstateVariantBuilder>>
    ): (ModelIdentifier, BlockState) -> UnbakedModel? {
        val map = mutableMapOf<String, UnbakedModel>()

        variants.forEach { (name, models) ->

            val modelVariants = models.mapNotNull { variant ->

                ModelManager.registerDisplay(id, variant.display, false)?.let { modelId ->
                    val rot = variant.rotation?.toAffineTransformation() ?: ModelRotation.X0_Y0.rotation

                    ModelVariant(modelId, rot, variant.uvLock, 1)
                }
            }

            map[name] = WeightedUnbakedModel(modelVariants)
        }

        return { modelId, _ -> map[modelId.variant] }
    }

    private fun registerBlockItem(block: KDSBlock, builder: ItemBuilder) {
        val id = Identifier(ref.modid, builder.name ?: block.config.name)

        if (!Registry.ITEM.ids.contains(id)) {
            val item = KDSBlockItem(block, Item.Settings().group(ItemGroup.MISC))
            Registry.register(Registry.ITEM, id, item)
        }

        val item = Registry.ITEM[id] as KDSBlockItem
        item.config = builder

        ModelManager.registerDisplay(id, builder.display, true)

        builder.defaultLocalizedName?.let {
            TranslationManager.registerDefaultTranslation(item.translationKey, it)
        }
    }
}