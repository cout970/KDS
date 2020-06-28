import example_mod.ExampleMod
import kds.api.API
import kds.api.Scripting

val ref = API.mod {
    modid = "example_mod"
    name = "Example mod"
    description = "A simple mod showing the features of the API"
}

ExampleMod.ref = ref

Scripting.include("example_mod/scripts/Blocks", "reference" to ref)
Scripting.include("example_mod/scripts/Items", "reference" to ref)
Scripting.include("example_mod/scripts/Storage", "reference" to ref)

