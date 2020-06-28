package kds.internal.item

import kds.api.item.ItemBuilder
import kds.api.item.ItemDSL
import kds.internal.ModReference
import kds.internal.client.ModelManager
import kds.internal.client.TranslationManager
import kds.internal.registries.InstanceManager
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class KDSItemDSL(private val ref: ModReference) : ItemDSL {

    override fun item(definition: ItemBuilder.() -> Unit): Identifier {
        val builder = ItemBuilder()
        builder.apply(definition)

        if (builder.name == null) {
            error("Item definition incomplete: name and material are required")
        }

        val id = Identifier(ref.modid, builder.name)

        if (!Registry.ITEM.ids.contains(id)) {
            val settings = Item.Settings().group(ItemGroup.MISC)
            val item = InstanceManager.newKDSItem(id, builder, settings)

            Registry.register(Registry.ITEM, id, item)
            ref.logger().info("Registering item ${builder.name}")
        }

        val item = Registry.ITEM[id] as KDSItem
        item.config = builder

        ModelManager.registerDisplay(id, builder.display, true)

        builder.defaultLocalizedName?.let {
            TranslationManager.registerDefaultTranslation(item.translationKey, it)
        }

        return id
    }
}