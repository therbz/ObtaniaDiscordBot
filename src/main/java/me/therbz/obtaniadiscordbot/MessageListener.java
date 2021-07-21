package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getChannelType().isGuild()) return;

        System.out.println("[#" + event.getChannel().getName() + "] " + event.getAuthor().getAsTag() + ": " + event.getMessage().getContentRaw());

        Message message = event.getMessage();
        String[] messageSplit = message.getContentRaw().split(" ");

        /*
        !suggest
         */
        if (messageSplit[0].equalsIgnoreCase("!suggest")) {
            message.delete().queue();

            if (messageSplit.length < 2) {
                event.getChannel().sendMessage("<@" + event.getAuthor().getId() + ">\nYou must supply a suggestion! Example: `!suggest Add more rats!`").queue(botMessage -> {
                    botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
                });
                return;
            }

            String suggestion = message.getContentRaw().replace("!suggest", "");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Towny Suggestion | !suggest");
            embedBuilder.setColor(new Color(255, 212, 0));
            embedBuilder.setDescription(suggestion);
            embedBuilder.setFooter("Suggested by " + event.getAuthor().getAsTag(), message.getAuthor().getAvatarUrl());
            embedBuilder.setTimestamp(new Date().toInstant());

            event.getGuild().getTextChannelById("699524366660010055").sendMessageEmbeds(embedBuilder.build())/*.setActionRow(Button.success("upvote", "Upvote"), Button.danger("downvote", "Downvote"))*/.queue(botMessage -> {
                botMessage.addReaction("✅").queue();
                botMessage.addReaction("❌").queue();
            });
        }
        else if (event.getChannel() == event.getGuild().getTextChannelById("699524366660010055")) {
            message.delete().queue();
            event.getChannel().sendMessage("<@" + event.getAuthor().getId() + ">\nPlease keep suggestions discussion to <#723448278666182737>.\nIf you want to make a suggestion, use `!suggest` in this channel or <#699358412210962496>.").queue(botMessage -> {
                botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
            });
        }

        else if (messageSplit[0].equalsIgnoreCase("!createbugreportmessage")) {
            if (event.getAuthor().getId().equals("222790155952586752")) {
                message.delete().queue();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Open a Bug Report");
                embedBuilder.setColor(new Color(255, 212, 0));
                embedBuilder.setDescription("You can report a bug by clicking the button below.\nYou will be taken to a separate private channel to explain the bug and provide any helpful related information.");

                event.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.primary("open_bug_report", "Open a Bug Report")).queue();
            }
        }

        else if (messageSplit[0].equalsIgnoreCase("!createreactionmessage")) {
            if (!event.getAuthor().getId().equals("222790155952586752")) return;
            if (messageSplit.length < 2) return;

            message.delete().queue();

            EmbedBuilder embedBuilder = new EmbedBuilder();

            switch (messageSplit[1]) {
                case "news":
                    embedBuilder.setColor(new Color(255, 170, 0));
                    embedBuilder.setTitle("News - Recommended");
                    embedBuilder.setDescription("React to this message with :ok_hand: to receive a mention whenever we post news, events, giveaways, sales and major updates.");
                    embedBuilder.setFooter("Remove your reaction to stop receiving mentions.");
                    break;

                case "updates":
                    embedBuilder.setColor(new Color(85, 255, 85));
                    embedBuilder.setTitle("Updates ");
                    embedBuilder.setDescription("React to this message with <:obtania:706542373903138907> to receive a mention whenever we make any update in <#705021150371119144>. Beware that this will be spammy at times.");
                    embedBuilder.setFooter("Remove your reaction to stop receiving mentions.");
                    break;

                case "voteparty":
                    embedBuilder.setColor(new Color(85, 255, 255));
                    embedBuilder.setTitle("VoteParty");
                    embedBuilder.setDescription("React to this message with :gem: to receive a mention when there are only 10 and 5 votes remaining until a VoteParty starts, so that you can quickly log in for it.");
                    embedBuilder.setFooter("Remove your reaction to stop receiving mentions.");
                    break;

                case "v5":
                    embedBuilder.setColor(new Color(255, 85, 85));
                    embedBuilder.setTitle("v5 Development");
                    embedBuilder.setDescription("React to this message with <:kanye:828712305390387210> to receive a mention when there is a new v5 development update.");
                    embedBuilder.setFooter("Remove your reaction to stop receiving mentions.");
                    break;
            }

            message.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
