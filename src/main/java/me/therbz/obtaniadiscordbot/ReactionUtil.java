package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.util.Date;

public class ReactionUtil {

    // I know this is a tad hacky but whatever I'll fix it if need be

    public static void addReactionRole(Long userId, Guild guild, String messageId) {
        Role role = guild.getRoleById(messageIdToReactionRoleId(messageId));

        guild.addRoleToMember(userId, role).queue();

        Main.jda.retrieveUserById(userId).complete().openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Reaction Role Added");
            embedBuilder.setDescription("You have added the " + role.getName() + " role in ObtaniaMC.");
            embedBuilder.setFooter("Made by therbz");
            embedBuilder.setTimestamp(new Date().toInstant());
            embedBuilder.setColor(Color.green);

            privateChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    public static void removeReactionRole(Long userId, Guild guild, String messageId) {
        Role role = guild.getRoleById(messageIdToReactionRoleId(messageId));

        guild.removeRoleFromMember(userId, role).queue();

        Main.jda.retrieveUserById(userId).complete().openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Reaction Role Removed");
            embedBuilder.setDescription("You have removed the " + role.getName() + " role in ObtaniaMC.");
            embedBuilder.setFooter("Made by therbz");
            embedBuilder.setTimestamp(new Date().toInstant());
            embedBuilder.setColor(Color.red);

            privateChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    public static String messageIdToReactionRoleId(String messageId) {
        String roleId = "NOT_A_REACTION_MESSAGE";

        switch (messageId) {
            // News
            case "867360685524516874":
                roleId = "606866125950812216";
                break;

            // Updates
            case "867360695045718016":
                roleId = "721725595012038797";
                break;

            // Voteparty
            case "867360717795885096":
                roleId = "774982037664170016";
                break;

            // v5
            case "867360727173824522":
                roleId = "831845783253942314";
                break;
        }

        return roleId;
    }
}
