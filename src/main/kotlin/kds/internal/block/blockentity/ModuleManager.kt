package kds.internal.block.blockentity

import kds.api.block.blockentity.IModuleManager
import kds.api.block.blockentity.Module
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class ModuleManager(val blockEntity: BlockEntity) :
    IModuleManager {
    override val modules =
        mutableMapOf<Identifier, Module>()

    override val blockstate: BlockState
        get() = blockEntity.cachedState

    override var removed: Boolean
        get() = blockEntity.isRemoved
        set(value) {
            if (value) blockEntity.markRemoved() else blockEntity.cancelRemoval()
        }

    override fun sendUpdateToNearPlayers() {
        val world = blockEntity.world ?: return
        val pos = blockEntity.pos ?: return
        if (world.isClient) return

        blockEntity.toUpdatePacket()?.let { packet->
            world.players
                .map { it as ServerPlayerEntity }
                .filter { it.squaredDistanceTo(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()) < (64 * 64) }
                .forEach { it.networkHandler.sendPacket(packet) }
        }
    }

    override fun markDirty() = blockEntity.markDirty()
}