package kds.api.item

import kds.api.model.JsonModel

interface IItemDSL {
    /**
     * Defines a single block to be added into the game
     */
    fun item(definition: ItemBuilder.() -> Unit)
}

class ItemBuilder {
    /**
     * The internal name of the block
     */
    var name: String? = null

    /**
     * Localized name of the item when no localization file is found
     */
    var defaultLocalizedName: String? = null

    var display: ItemDisplay? = null
}


sealed class ItemDisplay

class ItemSprite(val path: String) : ItemDisplay()

class BlockCube(
    val up: String,
    val down: String,
    val north: String,
    val south: String,
    val east: String,
    val west: String,
    val particle: String
) : ItemDisplay() {
    constructor(default: String) : this(default, default, default, default, default, default, default)
}

class CustomDisplay(val model: JsonModel) : ItemDisplay()
