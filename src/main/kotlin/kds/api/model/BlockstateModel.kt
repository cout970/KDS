package kds.api.model

import kds.api.KDS
import kds.api.item.DisplayModel

@KDS
class BlockstateModelBuilder {
    val variants = mutableMapOf<String, MutableList<BlockstateVariantBuilder>>()

    fun variant(name: String, func: BlockstateVariantBuilder.() -> Unit) {
        val list = variants.getOrPut(name) { mutableListOf() }

        list += BlockstateVariantBuilder().apply(func)
    }
}

@KDS
class BlockstateVariantBuilder {
    var display: DisplayModel? = null
    var rotation: Transformation? = null
    var uvLock: Boolean = false
}