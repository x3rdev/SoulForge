package com.github.x3rdev.soul_forge.common.scheduler;


import com.github.x3rdev.soul_forge.SoulForge;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = SoulForge.MOD_ID)
public final class ServerScheduler {

    private static final ConcurrentMap<Integer, List<Runnable>> SERVER_SCHEDULE = new ConcurrentHashMap<>();

    private ServerScheduler() {

    }

    public static void schedule(Runnable task, int delay) {
        SERVER_SCHEDULE.compute(ServerLifecycleHooks.getCurrentServer().getTickCount() + delay,
            (integer, runnables) -> {
                if(runnables == null) {
                    runnables = new ObjectArrayList<>();
                }
                runnables.add(task);
                return runnables;
            }
        );
    }

    @SubscribeEvent
    private static void serverTick(ServerTickEvent.Post event) {
        int ticks = ServerLifecycleHooks.getCurrentServer().getTickCount();
        List<Runnable> tasks = SERVER_SCHEDULE.get(ticks);
        if(tasks != null) {
            tasks.forEach(Runnable::run);
            SERVER_SCHEDULE.remove(ticks);
        }
    }
}