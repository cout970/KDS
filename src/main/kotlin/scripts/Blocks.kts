import kds.api.API
import kds.api.IModReference
import kds.api.block.BlockEntityBuilder
import kds.api.item.BlockCubeModel
import kds.api.util.findProperty
import kds.api.util.id
import net.minecraft.util.StringIdentifiable
import java.io.Serializable

val ref: IModReference = API.get("reference")

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

fun BlockEntityBuilder.customModule() {
    data class Counter(var count: Int = 0) : Serializable

    module {
        id = ref.id("custom_module")
        onInit = { mod ->
            mod.persistentState = Counter()
        }
        onTick = { module ->
            val enumProperty = moduleManager.blockstate.findProperty<States>("option")!!
            val counter = module.persistentState<Counter>()

            if (counter.count % 20 == 0) {
                world.setBlockState(pos, moduleManager.blockstate.cycle(enumProperty))
            }
            counter.count++
        }
    }
}

enum class States : StringIdentifiable {
    A, B, C;

    override fun asString(): String = name.toLowerCase()
}

API.include("scripts/blocks/Furnace", "reference" to ref)
