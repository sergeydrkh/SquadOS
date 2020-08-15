package app.os.bots.discord;

import app.os.ConsoleHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DiscordBot {
    public static final String VERSION = "0.3.4_stable";
    private final Map<DiscordProperties, String> loadProp;

    public DiscordBot(Map<DiscordProperties, String> loadProp) {
        this.loadProp = loadProp;
    }

    public void launch() {
        try {
            JDA api = JDABuilder.createDefault(loadProp.get(DiscordProperties.BOT_TOKEN)).build();
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
            // receive
            Message received = event.getMessage();
            String rawData = received.getContentRaw();
            String[] args = rawData.split(" ");

            try {
                if (!rawData.startsWith(loadProp.get(DiscordProperties.BOT_PREFIX)) || !rawData.startsWith(loadProp.get(DiscordProperties.BOT_ADDITIONAL_PREFIX)))
                    return;
            } catch (NullPointerException e) {
                ConsoleHelper.errln("No prefix founded in config file!");
            }

            if (rawData.contains("link"))
                received.getChannel().sendMessage(String.format("**Ссылка для приглашения:** %n%n%s%n%s%n%s", getInviteLink(), getInviteLink(), getInviteLink())).queue();

            // process data
            List<Role> adminRoles = received.getGuild().getRolesByName("admin", true);
            if (!Objects.requireNonNull(received.getMember()).getRoles().containsAll(adminRoles))
                return;

            String command = args[0];

            // other commands
            if (command.contains("clear")) {
                Commands.AdminCommands.clearMessages(received.getTextChannel());
                return;
            }


            // users management
            List<Member> mentionedMembers = received.getMentionedMembers();
            if (!mentionedMembers.isEmpty()) {
                if (command.contains("warn")) {
                    for (Member memberToWarn : mentionedMembers) {
                        Commands.AdminCommands.warnUser(memberToWarn);
                        received.getChannel().sendMessage(String.format("<@%s> получил **варн**!", memberToWarn.getId())).queue();
                    }
                } else if (command.contains("ban")) {
                    for (Member memberToTimeBan : mentionedMembers) {
                        Commands.AdminCommands.timeBan(memberToTimeBan);
                        received.getChannel().sendMessage(String.format("<@%s> получил **временный** бан! (снятие ролей)", memberToTimeBan.getId())).queue();
                    }
                } else if (command.contains("permban")) {
                    for (Member memberToPermBan : mentionedMembers) {
                        Commands.AdminCommands.permBan(memberToPermBan);
                        received.getChannel().sendMessage(String.format("<@%s> получил **перманентный** бан!", memberToPermBan.getId())).queue();
                    }
                } else if (command.contains("verify")) {
                    for (Member memberToVerify : mentionedMembers) {
                        Commands.AdminCommands.verify(memberToVerify);
                        received.getChannel().sendMessage(String.format("<@%s> прошел **верефикацию**.", memberToVerify.getId())).queue();
                    }
                } else if (command.contains("unwarn")) {
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

    private static class Commands {
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


    public static String getInviteLink() {
        return "https://discord.gg/ARuwEjd";
    }
}