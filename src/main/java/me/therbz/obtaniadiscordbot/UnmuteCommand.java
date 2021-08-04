package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class UnmuteCommand {
    public UnmuteCommand(MessageReceivedEvent event, String[] messageSplit) {
        Member member = event.getMember();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            /*event.getChannel().sendMessage("No permission!").queue(botMessage -> {
                botMessage.delete().queueAfter(5, TimeUnit.SECONDS);
            });*/
            return;
        }

        List<Member> targets = event.getMessage().getMentionedMembers();

        if (targets.size() == 0) {
            event.getChannel().sendMessage("You must mention at least one user to mute!").queue(botMessage -> {
                botMessage.delete().queueAfter(5, TimeUnit.SECONDS);
            });
            return;
        }

        StringBuilder muteReturnStringBuilder = new StringBuilder();

        for (int i=0; i<targets.size(); i++) {
            event.getGuild().removeRoleFromMember(targets.get(i), event.getGuild().getRoleById("581143000021991424")).queue();
            muteReturnStringBuilder.append(targets.get(i).getUser().getAsTag());
            if (i < targets.size() - 1) muteReturnStringBuilder.append(", ");
        }

        String muteReturnString = muteReturnStringBuilder.toString();

        event.getChannel().sendMessage("Unmuted " + muteReturnString + ".").queue(botMessage -> {
            botMessage.delete().queueAfter(5, TimeUnit.SECONDS);
        });
    }
}
