package org.exmple.itemmodelenhanced.mixin.client;

import org.exmple.itemmodelenhanced.client.render.DiamondDropScaleState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;

@Mixin(ItemEntityRenderState.class)
public class ItemEntityRenderStateMixin implements DiamondDropScaleState {
    @Unique
    private float itemmodelenhanced$scale = 1.0f;

    @Override
    public float itemmodelenhanced$getScale() {
        return this.itemmodelenhanced$scale;
    }

    @Override
    public void itemmodelenhanced$setScale(float scale) {
        this.itemmodelenhanced$scale = scale;
    }
}


