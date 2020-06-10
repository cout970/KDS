package kds.internal.client

import com.google.gson.Gson
import kds.api.item.BlockCube
import kds.api.item.CustomDisplay
import kds.api.item.ItemDisplay
import kds.api.item.ItemSprite
import kds.api.model.JsonModel
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier

object ModelManager {
    private val GSON = Gson()
    private val customModels = mutableMapOf<Identifier, JsonUnbakedModel>()
    private val customBlockstateModels = mutableMapOf<Identifier, (ModelIdentifier, BlockState) -> UnbakedModel?>()
    private val customTexturePaths = mutableMapOf<Identifier, Identifier>()

    fun getCustomModel(id: Identifier): JsonUnbakedModel? {
//        if (id.namespace == "example_mod") {
//            println("here")
//        }
        return customModels[id]
    }

    fun getBlockstateConfig(id: Identifier): ((ModelIdentifier, BlockState) -> UnbakedModel?)? {
//        if (id.namespace == "example_mod") {
//            println("here")
//        }
        return customBlockstateModels[id]
    }

    fun getCustomTexturePath(id: Identifier): Identifier? = customTexturePaths[id]

    fun removeItemModel(id: Identifier) = customModels.remove(id)

    fun registerTexturePath(namespace: String, path: String) {
        val id = Identifier(namespace, path)
        customTexturePaths[id] = Identifier(namespace, "$path.png")
    }

    fun registerItemSprite(id: Identifier, path: String, item: Boolean): Identifier {

        val jsonModel = JsonModel()
        jsonModel.parent = "minecraft:item/handheld"
        jsonModel.textures["layer0"] = "${id.namespace}:${path}"

        val model = JsonUnbakedModel.deserialize(GSON.toJson(jsonModel))
        model.id = id.toString()

        registerTexturePath(id.namespace, path)

        val modelPath = if (item)
            Identifier(id.namespace, "item/${id.path}")
        else
            Identifier(id.namespace, "blocks/${id.path}")

        customModels[modelPath] = model
        return modelPath
    }

    fun registerBlockCube(id: Identifier, blockCube: BlockCube, item: Boolean): Identifier {

        val jsonModel = JsonModel()
        jsonModel.parent = "minecraft:block/cube"
        jsonModel.textures["particle"] = "${id.namespace}:${blockCube.particle}"
        jsonModel.textures["down"] = "${id.namespace}:${blockCube.down}"
        jsonModel.textures["up"] = "${id.namespace}:${blockCube.up}"
        jsonModel.textures["north"] = "${id.namespace}:${blockCube.north}"
        jsonModel.textures["east"] = "${id.namespace}:${blockCube.east}"
        jsonModel.textures["south"] = "${id.namespace}:${blockCube.south}"
        jsonModel.textures["west"] = "${id.namespace}:${blockCube.west}"

        val model = JsonUnbakedModel.deserialize(GSON.toJson(jsonModel))
        model.id = id.toString()

        registerTexturePath(id.namespace, blockCube.particle)
        registerTexturePath(id.namespace, blockCube.down)
        registerTexturePath(id.namespace, blockCube.up)
        registerTexturePath(id.namespace, blockCube.north)
        registerTexturePath(id.namespace, blockCube.east)
        registerTexturePath(id.namespace, blockCube.south)
        registerTexturePath(id.namespace, blockCube.west)

        val modelPath = if (item)
            Identifier(id.namespace, "item/${id.path}")
        else
            Identifier(id.namespace, "blocks/${id.path}")

        customModels[modelPath] = model
        return modelPath
    }

    fun registerCustomModel(id: Identifier, jsonModel: JsonModel, item: Boolean): Identifier {

        val model = JsonUnbakedModel.deserialize(GSON.toJson(jsonModel))
        model.id = id.toString()

        jsonModel.textures.values.forEach { path ->
            registerTexturePath(id.namespace, Identifier(path).path)
        }

        val modelPath = if (item)
            Identifier(id.namespace, "item/${id.path}")
        else
            Identifier(id.namespace, "blocks/${id.path}")

        customModels[modelPath] = model
        return modelPath
    }

    fun registerDisplay(id: Identifier, display: ItemDisplay?, item: Boolean): Identifier? {
        return when (display) {
            is ItemSprite -> registerItemSprite(id, display.path, item)
            is BlockCube -> registerBlockCube(id, display, item)
            is CustomDisplay -> registerCustomModel(id, display.model, item)
            null -> {
                removeItemModel(id)
                null
            }
        }
    }

    fun registerBlockstate(id: Identifier, getter: (ModelIdentifier, BlockState) -> UnbakedModel?) {
        val blockstateJson = Identifier(id.namespace, "blockstates/${id.path}.json")
        customBlockstateModels[blockstateJson] = getter
    }
}