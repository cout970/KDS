package kds.internal

import kds.api.IModReference
import kds.api.block.IBlockDSL
import kds.api.item.IItemDSL

class ModReference(
    override val modid: String,
    override var name: String,
    override var description: String
) : IModReference {

    override fun blocks(dsl: IBlockDSL.() -> Unit) {
        try {
            BlockDSL(this).apply(dsl)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun items(dsl: IItemDSL.() -> Unit) {
        try {
            ItemDSL(this).apply(dsl)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}