package de.phibsy.eventsystem.listener;

import de.phibsy.eventsystem.EventSystem;
import de.phibsy.eventsystem.event.Event;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        Event event = EventSystem.getInstance().getPartyManager().getEventByPlayer(player);

        if(event == null) {
            return;
        }

        event.removePlayer(player);
    }

}
