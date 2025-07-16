package cn.fengge.acm;

import cn.fengge.acm.command.CommandACM;
import cn.fengge.acm.config.ConfigManager;
import cn.fengge.acm.gui.ConfigScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod("aichatmod")
public class AIChatMod {
    public static final String MODID = "aichatmod";
    // 添加日志记录器
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    // 修复：在声明时直接初始化KeyMapping
    public static final KeyMapping configKey = new KeyMapping("key.aichatmod.config", GLFW.GLFW_KEY_O, "key.categories.aichatmod");
    private static boolean aiChatEnabled = false;

    public AIChatMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigManager.CLIENT_CONFIG);
        
        // 修复：移除冗余的clientSetup事件监听
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeys);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(configKey);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && configKey.isDown()) { // 修复：使用isDown()替代consumeClick()
            Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen)); // 修复：直接打开配置界面
        }
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        CommandACM.register(event.getDispatcher());
    }

    // 新增方法：启用AI聊天功能
    public static void enableAIChat() {
        aiChatEnabled = true;
        LOGGER.info("AI聊天：开启"); // 添加日志输出
    }

    // 新增方法：禁用AI聊天功能
    public static void disableAIChat() {
        aiChatEnabled = false;
        LOGGER.info("AI聊天：关闭"); // 添加日志输出
    }

    // 新增方法：检查AI聊天是否启用
    public static boolean isAiChatEnabled() {
        return aiChatEnabled;
    }
}