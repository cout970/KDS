package kds.internal

import kds.api.IModReference
import kds.api.block.BlockDSL
import kds.api.item.ItemDSL
import kds.internal.block.KDSBlockDSL
import kds.internal.item.KDSItemDSL
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ModReference(
    override val modid: String,
    override var name: String,
    override var description: String
) : IModReference {

    private val logger = LogManager.getLogger(modid)

    override fun blocks(dsl: BlockDSL.() -> Unit) {
        try {
            KDSBlockDSL(this).apply(dsl)
        } catch (e: Exception) {
            logger.error("Exception in block definition", e)
            throw e
        }
    }

    override fun items(dsl: ItemDSL.() -> Unit) {
        try {
            KDSItemDSL(this).apply(dsl)
        } catch (e: Exception) {
            logger.error("Exception in item definition", e)
            throw e
        }
    }

    override fun id(path: String): Identifier = Identifier(modid, path)

    override fun logger(): Logger = logger
}