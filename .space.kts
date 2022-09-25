/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Build and publish") {
    container(displayName = "Build and notify", image = "gradle:7.1-jre11") {
        kotlinScript { api ->
            try {
                api.gradle("build")
            } catch (ex: Exception) {
                val recipient = ChannelIdentifier.Channel(ChatChannel.FromName("CI-channel"))
                val content = ChatMessage.Text("Build failed")
                api.space().chats.messages.sendMessage(recipient, content)
            }
        }
    }
}
