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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DiscordBot {
    private final Map<DiscordLoadProperties, String> loadProperties;

    public DiscordBot(Map<DiscordLoadProperties, String> loadProperties) {
        this.loadProperties = loadProperties;
    }

    public void launch() {
        try {
            JDA api = JDABuilder.createDefault(loadProperties.get(DiscordLoadProperties.BOT_TOKEN)).build();
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

            // load server config
            Path serverConfigFile = Paths.get(OS.DIR_CONFIGS + event.getGuild().getIdLong() + ".config");
            if (Files.notExists(serverConfigFile)) {
                try {
                    Files.createFile(serverConfigFile);

                    Properties newProperties = new Properties();
                    newProperties.put("adminRole", "admin");

                    newProperties.store(new FileWriter(serverConfigFile.toFile()), "generated");
                } catch (IOException e) {
                    ConsoleHelper.errln("Не удалось создать файл конфига! " + e.getMessage());
                    event.getChannel().sendMessage("``" + e.getMessage() + "``").queue();
                }
            }

            Properties serverConfig = new Properties();
            try {
                serverConfig.load(new FileReader(serverConfigFile.toFile()));
            } catch (IOException e) {
                ConsoleHelper.errln("Не удалось загрузить конфиг!");
                event.getChannel().sendMessage("``" + e.getMessage() + "``").queue();
            }


            // ----


            try {
                if (!rawData.startsWith(loadProperties.get(DiscordLoadProperties.BOT_PREFIX)))
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
                infoMsg.setColor(Color.decode(loadProperties.get(DiscordLoadProperties.MESSAGES_COLOR)));

                infoMsg.addField("Server's name", loadProperties.get(DiscordLoadProperties.SERVER_NAME), true);
                infoMsg.addField("Date", new SimpleDateFormat(loadProperties.get(DiscordLoadProperties.DATE_FORMAT)).format(currentDate), true);
                infoMsg.addField("<<>>", "<<>>", false);
                infoMsg.addField("Name", loadProperties.get(DiscordLoadProperties.BOT_NAME), true);
                infoMsg.addField("Ver.", "v" + OS.VERSION, true);

                received.getChannel().sendMessage(infoMsg.build()).queue(success -> {
                    long ping = event.getMessage().getTimeCreated().until(success.getTimeCreated(), ChronoUnit.MILLIS);
                    infoMsg.addField("Ping", String.valueOf(ping), true);
                    success.getChannel().editMessageById(success.getId(), infoMsg.build()).queue();
                });
            } else if (rawData.startsWith("ping")) {
                received.getChannel().sendMessage("Pong!").queue(success -> {
                    long ping = event.getMessage().getTimeCreated().until(success.getTimeCreated(), ChronoUnit.MILLIS);
                    success.getChannel().editMessageById(success.getId(), ("Pong!\nPing is **" + ping + "**ms.")).queue();
                });
            }


            // admin commands
            List<Role> adminRoles = received.getGuild().getRoles()
                    .stream()
                    .filter(role -> role.getName().toLowerCase().contains(serverConfig.getProperty("adminRole").toLowerCase()))
                    .collect(Collectors.toList());

                    if (Objects.requireNonNull(received.getMember()).isOwner() || (Objects.requireNonNull(received.getMember()).getRoles().containsAll(adminRoles) && !adminRoles.isEmpty())) {
                if (rawData.startsWith("clear")) {
                    AtomicBoolean isWorking = new AtomicBoolean(true);
                    OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

                    new Thread(() -> {
                        while (isWorking.get()) {
                            List<Message> messages = received.getTextChannel().getHistory().retrievePast(50).complete();
                            messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));

                            if (messages.isEmpty()) {
                                isWorking.set(false);
                                return;
                            }

                            try {
                                received.getTextChannel().deleteMessages(messages).complete();
                            } catch (java.lang.IllegalArgumentException e) {
                                received.getTextChannel().sendMessage("Произошла **ошибка** при выполнении операции!\n\n" + "``" + e.getMessage() + "``").queue();
                                return;
                            }

                        }
                    }).start();
                } else if (rawData.startsWith("text")) {
                    StringBuilder textChannelsString = new StringBuilder();
                    textChannelsString.append("Текстовые чаты на сервере **").append(received.getGuild().getName()).append("**.\n");
                    received.getGuild().getTextChannels().forEach(textChannel -> textChannelsString.append("- <#").append(textChannel.getId()).append(">\n"));

                    received.getChannel().sendMessage(
                            sendEmbedMessage(textChannelsString.toString())).queue();
                } else if (rawData.startsWith("voice")) {
                    StringBuilder voiceChannelsString = new StringBuilder();
                    voiceChannelsString.append("Голосовые комнаты на сервере **").append(received.getGuild().getName()).append("**.\n");
                    received.getGuild().getVoiceChannels().forEach(voiceChannel -> voiceChannelsString.append("- ").append(voiceChannel.getName()).append("\n"));

                    received.getChannel().sendMessage(
                            sendEmbedMessage(voiceChannelsString.toString())).queue();
                } else if (rawData.startsWith("admin")) {
                    if (rawData.split(" ").length >= 2) {
                        String adminRole = rawData.substring((loadProperties.get(DiscordLoadProperties.BOT_PREFIX) + "admin").length());
                        try {
                            String buff = serverConfig.getProperty("adminRole");

                            serverConfig.setProperty("adminRole", adminRole);
                            serverConfig.store(new FileWriter(serverConfigFile.toFile()), "changed");

                            event.getChannel().sendMessage("Роль администратора изменена с **" + buff + "** на **" + serverConfig.getProperty("adminRole") + "**.").queue();
                        } catch (IOException e) {
                            ConsoleHelper.errln("Не удалось изменить файл конфига! " + e.getMessage());
                            event.getChannel().sendMessage("``" + e.getMessage() + "``").queue();
                        }
                    }
                }

                List<TextChannel> mentionedChannels = received.getMentionedChannels();
                if (!mentionedChannels.isEmpty()) {
                    if (rawData.startsWith("send")) {
                        String messageText = rawData.substring(rawData.split(" ")[0].length());
                        for (TextChannel mentionedChannel : mentionedChannels) {
                            messageText = messageText.replaceAll("<#" + mentionedChannel.getId() + ">", "");
                        }

                        for (TextChannel mentionedChannel : mentionedChannels) {
                            mentionedChannel.sendMessage(sendEmbedMessage(messageText)).queue(
                                    success -> received.getChannel().sendMessage("Сообщение в чат <#" + mentionedChannel.getId() + "> **успешно** отправлено!").queue());
                        }
                    }
                }

                List<Member> mentionedMembers = received.getMentionedMembers();
                if (!mentionedMembers.isEmpty()) {
                    if (rawData.startsWith("warn")) {
                        for (Member member : mentionedMembers) {
                            List<Role> addWarns = member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("warn") && !member.getRoles().contains(role)).collect(Collectors.toList());
                            if (!addWarns.isEmpty()) {
                                member.getGuild().addRoleToMember(member.getUser().getId(), addWarns.get(addWarns.size() - 1)).queue();
                            } else {
                                member.getRoles().stream().filter(role -> !role.getName().toLowerCase().contains("ban")).forEach(role -> member.getGuild().removeRoleFromMember(member.getUser().getId(), role).queue());
                                received.getChannel().sendMessage(String.format("<@%s> получил **временный** бан! (снятие ролей)", member.getId())).queue();
                            }
                            received.getChannel().sendMessage(String.format("<@%s> получил **варн**!", member.getId())).queue();
                        }
                    } else if (rawData.startsWith("ban")) {
                        for (Member member : mentionedMembers) {
                            member.getRoles().stream().filter(role -> !role.getName().toLowerCase().contains("ban")).forEach(role -> member.getGuild().removeRoleFromMember(member.getUser().getId(), role).queue());
                            received.getChannel().sendMessage(String.format("<@%s> получил **временный** бан! (снятие ролей)", member.getId())).queue();
                        }
                    } else if (rawData.startsWith("permban")) {
                        for (Member member : mentionedMembers) {
                            member.ban(1, "WARNS").queue();
                            received.getChannel().sendMessage(String.format("<@%s> получил **перманентный** бан!", member.getId())).queue();
                        }
                    } else if (rawData.startsWith("verify")) {
                        for (Member member : mentionedMembers) {
                            member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("verified")).forEach(role -> member.getGuild().addRoleToMember(member.getUser().getId(), role).queue());
                            received.getChannel().sendMessage(String.format("<@%s> прошел **верификацию**.", member.getId())).queue();
                        }
                    } else if (rawData.startsWith("unwarn")) {
                        for (Member member : mentionedMembers) {
                            member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("warn")).forEach(role -> member.getGuild().removeRoleFromMember(member.getUser().getId(), role).queue());
                            received.getChannel().sendMessage(String.format("У <@%s> сняты **все** варны.", member.getId())).queue();
                        }
                    } else if (rawData.startsWith("mute")) {
                        for (Member member : mentionedMembers) {
                            try {
                                received.getGuild().mute(member, true).queue(success -> received.getChannel().sendMessage("<@" + member.getId() + "> получил **мут**!").queue());
                            } catch (java.lang.IllegalStateException e) {
                                received.getChannel().sendMessage("Пользователю <@" + member.getId() + "> **невозможно** выдать мут. Пользователь не в голосовом канале.").queue();
                            }
                        }
                    } else if (rawData.startsWith("unmute")) {
                        for (Member member : mentionedMembers) {
                            try {
                                received.getGuild().mute(member, false).queue(success -> received.getChannel().sendMessage("<@" + member.getId() + "> получил **размут**!").queue());
                            } catch (java.lang.IllegalStateException e) {
                                received.getChannel().sendMessage("Пользователя <@" + member.getId() + "> **невозможно** размутить. Пользователь не в голосовом канале.").queue();
                            }
                        }
                    } else {
                        received.getChannel().sendMessage("**Неизвестная** команда!").queue();
                    }
                }
            }
        }

        private MessageEmbed sendEmbedMessage(String text) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription(text);
            builder.setColor(Color.decode(loadProperties.get(DiscordLoadProperties.MESSAGES_COLOR)));

            return builder.build();
        }
    }
}