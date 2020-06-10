import kds.api.API
import kds.api.IModReference
import kds.api.item.BlockCube
import kds.api.item.ItemSprite
import net.minecraft.util.StringIdentifiable

val ref: IModReference = API.get("reference")

ref.blocks {
    block {
        name = "magic_dirt"
        material = "minecraft:dirt"

        item {
            defaultLocalizedName = "Magic Dirt"
            display = BlockCube("blocks/magic_dirt")
        }

        blockstate {
            model {
                variant("") {
                    display = BlockCube("blocks/magic_dirt")
                }
            }
        }
    }

    block {
        name = "magic_sand"
        material = "minecraft:sand"

        item {
            defaultLocalizedName = "Magic Sand"
            display = BlockCube("blocks/magic_sand")
        }

        blockstate {
            model {
                variant("working=true") {
                    display = BlockCube("blocks/magic_sand")
                }
                variant("working=false") {
                    display = BlockCube("blocks/magic_sand")
                }
            }

            properties {
                boolean("working")
            }
        }
    }

    block {
        name = "magic_sand2"
        material = "minecraft:sand"

        item {
            defaultLocalizedName = "Magic Sand2"
            display = BlockCube("blocks/magic_sand")
        }

        blockstate {
            model {
                variant("option=a") {
                    display = BlockCube("blocks/magic_sand")
                }
                variant("option=b") {
                    display = BlockCube("blocks/magic_dirt")
                }
                variant("option=c") {
                    display = BlockCube("blocks/magic_sand")
                }
            }

            properties {
                enum("option", States::class)
            }
        }
    }
}

enum class States : StringIdentifiable {
    A, B, C;

    override fun asString(): String = name.toLowerCase()
}
