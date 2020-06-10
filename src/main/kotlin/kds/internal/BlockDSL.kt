package kds.internal

import kds.api.block.BlockBuilder
import kds.api.block.IBlockDSL
import kds.api.item.ItemBuilder
import kds.api.model.BlockstateVariantBuilder
import kds.internal.client.ModelManager
import kds.internal.client.TranslationManager
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.client.render.model.ModelRotation
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.ModelVariant
import net.minecraft.client.render.model.json.WeightedUnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class BlockDSL(private val ref: ModReference) : IBlockDSL {

    override fun block(definition: BlockBuilder.() -> Unit) {
        val builder = BlockBuilder()
        builder.apply(definition)

        if (builder.name == null || builder.material == null) {
            println("Block definition incomplete: name and material are required")
        }

        val id = Identifier(ref.modid, builder.name)

        if (!Registry.BLOCK.ids.contains(id)) {
            KDSBlock._constructor_config_ = builder

            val block = KDSBlock(builder, Block.Settings.of(Material.STONE))
            Registry.register(Registry.BLOCK, id, block)
        }

        val block = Registry.BLOCK[id] as KDSBlock

        builder.blockStateConfig?.let { config ->

            if (config.customModel != null) {
                ModelManager.registerBlockstate(id, config.customModel!!)

            } else if (config.modelVariants != null) {

                ModelManager.registerBlockstate(id, getterFromVariants(id, config.modelVariants!!))
            }
        }

        val itemBlock = builder.blockItem ?: return
        registerBlockItem(block, itemBlock)
    }

    private fun getterFromVariants(
        id: Identifier,
        variants: Map<String, MutableList<BlockstateVariantBuilder>>
    ): (ModelIdentifier, BlockState) -> UnbakedModel? {
        val map = mutableMapOf<String, UnbakedModel>()

        variants.forEach { (name, models) ->

            val modelVariants = models.mapNotNull { variant ->

                ModelManager.registerDisplay(id, variant.display, false)?.let { modelId ->
                    val rot = variant.rotation?.toRotation3() ?: ModelRotation.X0_Y0.rotation

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