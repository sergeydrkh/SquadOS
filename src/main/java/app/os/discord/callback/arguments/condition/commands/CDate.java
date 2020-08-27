package app.os.discord.callback.arguments.condition.commands;

import app.os.discord.callback.CallbackEvent;
import app.os.discord.callback.arguments.condition.CallbackCondition;
import app.os.main.OS;

import java.text.ParseException;
import java.util.Date;

public class CDate extends CallbackCondition {
    public CDate() {
        super("date", "выполнить действие по дате", "{dd.MM.yyyy hh:ss}");
    }

    @Override
    public boolean check(CallbackEvent event) {
        try {
            Date argsDate = OS.DEFAULT_DATE_FORMAT.parse(event.getArgs().getProperty("date"));
            Date nowDate = new Date();

            return nowDate.before(argsDate);
        } catch (ParseException e) {
            return false;
        }
    }
}
