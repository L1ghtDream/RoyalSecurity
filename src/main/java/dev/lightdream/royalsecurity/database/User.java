package dev.lightdream.royalsecurity.database;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.royalsecurity.Main;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@DatabaseTable(table = "users")
public class User extends DatabaseEntry {

    @DatabaseField(columnName = "uuid", unique = true)
    public UUID uuid;
    @DatabaseField(columnName = "name", unique = true)
    public String name;
    @DatabaseField(columnName = "discord_id")
    public Long discordID;
    @DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "auto_connect")
    public boolean autoConnect;


    public User(UUID uuid, String name) {
        super(Main.instance);
        this.name = name;
        this.uuid = uuid;
        this.discordID = null;
        this.ip = "";
        this.autoConnect = false;
    }

    public User() {
        super(Main.instance);
    }

    public boolean hasSecurity() {
        return discordID != null;
    }

    public void sendAuth(String ip) {
        if (discordID == null) {
            return;
        }

        Main.instance.bot.retrieveUserById(discordID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> Main.instance.jdaConfig.auth.clone()
                                .parse("player_name", name)
                                .parse("ip", ip)
                                .parse("date", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()))
                                .buildMessageAction(channel)
                                .queue()), Throwable::printStackTrace);
    }

    public void sendSecure(MessageChannel originChannel, String code, Long discordID) {
        Main.instance.bot.retrieveUserById(discordID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> channel.sendMessageEmbeds(Main.instance.jdaConfig.secure.parse("code", code).build().build())
                                .queue(null,
                                        new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                                e -> originChannel.sendMessageEmbeds(Main.instance.jdaConfig.cannotSendMessage.build().build())
                                                        .queue()))));
    }

    public void setDiscordID(Long id) {
        this.discordID = id;
        save();
    }

    public void setIP(String ip) {
        this.ip = ip;
        save();
    }

    public void unlink() {
        setDiscordID(null);
    }

    public void changeAutoConnect() {
        autoConnect = !autoConnect;
        save();
    }

    public boolean autoConnect() {
        return autoConnect;
    }

}
