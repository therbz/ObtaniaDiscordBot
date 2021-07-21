package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ButtonListener extends ListenerAdapter {

    // https://github.com/DV8FromTheWorld/JDA/wiki/Interactions#buttons
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        switch (event.getComponentId()) {
            case "upvote":
                event.deferEdit().queue();
                break;
            case "downvote":
                event.deferEdit().queue();
                break;

            case "open_bug_report":
                event.deferEdit().queue();

                DataStorage dataStorage = Main.getDataStorage();
                Long userCooldownTime = dataStorage.getUserBugReportCooldown(event.getUser());

                if (userCooldownTime + 30000 > System.currentTimeMillis()) {
                    event.getChannel().sendMessage("<@" + event.getUser().getId() + "> Please wait before opening another bug report.").queue(botMessage -> {
                        botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
                    });
                    return;
                }

                dataStorage.setUserBugReportCooldown(event.getUser(), System.currentTimeMillis());

                String channelName = "bug-report-" + generateBugReportId(6);

                event.getGuild().createTextChannel(channelName, event.getGuild().getCategoryById("867104424638677013")).queue(channel -> {
                    channel.createPermissionOverride(event.getMember()).setAllow(Permission.MESSAGE_READ).queue();

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("Bug Report opened by " + event.getUser().getAsTag(), null, event.getUser().getAvatarUrl());
                    embedBuilder.setColor(new Color(255, 212, 0));
                    embedBuilder.setDescription("Please use the Bug Report format as follows:\n\n**1) Description of the bug**\n**2) Steps to reproduce the bug**\n**3) What you think the actual result should be**\n\nPlease provide screenshots if applicable.");
                    embedBuilder.setFooter("Click \"Close Bug Report\" to close this bug report.");

                    channel.sendMessageEmbeds(embedBuilder.build()).setActionRow(net.dv8tion.jda.api.interactions.components.Button.danger("close_bug_report", "Close Bug Report"), Button.primary("bug_report_mention_therbz", "Request Assistance")).queue();

                    event.getChannel().sendMessage("<@" + event.getUser().getId() + "> Created your Bug Report at --> <#" + channel.getId() + ">").queue(botMessage -> {
                        botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
                    });
                });

                System.out.println("Opened a Bug Report by " + event.getUser().getAsTag() + " in channel " + channelName);

                break;
            case "close_bug_report":
                event.deferEdit().queue();
                event.getChannel().sendMessage("<@" + event.getUser().getId() + "> Are you sure you want to Close this bug report? Doing so will mean that it is unavailable to our staff team.").setActionRow(Button.danger("close_bug_report_really", "Yes, Close Bug Report"), Button.secondary("cancel_close_bug_report", "Cancel")).queue();
                break;
            case "close_bug_report_really":
                event.deferEdit().queue();
                event.getTextChannel().delete().queue();
                System.out.println("Closed Bug Report " + event.getTextChannel().getName());
                break;
            case "cancel_close_bug_report":
                event.deferEdit().queue();
                event.getMessage().delete().queue();
                break;
            case "bug_report_mention_therbz":
                event.deferEdit().queue();
                event.getTextChannel().sendMessage("<@222790155952586752> Bug report assistance requested by " + event.getUser().getName()).queue();
                break;

            case "undo_role_add":

                break;

            case "undo_role_remove":

                break;
        }
    }

    private String generateBugReportId(int stringLength) {
        int leftLimit = 48; // letter '0'
        int rightLimit = 57; // Letter '9'
        // int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
