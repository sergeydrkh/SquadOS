package app.os.discord.callback.arguments.decision.commands;

import app.os.discord.DiscordBot;
import app.os.discord.callback.CallbackEvent;
import app.os.discord.callback.arguments.decision.CallbackDecision;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public class CWarn extends CallbackDecision {
    public CWarn() {
        super("warn", "выдать варн пользователю", "");
    }

    @Override
    public void execute(CallbackEvent event) {
        Member member = event.getGuild().getMemberById(event.getArgs().getProperty("member_id"));
        if (member == null)
            return;

        List<Role> serverWarnRoles = event.getGuild().getRoles()
                .stream()
                .filter(role -> role.getName().equalsIgnoreCase(DiscordBot.WARN_ROLES))
                .collect(Collectors.toList());

        List<Role> memberRoles = member.getRoles()
                .stream()
                .filter(role -> role.getName().equalsIgnoreCase(DiscordBot.WARN_ROLES))
                .collect(Collectors.toList());

        serverWarnRoles.removeAll(memberRoles);
        if (serverWarnRoles.isEmpty()) {
            new CBan().execute(event);
        } else {
            event.getGuild().addRoleToMember(member.getId(), serverWarnRoles.get(serverWarnRoles.size() - 1))
                    .queue(success -> event.getGuild().getDefaultChannel().sendMessage(String.format("<@%s> выдан **варн**!", member.getId())).queue());
        }
    }
}
