package dev.lightdream.royalsecurity.commands.minecraft.link;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.BaseCommand;
import dev.lightdream.api.databases.User;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.UserPair;

import java.util.List;

@dev.lightdream.api.annotations.commands.Command(
        command = "link"
)
public class LinkCommand extends BaseCommand {
    public LinkCommand(IAPI api) {
        super(api);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(User user, List<String> args) {
        if (args.size() != 1) {
            sendUsage(user);
            return;
        }

        UserPair pair = Main.instance.databaseManager.getUserPair(args.get(0));

        if (pair == null) {
            user.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        if (!pair.getUser().equals(user)) {
            user.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        pair.pair(user.getPlayer().getAddress().getHostName());
        user.sendMessage(Main.instance.lang.linked);
    }

    @Override
    public List<String> onTabComplete(User user, List<String> list) {
        return null;
    }
}