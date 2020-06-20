package kds.internal

import kds.api.item.IItemDSL
import kds.api.item.ItemBuilder
import kds.internal.client.ModelManager
import kds.internal.client.TranslationManager
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class ItemDSL(private val ref: ModReference) : IItemDSL {

    override fun item(definition: ItemBuilder.() -> Unit) {
        val builder = ItemBuilder()
        builder.apply(definition)

        if (builder.name == null) {
            error("Item definition incomplete: name and material are required")
        }

        val id = Identifier(ref.modid, builder.name)

        if (!Registry.ITEM.ids.contains(id)) {
            val item = KDSItem(Item.Settings().group(ItemGroup.MISC))
            Registry.register(Registry.ITEM, id, item)
            ref.logger().info("Registering item ${builder.name}")
        }

        val item = Registry.ITEM[id] as KDSItem
        item.config = builder

        ModelManager.registerDisplay(id, builder.display, true)

        builder.defaultLocalizedName?.let {
            TranslationManager.registerDefaultTranslation(item.translationKey, it)
        }
    }
}