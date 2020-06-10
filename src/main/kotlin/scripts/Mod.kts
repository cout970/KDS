import kds.api.API

val reference = API.mod {
    modid = "example_mod"
    name = "Example mod"
    description = "A simple mod showing the features of the API"
}


API.include("Blocks", "reference" to reference)
API.include("Items", "reference" to reference)

