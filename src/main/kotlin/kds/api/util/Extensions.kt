package kds.api.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.property.Property

fun <T: Comparable<T>> Block.findProperty(name: String): Property<T>? {
    return defaultState?.properties?.find { it.getName() == name } as? Property<T>?
}

fun <T: Comparable<T>> BlockState.findProperty(name: String): Property<T>? {
    return properties.find { it.getName() == name } as? Property<T>?
}