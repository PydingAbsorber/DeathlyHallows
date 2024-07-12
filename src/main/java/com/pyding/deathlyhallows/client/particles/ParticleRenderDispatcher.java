package com.pyding.deathlyhallows.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.profiler.Profiler;

public final class ParticleRenderDispatcher {

    public static void dispatch() {
        Tessellator tessellator = Tessellator.instance;

        Profiler profiler = Minecraft.getMinecraft().mcProfiler;

        profiler.startSection("Generic");
        GenericBlock.dispatchQueuedRenders(tessellator);
        profiler.endSection();
    }
}