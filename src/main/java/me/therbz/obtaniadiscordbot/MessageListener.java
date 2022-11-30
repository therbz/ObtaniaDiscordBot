package me.therbz.obtaniadiscordbot;

import me.therbz.obtaniadiscordbot.commands.MuteCommand;
import me.therbz.obtaniadiscordbot.commands.UnmuteCommand;
import me.therbz.obtaniadiscordbot.suggestions.Suggestion;
import me.therbz.obtaniadiscordbot.suggestions.SuggestionStatus;
import me.therbz.obtaniadiscordbot.suggestions.SuggestionsManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.internal.entities.UserById;

import java.awt.*;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getChannelType().isGuild()) return;

        User author = event.getAuthor();
        Message message = event.getMessage();
        TextChannel channel = event.getTextChannel();
        String[] messageSplit = message.getContentRaw().split(" ");

        System.out.println("[#" + channel.getName() + "] " + author.getAsTag() + ": " + message.getContentRaw());

        // announcement :obtania:
        if (channel.getId().equals("699362386007687198")) {
            message.addReaction(Objects.requireNonNull(event.getGuild().getEmoteById("972796222920343592"))).queue();
        }

        // #suggestions
        if (channel.getId().equals("956228629396860938")) {
            message.delete().queue();
        }
        
        /*if (messageSplit[0].equalsIgnoreCase("!setsuggestionstatus")) {
            if (!Main.getPermissionsHandler().userIsMod(author)) return;
            if (messageSplit.length < 3) {
                channel.sendMessage("Incorrect number of args! `!setsuggestionstatus <id> <ONGOING, DENIED, ACCEPTED>`").queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            int id;
            try {
                id = Integer.parseInt(messageSplit[1]);
            } catch (NumberFormatException e) {
                channel.sendMessage("Unable to parse id: " + messageSplit[1]).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            SuggestionsManager manager = Main.getSuggestionsManager();

            if (manager.getSuggestionById(id) == null) {
                channel.sendMessage("Couldn't find suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            Suggestion suggestion = manager.getSuggestionById(id);
            Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).retrieveMessageById(suggestion.getMessageId()).queue(suggestionMessage -> {
                EmbedBuilder embedBuilder = new EmbedBuilder(suggestionMessage.getEmbeds().get(0));

                switch (messageSplit[2].toLowerCase(Locale.ROOT)) {
                    case "ongoing":
                        manager.setSuggestionStatus(id, SuggestionStatus.ONGOING);
                        embedBuilder.setTitle("Suggestion #" + id + " | !suggest");
                        break;

                    case "denied":
                        manager.setSuggestionStatus(id, SuggestionStatus.DENIED);
                        embedBuilder.setTitle("Suggestion #" + id + " (Denied) | !suggest");
                        break;

                    case "accepted":
                        manager.setSuggestionStatus(id, SuggestionStatus.ACCEPTED);
                        embedBuilder.setTitle("Suggestion #" + id + " (Accepted) | !suggest");
                        break;

                    default:
                        channel.sendMessage("Incorrect status! `!setsuggestionstatus <id> <ONGOING, DENIED, ACCEPTED>`").queue(botMessage -> botMessage.delete().queueAfter(1, TimeUnit.SECONDS));
                }

                embedBuilder.setColor(suggestion.getSuggestionStatus().getColor());
                Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).editMessageEmbedsById(suggestion.getMessageId(), embedBuilder.build()).queue();
            });

            channel.sendMessage("Successfully updated Suggestion #" + id).queue();
        }*/
        if (messageSplit[0].equalsIgnoreCase("!accept")) {
            if (!Main.getPermissionsHandler().userIsMod(author)) return;
            if (messageSplit.length < 2) {
                channel.sendMessage("Incorrect number of args! `!accept <id>`").queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            int id;
            try {
                id = Integer.parseInt(messageSplit[1]);
            } catch (NumberFormatException e) {
                channel.sendMessage("Unable to parse id: " + messageSplit[1]).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            SuggestionsManager manager = Main.getSuggestionsManager();

            if (manager.getSuggestionById(id) == null) {
                channel.sendMessage("Couldn't find suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            Suggestion suggestion = manager.getSuggestionById(id);
            Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).retrieveMessageById(suggestion.getMessageId()).queue(suggestionMessage -> {
                EmbedBuilder embedBuilder = new EmbedBuilder(suggestionMessage.getEmbeds().get(0));
                manager.setSuggestionStatus(id, SuggestionStatus.ACCEPTED);
                embedBuilder.setTitle("Suggestion #" + id + " (Accepted) | !suggest");
                embedBuilder.setColor(suggestion.getSuggestionStatus().getColor());
                Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).editMessageEmbedsById(suggestion.getMessageId(), embedBuilder.build()).queue();
            });

            channel.sendMessage("Successfully accepted Suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
        }
        else if (messageSplit[0].equalsIgnoreCase("!deny")) {
            if (!Main.getPermissionsHandler().userIsMod(author)) return;
            if (messageSplit.length < 2) {
                channel.sendMessage("Incorrect number of args! `!deny <id>`").queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            int id;
            try {
                id = Integer.parseInt(messageSplit[1]);
            } catch (NumberFormatException e) {
                channel.sendMessage("Unable to parse id: " + messageSplit[1]).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            SuggestionsManager manager = Main.getSuggestionsManager();

            if (manager.getSuggestionById(id) == null) {
                channel.sendMessage("Couldn't find suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            Suggestion suggestion = manager.getSuggestionById(id);
            Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).retrieveMessageById(suggestion.getMessageId()).queue(suggestionMessage -> {
                EmbedBuilder embedBuilder = new EmbedBuilder(suggestionMessage.getEmbeds().get(0));
                manager.setSuggestionStatus(id, SuggestionStatus.DENIED);
                embedBuilder.setTitle("Suggestion #" + id + " (Denied) | !suggest");
                embedBuilder.setColor(suggestion.getSuggestionStatus().getColor());
                Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).editMessageEmbedsById(suggestion.getMessageId(), embedBuilder.build()).queue();
            });

            channel.sendMessage("Successfully denied Suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
        }
        else if (messageSplit[0].equalsIgnoreCase("!ongoing")) {
            if (!Main.getPermissionsHandler().userIsMod(author)) return;
            if (messageSplit.length < 2) {
                channel.sendMessage("Incorrect number of args! `!accept <id>`").queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            int id;
            try {
                id = Integer.parseInt(messageSplit[1]);
            } catch (NumberFormatException e) {
                channel.sendMessage("Unable to parse id: " + messageSplit[1]).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            SuggestionsManager manager = Main.getSuggestionsManager();

            if (manager.getSuggestionById(id) == null) {
                channel.sendMessage("Couldn't find suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            Suggestion suggestion = manager.getSuggestionById(id);
            Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).retrieveMessageById(suggestion.getMessageId()).queue(suggestionMessage -> {
                EmbedBuilder embedBuilder = new EmbedBuilder(suggestionMessage.getEmbeds().get(0));
                manager.setSuggestionStatus(id, SuggestionStatus.ONGOING);
                embedBuilder.setTitle("Suggestion #" + id + " | !suggest");
                embedBuilder.setColor(suggestion.getSuggestionStatus().getColor());
                Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).editMessageEmbedsById(suggestion.getMessageId(), embedBuilder.build()).queue();
            });

            channel.sendMessage("Successfully set ongoing Suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
        }
        else if (messageSplit[0].equalsIgnoreCase("!delete")) {
            if (!Main.getPermissionsHandler().userIsMod(author)) return;
            if (messageSplit.length < 2) {
                channel.sendMessage("Incorrect number of args! `!delete <id>`").queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            int id;
            try {
                id = Integer.parseInt(messageSplit[1]);
            } catch (NumberFormatException e) {
                channel.sendMessage("Unable to parse id: " + messageSplit[1]).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            SuggestionsManager manager = Main.getSuggestionsManager();

            if (manager.getSuggestionById(id) == null) {
                channel.sendMessage("Couldn't find suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            Suggestion suggestion = manager.getSuggestionById(id);
            Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).retrieveMessageById(suggestion.getMessageId()).queue(suggestionMessage -> {
                suggestionMessage.delete().queue();
            });
            manager.deleteSuggestion(id);

            channel.sendMessage("Successfully deleted Suggestion #" + id).queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
        }
        /*else if (messageSplit[0].equalsIgnoreCase("!ms")) {
            if (!Main.getPermissionsHandler().userIsMod(author)) return;
            if (messageSplit.length < 2) {
                channel.sendMessage("Incorrect number of args! `!ms <messageId>`").queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            String messageId = messageSplit[1];

            Objects.requireNonNull(event.getGuild().getTextChannelById("956228629396860938")).retrieveMessageById(messageId).queue(message1 -> {
                String content = message1.getEmbeds().get(0).getDescription();

                SuggestionsManager manager = Main.getSuggestionsManager();
                manager.manuallyAddSuggestion(messageId, content);
            });

            channel.sendMessage("Manually added Suggestion").queue();
            System.out.println("Manually added Suggestion");
        }*/

        // !suggest
        else if (messageSplit[0].equalsIgnoreCase("!suggest")) {
            message.delete().queue();

            DataStorage dataStorage = Main.getDataStorage();
            Long userCooldownTime = dataStorage.getUserSuggestCooldown(event.getMember().getUser());

            if (userCooldownTime != null && userCooldownTime + 30000 > System.currentTimeMillis()) {
                channel.sendMessage("<@" + event.getMember().getUser().getId() + "> Please wait before posting another suggestion.").queue(botMessage -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            dataStorage.setUserSuggestCooldown(event.getMember().getUser(), System.currentTimeMillis());

            if (messageSplit.length < 2) {
                channel.sendMessage("<@" + author.getId() + ">\nYou must supply a suggestion! Example: `!suggest Add more rats!`").queue(botMessage -> {
                    botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
                });
                return;
            }

            String suggestion = message.getContentRaw().replace("!suggest", "");

            Main.getSuggestionsManager().makeSuggestion(message.getAuthor(), suggestion, message.getGuild());
        }
        else if (channel == event.getGuild().getTextChannelById("956228629396860938")) {
            DataStorage dataStorage = Main.getDataStorage();
            Long userCooldownTime = dataStorage.getUserSuggestCooldown(event.getMember().getUser());

            if (userCooldownTime != null && userCooldownTime + 30000 > System.currentTimeMillis()) {
                channel.sendMessage("<@" + event.getMember().getUser().getId() + "> Please wait before posting another suggestion.").queue(botMessage -> {
                    botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
                });
                return;
            }

            dataStorage.setUserSuggestCooldown(event.getMember().getUser(), System.currentTimeMillis());

            Main.getSuggestionsManager().makeSuggestion(message.getAuthor(), message.getContentRaw(), message.getGuild());
        }

        else if (messageSplit[0].equalsIgnoreCase("!createbugreportmessage")) {
            if (!author.getId().equals("222790155952586752")) return;

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Open a Bug Report");
            embedBuilder.setColor(new Color(255, 212, 0));
            embedBuilder.setDescription("You can report a bug by clicking the button below.\nYou will be taken to a separate private channel to explain the bug and provide any helpful related information.");

            channel.sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.primary("open_bug_report", "Open a Bug Report")).queue();
        }

        else if (messageSplit[0].equalsIgnoreCase("!createreactionmessage")) {
            if (!author.getId().equals("222790155952586752")) return;
            if (messageSplit.length < 2) return;

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
                    embedBuilder.setDescription("React to this message with <:obtania:972796222920343592> to receive a mention whenever we make any update in <#705021150371119144>. Beware that this will be spammy at times.");
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

        else if (messageSplit[0].equalsIgnoreCase("!tnttutorial")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("How to make TNT");
            embedBuilder.setImage("https://staticg.sportskeeda.com/editor/2021/01/81507-16097433431054-800.jpg");
            embedBuilder.setTimestamp(new Date().toInstant());
            embedBuilder.setFooter("Requested by " + message.getAuthor().getAsTag(), message.getAuthor().getAvatarUrl());

            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        }

        else if (messageSplit[0].equalsIgnoreCase("!mute")) {
            new MuteCommand(event, messageSplit);
        }

        else if (messageSplit[0].equalsIgnoreCase("!unmute")) {
            new UnmuteCommand(event, messageSplit);
        }

        else if (messageSplit[0].equalsIgnoreCase("!dothingy123")) {
            if (!author.getId().equals("222790155952586752")) return;

            channel.sendMessage("<@&774982037664170016>").queue();
        }
    }
}
