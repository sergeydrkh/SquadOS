package app.os.discord.callback.arguments.decision.commands;

import app.os.discord.callback.CallbackEvent;
import app.os.discord.callback.arguments.decision.CallbackDecision;
import net.dv8tion.jda.api.entities.Member;

public class CBan extends CallbackDecision {
    public CBan() {
        super("ban", "выдать бан пользователю", "");
    }

    @Override
    public void execute(CallbackEvent event) {
        Member member = event.getGuild().getMemberById(event.getArgs().getProperty("member_id"));
        if (member == null)
            return;

        member.getRoles()
                .forEach(role -> event.getGuild().removeRoleFromMember(member.getId(), role).queue());

        event.getGuild().getDefaultChannel().sendMessage(String.format("<@%s> выдан **бан**! *(снятие ролей)*", member.getId())).queue();
    }
}
