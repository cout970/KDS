package scripts.blocks

import kds.api.API
import kds.api.IModReference
import kds.api.item.BlockCubeModel
import kds.api.model.Transformation.Companion.rotation0
import kds.api.model.Transformation.Companion.rotation180Y
import kds.api.model.Transformation.Companion.rotation270Y
import kds.api.model.Transformation.Companion.rotation90Y
import kds.api.util.id

val ref: IModReference = API.get("reference")

ref.blocks {
    block {
        name = "furnace"
        material = id("minecraft", "stone")

        val cube = BlockCubeModel(
            up = "minecraft:textures/block/furnace_top",
            down = "minecraft:textures/block/furnace_side",
            north = "minecraft:textures/block/furnace_front",
            south = "minecraft:textures/block/furnace_side",
            east = "minecraft:textures/block/furnace_side",
            west = "minecraft:textures/block/furnace_side",
            particle = "minecraft:textures/block/furnace_side"
        )

        item {
            defaultLocalizedName = "Example Furnace"
            display = cube
        }

        blockState {
            properties {
                horizontalFacing()
            }

            model {
                variant("facing=north") {
                    display = cube
                    rotation = rotation90Y()
                }
                variant("facing=south") {
                    display = cube
                    rotation = rotation270Y()
                }
                variant("facing=east") {
                    display = cube
                    rotation = rotation180Y()
                }
                variant("facing=west") {
                    display = cube
                    rotation = rotation0()
                }
            }
        }
    }
}
