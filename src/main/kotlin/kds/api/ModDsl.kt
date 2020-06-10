package kds.api

import kds.api.block.IBlockDSL
import kds.api.item.IItemDSL

class ModDsl {
    /** The modid is required and must not collide with other mods */
    var modid: String? = null

    /** Human readable name of the mod */
    var name: String? = null

    /** Short description of the mod */
    var description: String? = null
}

interface IModReference {
    val modid: String
    var name: String
    var description: String

    /**
     * Definition of the blocks of the mod, can be called multiple times
     */
    fun blocks(dsl: IBlockDSL.() -> Unit)

    /**
     * Definition of the blocks of the mod, can be called multiple times
     */
    fun items(dsl: IItemDSL.() -> Unit)
}