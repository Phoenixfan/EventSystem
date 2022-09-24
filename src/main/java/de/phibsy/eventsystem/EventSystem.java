package de.phibsy.eventsystem;

import de.phibsy.eventsystem.event.EventManager;
import de.phibsy.eventsystem.listener.ChatListener;
import de.phibsy.eventsystem.listener.PlayerDisconnectListener;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import de.phibsy.eventsystem.command.EventCommand;
import de.phibsy.eventsystem.listener.ServerSwitchListener;
import net.md_5.bungee.api.plugin.PluginManager;

public class EventSystem extends Plugin {

    private static EventSystem instance;
    private EventManager eventManager;

    public static final String PREFIX = "§8» §dEvent §8┃ §7";

    @Override
    public void onEnable() {
        instance = this;
        eventManager = new EventManager();

        PluginManager pluginManager = getProxy().getPluginManager();

        //pluginManager.registerListener(this, new ServerSwitchListener());
        pluginManager.registerListener(this, new PlayerDisconnectListener());
        pluginManager.registerListener(this, new ChatListener());

        pluginManager.registerCommand(this, new EventCommand(eventManager));
    }

    public EventManager getPartyManager() {
        return eventManager;
    }

    public ServerInfo getServerByName(String name) {
        return getProxy().getServers().containsKey(name) ? getProxy().getServerInfo(name) : null;
    }

    public static EventSystem getInstance() {
        return instance;
    }

}
