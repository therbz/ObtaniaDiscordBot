package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;

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
            embedBuilder.setDescription("You have been given the " + role.getName() + " role in Obtania Towny.");
            embedBuilder.setFooter("Made by therbz");
            embedBuilder.setTimestamp(new Date().toInstant());
            embedBuilder.setColor(Color.green);

            privateChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(net.dv8tion.jda.api.interactions.components.Button.secondary("undo_role_add", "Undo This")).queue();
        });
    }

    public static void removeReactionRole(Long userId, Guild guild, String messageId) {
        Role role = guild.getRoleById(messageIdToReactionRoleId(messageId));

        guild.removeRoleFromMember(userId, role).queue();

        Main.jda.retrieveUserById(userId).complete().openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Reaction Role Removed");
            embedBuilder.setDescription("You have removed the " + role.getName() + " role in Obtania Towny.");
            embedBuilder.setFooter("Made by therbz");
            embedBuilder.setTimestamp(new Date().toInstant());
            embedBuilder.setColor(Color.red);

            privateChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.secondary("undo_role_remove", "Undo This")).queue();
        });
    }

    public static String messageIdToReactionRoleId(String messageId) {
        String roleId = "null";

        switch (messageId) {
            // News
            case "606869054707335203":
                roleId = "606866125950812216";
                break;

            // Updates
            case "721725312056164382":
                roleId = "721725595012038797";
                break;

            // Voteparty
            case "775104832771457065":
                roleId = "774982037664170016";
                break;

            // v5
            case "831845134999486485":
                roleId = "831845783253942314";
                break;
        }

        return roleId;
    }
}
