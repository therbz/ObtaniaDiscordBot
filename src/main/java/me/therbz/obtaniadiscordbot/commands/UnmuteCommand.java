package me.therbz.obtaniadiscordbot.commands;

import me.therbz.obtaniadiscordbot.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UnmuteCommand {
    public UnmuteCommand(MessageReceivedEvent event, String[] messageSplit) {
        if (!Main.getPermissionsHandler().userIsMod(event.getAuthor())) return;

        List<Member> targets = event.getMessage().getMentionedMembers();

        if (targets.size() == 0) {
            event.getChannel().sendMessage("You must mention at least one user to mute!").queue(botMessage -> {
                botMessage.delete().queueAfter(5, TimeUnit.SECONDS);
            });
            return;
        }

        StringBuilder muteReturnStringBuilder = new StringBuilder();

        for (int i=0; i<targets.size(); i++) {
            event.getGuild().removeRoleFromMember(targets.get(i), Objects.requireNonNull(event.getGuild().getRoleById("581143000021991424"))).queue();
            muteReturnStringBuilder.append(targets.get(i).getUser().getAsTag());
            if (i < targets.size() - 1) muteReturnStringBuilder.append(", ");
        }

        String muteReturnString = muteReturnStringBuilder.toString();

        event.getChannel().sendMessage("Unmuted " + muteReturnString + ".").queue(botMessage -> {
            botMessage.delete().queueAfter(5, TimeUnit.SECONDS);
        });
    }
}
