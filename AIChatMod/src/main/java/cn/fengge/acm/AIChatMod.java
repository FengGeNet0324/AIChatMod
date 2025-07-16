// 修复KeyMapping初始化问题
public AIChatMod() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigManager.CLIENT_CONFIG);
    
    // 直接初始化KeyMapping
    configKey = new KeyMapping("key.aichatmod.config", GLFW.GLFW_KEY_O, "key.categories.aichatmod");
    
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeys);
    MinecraftForge.EVENT_BUS.register(this);
}

// 删除不再需要的clientSetup方法中的KeyMapping初始化
private void clientSetup(final FMLClientSetupEvent event) {
    // KeyMapping已提前初始化
}