package de.phibsy.eventsystem.listener;

import de.phibsy.eventsystem.EventSystem;
import de.phibsy.eventsystem.event.Event;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Event party = EventSystem.getInstance().getPartyManager().getEventByPlayer(player);
        ServerInfo server = player.getServer().getInfo();

        if(party == null || !party.getLeader().equals(player) || server.getName().toLowerCase().contains("lobby")) {
            return;
        }

        for(ProxiedPlayer players : party.getPlayers()) {
            if(players.getServer().getInfo().equals(server)) {
                continue;
            }

            players.connect(player.getServer().getInfo());
        }
    }

}
