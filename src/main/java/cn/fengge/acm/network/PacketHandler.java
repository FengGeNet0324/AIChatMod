package cn.fengge.acm.network;

import cn.fengge.acm.AIChatMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;
    public static void register() {
        // 使用双参数构造函数
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(AIChatMod.MODID, "main"),
                () -> "1.0",
                s -> true,
                s -> true
            );
    }
}
