package scripts

import kds.api.API
import kds.api.IModReference
import kds.api.registries.block
import kds.api.registries.item

val ref: IModReference = API.get("reference")

object Storage {
    val MAGIC_DIRT = ref.id("magic_dirt").block()!!
    val MAGIC_SAND = ref.id("magic_sand").block()!!
    val MAGIC_SAND2 = ref.id("magic_sand2").block()!!
    val STICK = ref.id("stick").item()!!
    val STICK2 = ref.id("stick2").item()!!
}
