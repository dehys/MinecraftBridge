package me.arijan221.mcb2;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Main extends JavaPlugin implements Listener {

    public static void main(String[] args){}

    private static JDA api;

    private String botToken = getConfig().getString("botToken");
    private String guildID = getConfig().getString("guildID");
    private String channelID = getConfig().getString("channelID");
    private String timezone = getConfig().getString("timezone");
    private String messageFormat = getConfig().getString("messageFormat");

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        try {
            api = new JDABuilder(AccountType.BOT).setToken(botToken).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
        Date date = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(timezone))).getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        String time = simpleDateFormat.format(date);

        String message = messageFormat
                .replace("%player", event.getPlayer().getName())
                .replace("%message", event.getMessage())
                .replace("%currenttime", time);

        api.getGuildById(guildID).getTextChannelById(channelID).sendMessage(message).queue();
    }

}
