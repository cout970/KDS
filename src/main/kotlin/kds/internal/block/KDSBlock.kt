package kds.internal.block

import kds.api.block.BlockBuilder
import kds.internal.block.blockentity.KDSBlockEntity
import kds.internal.block.blockentity.KDSTickableBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.StateManager
import net.minecraft.world.BlockView

open class KDSBlock(val config: BlockBuilder, settings: Settings) : Block(settings) {

    companion object {
        // Fuck you Mojang, stop using bugs to design your code
        lateinit var _constructor_config_: BlockBuilder
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        val list = _constructor_config_.blockStateConfig?.properties ?: return

        list.forEach { builder.add(it) }
    }
}

class KDSBlockWithEntity(config: BlockBuilder, settings: Settings) : KDSBlock(config, settings), BlockEntityProvider {
    override fun createBlockEntity(world: BlockView?): BlockEntity? {
        val config = config.blockEntityConfig!!
        val tickable = config.modules.any { it.value.onTick != null }

        return if (tickable) KDSTickableBlockEntity(config) else KDSBlockEntity(config)
    }
}