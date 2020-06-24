import kds.api.API

val ref = API.mod {
    modid = "example_mod"
    name = "Example mod"
    description = "A simple mod showing the features of the API"
}

API.include("scripts/Blocks", "reference" to ref)
API.include("scripts/Items", "reference" to ref)
API.include("scripts/Storage", "reference" to ref)

