import example_mod.States
import example_mod.customModule
import kds.api.IModReference
import kds.api.Scripting
import kds.api.item.BlockCubeModel
import kds.api.util.id

val ref: IModReference = Scripting.get("reference")

ref.blocks {
    block {
        name = "magic_dirt"
        material = id("minecraft", "dirt")

        item {
            defaultLocalizedName = "Magic Dirt"
            display = BlockCubeModel("blocks/magic_dirt")
        }

        blockState {
            model {
                variant("") {
                    display = BlockCubeModel("blocks/magic_dirt")
                }
            }
        }
    }

    block {
        name = "magic_sand"
        material = id("minecraft", "sand")

        item {
            defaultLocalizedName = "Magic Sand"
            display = BlockCubeModel("blocks/magic_sand")
        }

        blockState {
            model {
                variant("working=true") {
                    display = BlockCubeModel("blocks/magic_sand")
                }
                variant("working=false") {
                    display = BlockCubeModel("blocks/magic_sand")
                }
            }

            properties {
                boolean("working")
            }
        }
    }

    block {
        name = "magic_sand2"
        material = id("minecraft", "sand")

        item {
            defaultLocalizedName = "Magic Sand2"
            display = BlockCubeModel("blocks/magic_sand")
        }

        blockState {
            model {
                variant("option=a") {
                    display = BlockCubeModel("blocks/magic_sand")
                }
                variant("option=b") {
                    display = BlockCubeModel("blocks/magic_dirt")
                }
                variant("option=c") {
                    display = BlockCubeModel("blocks/magic_sand")
                }
            }

            properties {
                enum("option", States::class)
            }
        }

        blockEntity {
            customModule()
        }
    }
}

Scripting.include("example_mod/scripts/blocks/Furnace", "reference" to ref)
