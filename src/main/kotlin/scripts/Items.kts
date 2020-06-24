import kds.api.API
import kds.api.IModReference
import kds.api.item.ItemSpriteModel

val ref: IModReference = API.get("reference")

ref.items {
    item {
        name = "stick"
        defaultLocalizedName = "Stick"
        display = ItemSpriteModel("items/stick")
    }
    item {
        name = "stick2"
        defaultLocalizedName = "Stick 2"
        display = ItemSpriteModel("items/stick")
    }
}