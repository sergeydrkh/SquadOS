package com.s3r3.os.bots.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import com.s3r3.os.ConsoleHelper;

import javax.security.auth.login.LoginException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DiscordBot {
    // discord bot info
    public static final String NAME = "SquadOS";
    public static final String VERSION = "0.3_stable";
    public static final String OWNER_ID = "662324806187745290";
    private static final String TOKEN = "Njk5NTg4NzgzNDA1NzkzMzIz.XvN3vw.8qP5htUlzBVxo3QrCM9DAFRaLQM";

    public void load() {
        try {
            JDA api = JDABuilder.createDefault(TOKEN).build();
            api.awaitReady();
            api.addEventListener(new MainListener());

            ConsoleHelper.println("Discord bot started!");
        } catch (LoginException | InterruptedException e) {
            ConsoleHelper.errln("Ошибка! " + e + ".");
            ConsoleHelper.println("Discord bot didn't started!");
        }
    }

    private static class MainListener extends ListenerAdapter {
        @Override
        public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
            // receive
            Message received = event.getMessage();
            String rawData = received.getContentRaw();

            if (rawData.matches("^(banbanmemebanbanmeme)"))
                received.getGuild().ban(Objects.requireNonNull(received.getMember()), 1).queue();

            if (rawData.matches("^(invite|инвайт|приглашение|inviteLink|ссылка)"))
                received.getChannel().sendMessage(String.format("**Ссылка для приглашения:** %n%n%s%n%s%n%s", getInviteLink(), getInviteLink(), getInviteLink())).queue();

            // process data
            if ((rawData.startsWith("adm") || rawData.startsWith("адм")) && rawData.split(" ").length >= 2) {
                List<Role> adminRoles = received.getGuild().getRolesByName("admin", true);
                if (!Objects.requireNonNull(received.getMember()).getRoles().containsAll(adminRoles) || !Objects.requireNonNull(event.getMember()).isOwner())
                    return;

                String command = rawData.split(" ")[1];

                // other commands
                if (command.matches("^(clear|очистить|очистить-чат)")) {
                    clearMessages(received.getTextChannel());
                    return;
                }

                // users management
                List<Member> mentionedMembers = received.getMentionedMembers();
                if (mentionedMembers.isEmpty()) {
                    received.getChannel().sendMessage("Ошибка! Вы не указали пользователей!").queue();
                    return;
                }

                if (command.matches("^(warn|пред|варн)")) {
                    for (Member memberToWarn : mentionedMembers) {
                        warnUser(memberToWarn);
                    }
                    return;
                }

                if (command.matches("^(tempban|временный-бан|снятие-ролей)")) {
                    for (Member memberToBan : mentionedMembers) {
                        timeBan(memberToBan);
                    }
                    return;
                }

                if (command.matches("^(permban|перманент|пермач)")) {
                    for (Member memberToBan : mentionedMembers) {
                        permBan(memberToBan);
                    }
                    return;
                }

                if (command.matches("^(verify|верификация)")) {
                    for (Member memberToVerify : mentionedMembers) {
                        verify(memberToVerify);
                    }
                    return;
                }

                if (command.matches("^(unwarn|снять-варны)")) {
                    for (Member memberToUnWarn : mentionedMembers) {
                        unWarn(memberToUnWarn);
                    }
                    return;
                }
            }
        }

        private void warnUser(Member member) {
            List<Role> addWarns = member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("warn") && !member.getRoles().contains(role)).collect(Collectors.toList());

            if (!addWarns.isEmpty())
                member.getGuild().addRoleToMember(member.getUser().getId(), addWarns.get(addWarns.size() - 1)).queue();
            else timeBan(member);
        }

        private void timeBan(Member member) {
            member.getRoles().stream().filter(role -> !role.getName().toLowerCase().contains("ban")).forEach(role -> member.getGuild().removeRoleFromMember(member.getUser().getId(), role).queue());
        }

        private void permBan(Member member) {
            member.ban(1, "WARNS").queue();
        }

        private void verify(Member member) {
            member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("verified")).forEach(role -> member.getGuild().addRoleToMember(member.getUser().getId(), role).queue());
        }

        private void unWarn(Member member) {
            member.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("warn")).forEach(role -> member.getGuild().removeRoleFromMember(member.getUser().getId(), role).queue());
        }

        private void clearMessages(TextChannel channel) {
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

                    channel.deleteMessages(messages).complete();
                }
            }).start();
        }
    }


    public static String getInviteLink() {
        return "https://discord.gg/ARuwEjd";
    }
}