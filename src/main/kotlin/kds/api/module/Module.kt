package kds.api.module

import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class ModuleCtx(val world: World, val pos: BlockPos)

class Module<PersistentState : Any, TempState : Any> {
    var name: Identifier? = null
    lateinit var initPersistentState: (ModuleCtx) -> PersistentState
    lateinit var initTempState: (ModuleCtx) -> TempState

    var tick: ((ModuleCtx, PersistentState, TempState) -> Unit)? = null
}