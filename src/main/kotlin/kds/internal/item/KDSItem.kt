package kds.internal.item

import kds.api.item.ItemBuilder
import kds.internal.block.KDSBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

open class KDSItem(settings: Settings) : Item(settings) {
    lateinit var config: ItemBuilder
}

open class KDSBlockItem(val block: KDSBlock, settings: Settings) : BlockItem(block, settings) {
    lateinit var config: ItemBuilder
}