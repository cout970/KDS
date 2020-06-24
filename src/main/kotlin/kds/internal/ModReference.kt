package kds.internal

import kds.api.IModReference
import kds.api.block.IBlockDSL
import kds.api.item.IItemDSL
import kds.internal.block.BlockDSL
import kds.internal.item.ItemDSL
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ModReference(
    override val modid: String,
    override var name: String,
    override var description: String
) : IModReference {

    private val logger = LogManager.getLogger(modid)

    override fun blocks(dsl: IBlockDSL.() -> Unit) {
        try {
            BlockDSL(this).apply(dsl)
        } catch (e: Exception) {
            logger.error("Exception in block definition", e)
            throw e
        }
    }

    override fun items(dsl: IItemDSL.() -> Unit) {
        try {
            ItemDSL(this).apply(dsl)
        } catch (e: Exception) {
            logger.error("Exception in item definition", e)
            throw e
        }
    }

    override fun id(path: String): Identifier = Identifier(modid, path)

    override fun logger(): Logger = logger
}