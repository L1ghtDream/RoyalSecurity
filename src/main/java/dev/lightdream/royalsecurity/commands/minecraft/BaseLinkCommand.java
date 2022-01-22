package dev.lightdream.royalsecurity.commands.minecraft;

import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.databases.User;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.UserPair;

import java.util.List;

@dev.lightdream.api.annotations.commands.SubCommand(usage = "[code]",
        onlyForPlayers = true,
        parentCommand = "link")
public class BaseLinkCommand extends SubCommand {
    public BaseLinkCommand() {
        super(Main.instance);
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
            user.sendMessage(api, Main.instance.lang.invalidCode);
            return;
        }

        if (!pair.getUser().equals(user)) {
            user.sendMessage(api, Main.instance.lang.invalidCode);
            return;
        }

        pair.pair(user.getPlayer().getAddress().getHostName());
        user.sendMessage(api, Main.instance.lang.linked);
    }

    @Override
    public List<String> onTabComplete(User user, List<String> list) {
        return null;
    }
}
