package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.getChannel().getId().equals("606865849654968320")) return;

        ReactionUtil.addReactionRole(event.getUserIdLong(), event.getGuild(), event.getMessageId());
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (!event.getChannel().getId().equals("606865849654968320")) return;

        ReactionUtil.removeReactionRole(event.getUserIdLong(), event.getGuild(), event.getMessageId());
    }

    /*@Override
    public void onMessageReactionRemoval(MessageReactionRemoveEmoteEvent event) {
        if (!event.getChannel().getId().equals("606865849654968320")) return;

        ReactionUtil.removeReactionRole(event.getMember(), event.getMessageId());
    }*/
}
