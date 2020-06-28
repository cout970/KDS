package example_mod.scripts.blocks

import kds.api.IModReference
import kds.api.Scripting
import kds.api.item.BlockCubeModel
import kds.api.model.Transformation
import kds.api.module.inventory
import kds.api.util.id
import kds.api.util.withProperty

val ref: IModReference = Scripting.get("reference")

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
                    rotation = Transformation.rotation90Y()
                }
                variant("facing=south") {
                    display = cube
                    rotation = Transformation.rotation270Y()
                }
                variant("facing=east") {
                    display = cube
                    rotation = Transformation.rotation180Y()
                }
                variant("facing=west") {
                    display = cube
                    rotation = Transformation.rotation0()
                }
            }
        }

        blockEntity {
            inventory { slots = 3 }
        }

        placementState = {
            result = block.defaultState.withProperty("facing", ctx.playerFacing.opposite)
        }
    }
}
