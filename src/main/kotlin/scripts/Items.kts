import kds.api.API
import kds.api.IModReference
import kds.api.item.ItemSprite

val ref: IModReference = API.get("reference")

ref.items {
    item {
        name = "stick"
        defaultLocalizedName = "Stick"
        display = ItemSprite("items/stick")
    }
    item {
        name = "stick2"
        defaultLocalizedName = "Stick 2"
        display = ItemSprite("items/stick")
    }
}