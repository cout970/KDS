package kds.internal.block.blockentity

import kds.api.block.BlockEntityBuilder
import kds.api.block.blockentity.Module
import kds.api.block.blockentity.ModuleCtx
import kds.api.util.NBTSerialization
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.Tickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

open class KDSBlockEntity(val config: BlockEntityBuilder) : BlockEntity(type(config)) {
    /**
     * Be careful extending this class, moduleManager may not be fully configured in the subclass constructor
     */
    @Suppress("LeakingThis")
    val moduleManager = ModuleManager(this)

    val ctx: ModuleCtx
        get() = ModuleCtx(world!!, pos, moduleManager)

    fun init() {
        if (!hasWorld()) return

        config.modules.forEach { (id, dsl) ->
            val module = Module(id)
            dsl.onInit?.invoke(ctx, module)
            moduleManager.modules[id] = module
        }
    }

    fun reset() {
        config.modules.forEach { (id, dsl) ->
            val module = moduleManager.modules[id] ?: return@forEach
            dsl.onReset?.invoke(ctx, module)
        }
    }

    override fun setLocation(world: World?, pos: BlockPos?) {
        val initialized = hasWorld()
        super.setLocation(world, pos)
        if (!initialized) init() else reset()
    }

    override fun getSquaredRenderDistance(): Double {
        return config.renderDistance * config.renderDistance
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        modules { id, module ->
            if (module.persistentState != null) {
                val value = NBTSerialization.serialize(module.persistentState!!)
                tag.put(id.toString(), value)
            }
        }
        return super.toTag(tag)
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
        modules { id, module ->
            if (tag.contains(id.toString())) {
                module.persistentState = NBTSerialization.deserialize(tag.getCompound(id.toString()))
            }
        }
    }

    inline fun modules(func: (id: Identifier, module: Module) -> Unit) {
        moduleManager.modules.forEach { (id, module) ->
            func(id, module)
        }
    }
}

open class KDSTickableBlockEntity(config: BlockEntityBuilder) : KDSBlockEntity(config), Tickable {

    override fun tick() {
        config.modules.forEach { (id, dsl) ->
            dsl.onTick?.invoke(ctx, moduleManager.modules[id]!!)
        }
    }
}

private fun type(config: BlockEntityBuilder): BlockEntityType<*> {
    val id = config.type!!
    return Registry.BLOCK_ENTITY_TYPE[id] ?: error("Block entity ${config.type} is not registered")
}