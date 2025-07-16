// 修复配置保存逻辑和预置提示词处理
public class ConfigManager {
    ...
    
    // 修改保存方法，新增预置提示词参数
    public static void saveConfig(String modelName, String apiUrl, String apiKey, int timeout, 
                                 String chatDisplayName, String presetPrompt) { //
        if (apiUrl == null || apiKey == null || chatDisplayName == null || modelName == null || presetPrompt == null) { //
            LOGGER.error("配置值为空，保存失败");
            return;
        }

        ModConfig.MODEL_NAME.set(modelName.trim());
        ModConfig.API_URL.set(apiUrl.trim());
        ModConfig.API_KEY.set(apiKey.trim());
        ModConfig.TIMEOUT.set(timeout);
        ModConfig.CHAT_DISPLAY_NAME.set(chatDisplayName.trim());
        ModConfig.PRESET_PROMPT.set(presetPrompt.trim()); // 保存预置提示词

        try {
            ModConfig.SPEC.save();
            ModLoadingContext.get().rebuildConfig(Type.CLIENT); // 强制重建配置
            LOGGER.info("配置文件已成功保存并重载");
        } catch (Exception e) {
            LOGGER.error("配置保存失败", e);
            throw new RuntimeException("配置保存失败", e);
        }
    }
    
    // 新增获取预置提示词方法
    public static String getPresetPrompt() {
        return ModConfig.PRESET_PROMPT.get();
    }
}