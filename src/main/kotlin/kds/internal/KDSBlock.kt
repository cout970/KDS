package kds.internal

import kds.api.block.BlockBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.StateManager
import net.minecraft.world.BlockView

class KDSBlock(val config: BlockBuilder, settings: Settings) : Block(settings), BlockEntityProvider {

    companion object {
        // Fuck you Mojang, again
        lateinit var _constructor_config_: BlockBuilder
    }

// TODO fuck you Mojang x3
//    override fun hasBlockEntity(): Boolean {
//        return config.blockEntityConfig != null
//    }

    override fun createBlockEntity(view: BlockView?): BlockEntity? {
        return null
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        val list = _constructor_config_.blockStateConfig?.properties ?: return

        list.forEach { builder.add(it) }
    }
}