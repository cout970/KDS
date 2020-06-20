package kds.api.registries

import com.mojang.serialization.Codec
import kds.api.API
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.Schedule
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.decoration.painting.PaintingMotive
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.entry.LootPoolEntryType
import net.minecraft.loot.function.LootFunctionType
import net.minecraft.particle.ParticleType
import net.minecraft.potion.Potion
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.sound.SoundEvent
import net.minecraft.stat.StatType
import net.minecraft.structure.StructurePieceType
import net.minecraft.structure.pool.StructurePoolElementType
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.structure.rule.PosRuleTestType
import net.minecraft.structure.rule.RuleTestType
import net.minecraft.util.Identifier
import net.minecraft.village.VillagerProfession
import net.minecraft.village.VillagerType
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.chunk.ChunkStatus
import net.minecraft.world.gen.carver.Carver
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.decorator.Decorator
import net.minecraft.world.gen.decorator.TreeDecoratorType
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.size.FeatureSizeType
import net.minecraft.world.gen.foliage.FoliagePlacerType
import net.minecraft.world.gen.placer.BlockPlacerType
import net.minecraft.world.gen.stateprovider.BlockStateProviderType
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder
import net.minecraft.world.gen.trunk.TrunkPlacerType
import net.minecraft.world.poi.PointOfInterestType

/**
 * List of registries to be accessed by scripts
 */
interface IRegistries {
    val soundEvent: IRegistry<SoundEvent>
    val fluid: IRegistry<Fluid>
    val statusEffect: IRegistry<StatusEffect>
    val block: IRegistry<Block>
    val enchantment: IRegistry<Enchantment>
    val entityType: IRegistry<EntityType<*>>
    val item: IRegistry<Item>
    val potion: IRegistry<Potion>
    val carver: IRegistry<Carver<*>>
    val surfaceBuilder: IRegistry<SurfaceBuilder<*>>
    val feature: IRegistry<Feature<*>>
    val decorator: IRegistry<Decorator<*>>
    val biome: IRegistry<Biome>
    val blockStateProviderType: IRegistry<BlockStateProviderType<*>>
    val blockPlacerType: IRegistry<BlockPlacerType<*>>
    val foliagePlacerType: IRegistry<FoliagePlacerType<*>>
    val trunkPlacerType: IRegistry<TrunkPlacerType<*>>
    val treeDecoratorType: IRegistry<TreeDecoratorType<*>>
    val featureSizeType: IRegistry<FeatureSizeType<*>>
    val particleType: IRegistry<ParticleType<*>>
    val biomeSource: IRegistry<Codec<out BiomeSource>>
    val chunkGenerator: IRegistry<Codec<out ChunkGenerator>>
    val blockEntityType: IRegistry<BlockEntityType<*>>
    val paintingMotive: IRegistry<PaintingMotive>
    val customStat: IRegistry<Identifier>
    val chunkStatus: IRegistry<ChunkStatus>
    val structureFeature: IRegistry<StructureFeature<*>>
    val structurePiece: IRegistry<StructurePieceType>
    val ruleTest: IRegistry<RuleTestType<*>>
    val posRuleTest: IRegistry<PosRuleTestType<*>>
    val structureProcessor: IRegistry<StructureProcessorType<*>>
    val structurePoolElement: IRegistry<StructurePoolElementType<*>>
    val screenHandler: IRegistry<ScreenHandlerType<*>>
    val recipeType: IRegistry<RecipeType<*>>
    val recipeSerializer: IRegistry<RecipeSerializer<*>>
    val attribute: IRegistry<EntityAttribute>
    val statType: IRegistry<StatType<*>>
    val villagerType: IRegistry<VillagerType>
    val villagerProfession: IRegistry<VillagerProfession>
    val pointOfInterestType: IRegistry<PointOfInterestType>
    val memoryModuleType: IRegistry<MemoryModuleType<*>>
    val sensorType: IRegistry<SensorType<*>>
    val schedule: IRegistry<Schedule>
    val activity: IRegistry<Activity>
    val lootPoolEntryType: IRegistry<LootPoolEntryType>
    val lootFunctionType: IRegistry<LootFunctionType>
    val lootConditionType: IRegistry<LootConditionType>
}


interface IRegistry<T> {

    /**
     * Get a value from the registry by its id
     */
    operator fun get(id: Identifier): T?
}

/**
 * Easy registry accessors
 */

fun Identifier.soundEvent() = API.registries().soundEvent[this]
fun Identifier.fluid() = API.registries().fluid[this]
fun Identifier.statusEffect() = API.registries().statusEffect[this]
fun Identifier.block() = API.registries().block[this]
fun Identifier.enchantment() = API.registries().enchantment[this]
fun Identifier.entityType() = API.registries().entityType[this]
fun Identifier.item() = API.registries().item[this]
fun Identifier.potion() = API.registries().potion[this]
fun Identifier.carver() = API.registries().carver[this]
fun Identifier.surfaceBuilder() = API.registries().surfaceBuilder[this]
fun Identifier.feature() = API.registries().feature[this]
fun Identifier.decorator() = API.registries().decorator[this]
fun Identifier.biome() = API.registries().biome[this]
fun Identifier.blockStateProviderType() = API.registries().blockStateProviderType[this]
fun Identifier.blockPlacerType() = API.registries().blockPlacerType[this]
fun Identifier.foliagePlacerType() = API.registries().foliagePlacerType[this]
fun Identifier.trunkPlacerType() = API.registries().trunkPlacerType[this]
fun Identifier.treeDecoratorType() = API.registries().treeDecoratorType[this]
fun Identifier.featureSizeType() = API.registries().featureSizeType[this]
fun Identifier.particleType() = API.registries().particleType[this]
fun Identifier.biomeSource() = API.registries().biomeSource[this]
fun Identifier.chunkGenerator() = API.registries().chunkGenerator[this]
fun Identifier.blockEntityType() = API.registries().blockEntityType[this]
fun Identifier.paintingMotive() = API.registries().paintingMotive[this]
fun Identifier.customStat() = API.registries().customStat[this]
fun Identifier.chunkStatus() = API.registries().chunkStatus[this]
fun Identifier.structureFeature() = API.registries().structureFeature[this]
fun Identifier.structurePiece() = API.registries().structurePiece[this]
fun Identifier.ruleTest() = API.registries().ruleTest[this]
fun Identifier.posRuleTest() = API.registries().posRuleTest[this]
fun Identifier.structureProcessor() = API.registries().structureProcessor[this]
fun Identifier.structurePoolElement() = API.registries().structurePoolElement[this]
fun Identifier.screenHandler() = API.registries().screenHandler[this]
fun Identifier.recipeType() = API.registries().recipeType[this]
fun Identifier.recipeSerializer() = API.registries().recipeSerializer[this]
fun Identifier.attribute() = API.registries().attribute[this]
fun Identifier.statType() = API.registries().statType[this]
fun Identifier.villagerType() = API.registries().villagerType[this]
fun Identifier.villagerProfession() = API.registries().villagerProfession[this]
fun Identifier.pointOfInterestType() = API.registries().pointOfInterestType[this]
fun Identifier.memoryModuleType() = API.registries().memoryModuleType[this]
fun Identifier.sensorType() = API.registries().sensorType[this]
fun Identifier.schedule() = API.registries().schedule[this]
fun Identifier.activity() = API.registries().activity[this]
fun Identifier.lootPoolEntryType() = API.registries().lootPoolEntryType[this]
fun Identifier.lootFunctionType() = API.registries().lootFunctionType[this]
fun Identifier.lootConditionType() = API.registries().lootConditionType[this]