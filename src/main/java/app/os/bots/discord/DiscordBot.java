package app.os.bots.discord;

import app.os.main.OS;
import app.os.utilities.ConsoleHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DiscordBot {
    private final Map<DiscordProperties, String> loadProperties;

    public DiscordBot(Map<DiscordProperties, String> loadProperties) {
        this.loadProperties = loadProperties;
    }

    public void launch() {
        try {
            JDA api = JDABuilder.createDefault(loadProperties.get(DiscordProperties.BOT_TOKEN)).build();
            api.awaitReady();
            api.addEventListener(new MainListener());

            ConsoleHelper.println("Discord bot started!");
        } catch (LoginException | InterruptedException e) {
            ConsoleHelper.println("Discord bot didn't started!");
            ConsoleHelper.errln("Ошибка! " + e + ".");
        }
    }

    private class MainListener extends ListenerAdapter {
        @Override
        public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
            Message received = event.getMessage();
            String rawData = received.getContentRaw();


            try {
                if (!rawData.startsWith(loadProperties.get(DiscordProperties.BOT_PREFIX)))
                    return;
                else
                    rawData = rawData.substring(1);
            } catch (NullPointerException e) {
                ConsoleHelper.errln("No prefix founded in config file!");
            }

            // user commands
            if (rawData.startsWith("info")) {
                EmbedBuilder infoMsg = new EmbedBuilder();

                Date currentDate = new Date();
                infoMsg.setColor(Color.decode(loadProperties.get(DiscordProperties.MESSAGES_COLOR)));

                infoMsg.addField("Date", new SimpleDateFormat(loadProperties.get(DiscordProperties.DATE_FORMAT)).format(currentDate), false);
                infoMsg.addField("Name", loadProperties.get(DiscordProperties.BOT_NAME), true);
                infoMsg.addField("ver.", "v" + OS.VERSION, true);

                received.getChannel().sendMessage(infoMsg.build()).queue(success -> {
                    long ping = event.getMessage().getTimeCreated().until(success.getTimeCreated(), ChronoUnit.MILLIS);
                    infoMsg.addField("Ping", String.valueOf(ping), false);
                    success.getChannel().editMessageById(success.getId(), infoMsg.build()).queue();
                });
            } else if (rawData.startsWith("ping")) {
                received.getChannel().sendMessage("Pong!").queue(success -> {
                    long ping = event.getMessage().getTimeCreated().until(success.getTimeCreated(), ChronoUnit.MILLIS);
                    success.getChannel().editMessageById(success.getId(), ("Pong!\nPing is **" + ping + "**ms.")).queue();
                });
            }


            // admin commands
            List<Role> adminRoles = received.getGuild().getRolesByName(loadProperties.get(DiscordProperties.ADMIN_ROLE), true);
            if (Objects.requireNonNull(received.getMember()).getRoles().containsAll(adminRoles)) {

                if (rawData.startsWith("clear")) {
                    Commands.AdminCommands.clearMessages(received.getTextChannel());
                } else if (rawData.startsWith("text")) {
                    StringBuilder textChannelsString = new StringBuilder();
                    textChannelsString.append("Текстовые чаты на сервере **").append(received.getGuild().getName()).append("**.\n");
                    received.getGuild().getTextChannels().forEach(textChannel -> textChannelsString.append("> <#").append(textChannel.getId()).append(">\n"));

                    received.getChannel().sendMessage(
                            Commands.Utilities.sendEmbedMessage(textChannelsString.toString(), loadProperties.get(DiscordProperties.MESSAGES_COLOR))).queue();
                } else if (rawData.startsWith("voice")) {
                    StringBuilder voiceChannelsString = new StringBuilder();
                    voiceChannelsString.append("Голосовые комнаты на сервере **").append(received.getGuild().getName()).append("**.\n");
                    received.getGuild().getVoiceChannels().forEach(voiceChannel -> voiceChannelsString.append("> <#").append(voiceChannel.getId()).append(">\n"));

                    received.getChannel().sendMessage(
                            Commands.Utilities.sendEmbedMessage(voiceChannelsString.toString(), loadProperties.get(DiscordProperties.MESSAGES_COLOR))).queue();
                }

                List<TextChannel> mentionedChannels = received.getMentionedChannels();
                if (!mentionedChannels.isEmpty()) {
                    if (rawData.startsWith("send")) {
                        String messageText = rawData.substring(rawData.split(" ")[0].length());
                        for (TextChannel mentionedChannel : mentionedChannels) {
                            messageText = messageText.replaceAll("<#" + mentionedChannel.getId() + ">", "");
                        }

                        for (TextChannel mentionedChannel : mentionedChannels) {
                            mentionedChannel.sendMessage(Commands.Utilities.sendEmbedMessage(messageText, loadProperties.get(DiscordProperties.MESSAGES_COLOR))).queue(
                                    success -> received.getChannel().sendMessage("Сообщение в чат <#" + mentionedChannel.getId() + "> **успешно** отправлено!").queue());
                        }
                    }
                }

                List<Member> mentionedMembers = received.getMentionedMembers();
                if (!mentionedMembers.isEmpty()) {
                    if (rawData.startsWith("warn")) {
                        for (Member memberToWarn : mentionedMembers) {
                            Commands.AdminCommands.warnUser(memberToWarn);
                            received.getChannel().sendMessage(String.format("<@%s> получил **варн**!", memberToWarn.getId())).queue();
                        }
                    } else if (rawData.startsWith("ban")) {
                        for (Member memberToTimeBan : mentionedMembers) {
                            Commands.AdminCommands.timeBan(memberToTimeBan);
                            received.getChannel().sendMessage(String.format("<@%s> получил **временный** бан! (снятие ролей)", memberToTimeBan.getId())).queue();
                        }
                    } else if (rawData.startsWith("permban")) {
                        for (Member memberToPermBan : mentionedMembers) {
                            Commands.AdminCommands.permBan(memberToPermBan);
                            received.getChannel().sendMessage(String.format("<@%s> получил **перманентный** бан!", memberToPermBan.getId())).queue();
                        }
                    } else if (rawData.startsWith("verify")) {
                        for (Member memberToVerify : mentionedMembers) {
                            Commands.AdminCommands.verify(memberToVerify);
                            received.getChannel().sendMessage(String.format("<@%s> прошел **верефикацию**.", memberToVerify.getId())).queue();
                        }
                    } else if (rawData.startsWith("unwarn")) {
                        for (Member memberToUnWarn : mentionedMembers) {
                            Commands.AdminCommands.unWarn(memberToUnWarn);
                            received.getChannel().sendMessage(String.format("У <@%s> сняты **все** варны.", memberToUnWarn.getId())).queue();
                        }
                    } else {
                        received.getChannel().sendMessage("**Неизвестная** команда!").queue();
                    }
                }
            }
        }
    }

    private static class Commands {
        private static class Utilities {
            private static MessageEmbed sendEmbedMessage(String text, String color) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setDescription(text);
                builder.setColor(Color.decode(color));

                return builder.build();
            }
        }

        private static class AdminCommands {
            private static void warnUser(Member member) {
                List<Role> addWarns = member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("warn") && !member.getRoles().contains(role)).collect(Collectors.toList());

                if (!addWarns.isEmpty())
                    member.getGuild().addRoleToMember(member.getUser().getId(), addWarns.get(addWarns.size() - 1)).queue();
                else timeBan(member);
            }

            private static void timeBan(Member member) {
                member.getRoles().stream().filter(role -> !role.getName().toLowerCase().contains("ban")).forEach(role -> member.getGuild().removeRoleFromMember(member.getUser().getId(), role).queue());
            }

            private static void permBan(Member member) {
                member.ban(1, "WARNS").queue();
            }

            private static void verify(Member member) {
                member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("verified")).forEach(role -> member.getGuild().addRoleToMember(member.getUser().getId(), role).queue());
            }

            private static void unWarn(Member member) {
                member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("warn")).forEach(role -> member.getGuild().removeRoleFromMember(member.getUser().getId(), role).queue());
            }

            private static void clearMessages(TextChannel channel) {
                AtomicBoolean isWorking = new AtomicBoolean(true);
                OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

                new Thread(() -> {
                    while (isWorking.get()) {
                        List<Message> messages = channel.getHistory().retrievePast(50).complete();
                        messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));

                        if (messages.isEmpty()) {
                            isWorking.set(false);
                            return;
                        }

                        try {
                            channel.deleteMessages(messages).complete();
                        } catch (java.lang.IllegalArgumentException e) {
                            channel.sendMessage("Произошла **ошибка** при выполнении операции!\n\n" + "``" + e.getMessage() + "``").queue();
                            return;
                        }

                    }
                }).start();
            }
        }
    }
}