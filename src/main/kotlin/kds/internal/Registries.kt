package kds.internal

import com.mojang.serialization.Codec
import kds.api.registries.IRegistries
import kds.api.registries.IRegistry
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
import net.minecraft.util.registry.Registry
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

internal object Registries : IRegistries {
    override val soundEvent: IRegistry<SoundEvent> = RegistryWrapper(Registry.SOUND_EVENT)
    override val fluid: IRegistry<Fluid> = RegistryWrapper(Registry.FLUID)
    override val statusEffect: IRegistry<StatusEffect> = RegistryWrapper(Registry.STATUS_EFFECT)
    override val block: IRegistry<Block> = RegistryWrapper(Registry.BLOCK)
    override val enchantment: IRegistry<Enchantment> = RegistryWrapper(Registry.ENCHANTMENT)
    override val entityType: IRegistry<EntityType<*>> = RegistryWrapper(Registry.ENTITY_TYPE)
    override val item: IRegistry<Item> = RegistryWrapper(Registry.ITEM)
    override val potion: IRegistry<Potion> = RegistryWrapper(Registry.POTION)
    override val carver: IRegistry<Carver<*>> = RegistryWrapper(Registry.CARVER)
    override val surfaceBuilder: IRegistry<SurfaceBuilder<*>> = RegistryWrapper(Registry.SURFACE_BUILDER)
    override val feature: IRegistry<Feature<*>> = RegistryWrapper(Registry.FEATURE)
    override val decorator: IRegistry<Decorator<*>> = RegistryWrapper(Registry.DECORATOR)
    override val biome: IRegistry<Biome> = RegistryWrapper(Registry.BIOME)
    override val blockStateProviderType: IRegistry<BlockStateProviderType<*>> = RegistryWrapper(Registry.BLOCK_STATE_PROVIDER_TYPE)
    override val blockPlacerType: IRegistry<BlockPlacerType<*>> = RegistryWrapper(Registry.BLOCK_PLACER_TYPE)
    override val foliagePlacerType: IRegistry<FoliagePlacerType<*>> = RegistryWrapper(Registry.FOLIAGE_PLACER_TYPE)
    override val trunkPlacerType: IRegistry<TrunkPlacerType<*>> = RegistryWrapper(Registry.TRUNK_PLACER_TYPE)
    override val treeDecoratorType: IRegistry<TreeDecoratorType<*>> = RegistryWrapper(Registry.TREE_DECORATOR_TYPE)
    override val featureSizeType: IRegistry<FeatureSizeType<*>> = RegistryWrapper(Registry.FEATURE_SIZE_TYPE)
    override val particleType: IRegistry<ParticleType<*>> = RegistryWrapper(Registry.PARTICLE_TYPE)
    override val biomeSource: IRegistry<Codec<out BiomeSource>> = RegistryWrapper(Registry.BIOME_SOURCE)
    override val chunkGenerator: IRegistry<Codec<out ChunkGenerator>> = RegistryWrapper(Registry.CHUNK_GENERATOR)
    override val blockEntityType: IRegistry<BlockEntityType<*>> = RegistryWrapper(Registry.BLOCK_ENTITY_TYPE)
    override val paintingMotive: IRegistry<PaintingMotive> = RegistryWrapper(Registry.PAINTING_MOTIVE)
    override val customStat: IRegistry<Identifier> = RegistryWrapper(Registry.CUSTOM_STAT)
    override val chunkStatus: IRegistry<ChunkStatus> = RegistryWrapper(Registry.CHUNK_STATUS)
    override val structureFeature: IRegistry<StructureFeature<*>> = RegistryWrapper(Registry.STRUCTURE_FEATURE)
    override val structurePiece: IRegistry<StructurePieceType> = RegistryWrapper(Registry.STRUCTURE_PIECE)
    override val ruleTest: IRegistry<RuleTestType<*>> = RegistryWrapper(Registry.RULE_TEST)
    override val posRuleTest: IRegistry<PosRuleTestType<*>> = RegistryWrapper(Registry.POS_RULE_TEST)
    override val structureProcessor: IRegistry<StructureProcessorType<*>> = RegistryWrapper(Registry.STRUCTURE_PROCESSOR)
    override val structurePoolElement: IRegistry<StructurePoolElementType<*>> = RegistryWrapper(Registry.STRUCTURE_POOL_ELEMENT)
    override val screenHandler: IRegistry<ScreenHandlerType<*>> = RegistryWrapper(Registry.SCREEN_HANDLER)
    override val recipeType: IRegistry<RecipeType<*>> = RegistryWrapper(Registry.RECIPE_TYPE)
    override val recipeSerializer: IRegistry<RecipeSerializer<*>> = RegistryWrapper(Registry.RECIPE_SERIALIZER)
    override val attribute: IRegistry<EntityAttribute> = RegistryWrapper(Registry.ATTRIBUTE)
    override val statType: IRegistry<StatType<*>> = RegistryWrapper(Registry.STAT_TYPE)
    override val villagerType: IRegistry<VillagerType> = RegistryWrapper(Registry.VILLAGER_TYPE)
    override val villagerProfession: IRegistry<VillagerProfession> = RegistryWrapper(Registry.VILLAGER_PROFESSION)
    override val pointOfInterestType: IRegistry<PointOfInterestType> = RegistryWrapper(Registry.POINT_OF_INTEREST_TYPE)
    override val memoryModuleType: IRegistry<MemoryModuleType<*>> = RegistryWrapper(Registry.MEMORY_MODULE_TYPE)
    override val sensorType: IRegistry<SensorType<*>> = RegistryWrapper(Registry.SENSOR_TYPE)
    override val schedule: IRegistry<Schedule> = RegistryWrapper(Registry.SCHEDULE)
    override val activity: IRegistry<Activity> = RegistryWrapper(Registry.ACTIVITY)
    override val lootPoolEntryType: IRegistry<LootPoolEntryType> = RegistryWrapper(Registry.LOOT_POOL_ENTRY_TYPE)
    override val lootFunctionType: IRegistry<LootFunctionType> = RegistryWrapper(Registry.LOOT_FUNCTION_TYPE)
    override val lootConditionType: IRegistry<LootConditionType> = RegistryWrapper(Registry.LOOT_CONDITION_TYPE)
}

internal class RegistryWrapper<T>(private val reg: Registry<T>) : IRegistry<T> {
    override fun get(id: Identifier): T? = reg.get(id)
}