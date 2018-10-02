package link.servr.manager.spigot;

import link.servr.manager.core.Core;
import link.servr.manager.core.Generator;
import link.servr.manager.core.ServrLink;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class SpigotServrLink extends JavaPlugin implements ServrLink {

    private Core core;

    @Override
    public void onEnable() {
        core = new Core(this);
    }

    @Override
    public File getDirectory() {
        return this.getDataFolder();
    }

    @Override
    public InputStream getResource(String name) {
        return this.getClassLoader().getResourceAsStream(name);
    }

    @Override
    public void log(String message) {
        Bukkit.getLogger().info("[ServrLink] " + message);
    }

    @Override
    public void disable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void runAsync(Runnable runnable) {
        getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }

    @Override
    public String translateColour(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("link")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command");
                return true;
            }

            Player p = (Player) sender;

            Generator generator = new Generator();
            generator.register(p.getUniqueId());
            URL url = generator.getURL();

            BaseComponent[] components = new ComponentBuilder(translateColour(core.configManager.getGenericOrNull("lang.message")))
                    .event(new ClickEvent(ClickEvent.Action.OPEN_URL, url.toString()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here").color(net.md_5.bungee.api.ChatColor.GREEN).create()))
                    .create();

            p.spigot().sendMessage(components);
        }

        return true;
    }
}
