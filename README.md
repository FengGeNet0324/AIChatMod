为[EAS2] 伊甸空天2 Eden Aerospace 2 整合包制作
=======
[BiliBili视频](https://www.bilibili.com/video/BV1iD421L79A)
[MC百科介绍](https://www.mcmod.cn/modpack/1060.html)

![](https://i.mcmod.cn/modpack/cover/20250219/1739939320_7949_tziU.jpg@480x300.jpg)

本模组由AI代码工具辅助编写，毫无技术含量（确信）
-------
玩家可以在聊天框与AI对话，支持自定义模型

食用方法
=======

**主要指令：`/acm`**
| 子命令 | 用途 | 用法1 | 用法2 |
| ------------ | ------------ |  -----------  | ------------ |
|`help`|命令帮助|无|无|
|`context`|上下文参数|`enable/disable` 启用/禁用功能|`set [消息数量]` 设置长度|
|`prefix`|消息前缀检测(默认为'#')|`enable/disable` 启用/禁用功能(默认启用)|无|


### 配置文件
```javascript
["AIChatMod Config"]
	#API 请求地址
	#示例: https://api.deepseek.com/v1/chat/completions
	apiUrl = ""
	#APIKEY (请勿将此条目透露给他人)
	apiKey = ""
	#API请求超时时间(单位：秒,范围5~120)
	#Range: 5 ~ 120
	timeout = 30
	#System提示词(将在每次请求中强制添加在最前)
	#System提示词独立于用户聊天历史之外
	presetPrompt = ""
	#AI在聊天界面中显示的聊天名称
	#这个名字会出现在AI消息之前
	chatDisplayName = "AI"
	#API请求使用模型的名称
	#指定使用哪个模型
	modelName = ""
	#是否启用消息前缀检测（默认开启）
	#开启时只处理以'#'开头的消息，关闭时处理所有非命令消息
	enablePrefix = true
	#是否启用上下文功能
	#启用后AI将记住之前的对话内容
	enableContext = false
	#上下文消息数量限制
	#设置AI能记住的对话消息数量（1-20）
	#Range: 1 ~ 20
	contextLimit = 2
	#API类型选择
	#支持: deepseek, spark (讯飞星火), openai（兼容）
	apiType = ""
	#是否启用联网搜索（支持此功能的API）
	#启用后AI可以访问实时网络信息
	enableWebSearch = false
	#是否启用深度思考（支持此功能的API）
	#启用后AI会进行更深入的推理
	enableDeepThinking = false
	#是否显示思考过程
	#启用后会显示AI的推理过程
	showThoughtProcess = false
```




本模组使用MIT协议开源
