package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("Message received: " + event.getMessage().getContentRaw());
        if (event.getMessage().getContentRaw().equalsIgnoreCase("!therbz")) {
            event.getChannel().sendMessage("test").queue();
        }
    }
}
