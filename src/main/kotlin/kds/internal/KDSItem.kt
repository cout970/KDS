package kds.internal

import kds.api.item.ItemBuilder
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

class KDSItem(settings: Settings) : Item(settings) {
    lateinit var config: ItemBuilder
}

class KDSBlockItem(val block: KDSBlock, settings: Settings) : BlockItem(block, settings) {
    lateinit var config: ItemBuilder
}