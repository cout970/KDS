package kds.api.model

import kds.api.item.ItemDisplay
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Property
import net.minecraft.util.StringIdentifiable
import kotlin.reflect.KClass

class BlockStateBuilder {
    var customModel: ((ModelIdentifier, BlockState) -> UnbakedModel?)? = null
    var modelVariants: (Map<String, MutableList<BlockstateVariantBuilder>>)? = null
    val properties = mutableSetOf<Property<*>>()

    fun model(func: BlockstateModelBuilder.() -> Unit) {
        modelVariants = BlockstateModelBuilder().apply(func).variants
    }

    fun properties(func: PropertyBuilder.() -> Unit) {
        PropertyBuilder(properties).apply(func)
    }
}

class PropertyBuilder(val properties: MutableSet<Property<*>>) {

    fun boolean(name: String) {
        properties += BooleanProperty.of(name)
    }

    fun <T> enum(name: String, clazz: KClass<T>) where T : Enum<T>, T : StringIdentifiable {
        properties += EnumProperty.of(name, clazz.java)
    }
}

class BlockstateModelBuilder {
    val variants = mutableMapOf<String, MutableList<BlockstateVariantBuilder>>()

    fun variant(name: String, func: BlockstateVariantBuilder.() -> Unit) {
        val list = variants.getOrPut(name) { mutableListOf() }

        list += BlockstateVariantBuilder().apply(func)
    }
}

class BlockstateVariantBuilder {
    var display: ItemDisplay? = null
    var rotation: Transformation? = null
    var uvLock: Boolean = false
}