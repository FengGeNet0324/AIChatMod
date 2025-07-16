        new Thread(() -> {
            String response = ApiUtil.sendChatRequest(history);
            history.add("AI: " + response);

            Minecraft.getInstance().execute(() -> {
                // 使用统一的显示方式
                Minecraft.getInstance().player.displayClientMessage(
                    Component.literal(response),
                    false
                );
            });
        }).start();