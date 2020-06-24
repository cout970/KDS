package kds.api.block.blockentity

import kds.api.KDS
import net.minecraft.block.BlockState
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class ModuleCtx(val world: World, val pos: BlockPos, val moduleManager: IModuleManager)

interface IModuleManager {
    val modules: Map<Identifier, Module>
    val blockstate: BlockState
    var removed: Boolean

    fun sendUpdateToNearPlayers()
    fun markDirty()
}

class Module(val id: Identifier) {
    var persistentState: Any? = null
    var syncState: Any? = null
    var temporaryState: Any? = null

    fun <T> persistentState(): T = persistentState as T
    fun <T> temporaryState(): T = temporaryState as T
    fun <T> syncState(): T = syncState as T
}

@KDS
open class ModuleBuilder {
    var id: Identifier? = null
    var onTick: (ModuleCtx.(Module) -> Unit)? = null
    var onInit: (ModuleCtx.(Module) -> Unit)? = null
    var onReset: (ModuleCtx.(Module) -> Unit)? = null
}