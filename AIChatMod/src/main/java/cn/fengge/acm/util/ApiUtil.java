            if (response.statusCode() == 200) {
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                // 增强解析逻辑，仅提取核心内容
                if (jsonResponse.has("choices") && 
                    jsonResponse.get("choices").isJsonArray()) {
                    JsonArray choices = jsonResponse.getAsJsonArray("choices");
                    if (!choices.isEmpty()) {
                        JsonElement firstChoice = choices.get(0);
                        if (firstChoice.isJsonObject() && 
                            firstChoice.getAsJsonObject().has("message")) {
                            JsonObject message = firstChoice.getAsJsonObject()
                                .getAsJsonObject("message");
                            if (message.has("content") && 
                                message.get("content").isJsonPrimitive()) {
                                return message.get("content").getAsString();
                            }
                        }
                    }
                }
                return "Error: Invalid response structure";
            }