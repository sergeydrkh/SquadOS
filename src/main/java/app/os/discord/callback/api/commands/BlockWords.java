package app.os.discord.callback.api.commands;

import app.os.discord.callback.Callback;
import net.dv8tion.jda.api.entities.Guild;

public class BlockWords extends CallbackCommand {
    public BlockWords() {
        this.name = "blockWords";
        this.help = "запретить использовать помеченные слова";
        this.args = "{word1,word2,word3}";
    }

    @Override
    public void execute(Callback callback, Guild guild) {

    }
}
