package net.fabricmc.example.mixin;

import com.google.common.collect.ImmutableList;
import kds.internal.client.ModelManager;
import kotlin.jvm.functions.Function2;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Shadow
    Map<Identifier, UnbakedModel> unbakedModels;

    @Shadow
    Set<Identifier> modelsToLoad;

    @Inject(
            at = @At("HEAD"),
            method = "loadModelFromJson(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;",
            cancellable = true
    )
    private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> info) {
        JsonUnbakedModel result = ModelManager.INSTANCE.getCustomModel(id);
        if (result != null) {
            info.setReturnValue(result);
            info.cancel();
        }
    }

    @Inject(
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/client/render/model/ModelLoader$ModelDefinition",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            method = "loadModel(Lnet/minecraft/util/Identifier;)V",
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void loadModel(Identifier id, CallbackInfo ci, Identifier identifier2, StateManager stateManager, List properties, ImmutableList immutableList, Map<ModelIdentifier, BlockState> modelIdToBlockstate, Map blockstateToUnbaked, Identifier blockstateJson, UnbakedModel defaultModel) {
        Function2<ModelIdentifier, BlockState, UnbakedModel> result = ModelManager.INSTANCE.getBlockstateConfig(blockstateJson);

        if (result != null) {
            ci.cancel();

            for (Map.Entry<ModelIdentifier, BlockState> entry : modelIdToBlockstate.entrySet()) {
                ModelIdentifier modelId = entry.getKey();

                // ModelId + Blockstate => UnbakedModel
                try {
                    UnbakedModel model = result.invoke(modelId, entry.getValue());

                    if (model == null) {
                        model = defaultModel;
                    }

                    unbakedModels.put(id, model);
                    modelsToLoad.addAll(model.getModelDependencies());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
