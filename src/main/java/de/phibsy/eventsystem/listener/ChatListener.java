package de.phibsy.eventsystem.listener;

import de.phibsy.eventsystem.EventSystem;
import de.phibsy.eventsystem.event.Event;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent e) {
        if(!(e.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        String[] args = e.getMessage().split(" ");

        if(args.length < 2 || !args[0].equalsIgnoreCase("#e")) {
            return;
        }

        Event event = EventSystem.getInstance().getPartyManager().getEventByPlayer(player);

        if(event == null) {
            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cYou are not in the event!"));

            e.setCancelled(true);
            return;
        }

        StringBuilder builder = new StringBuilder();

        for(int i = 1; i < args.length; i++) {
            builder.append(args[i]);

            if(i == args.length - 1) {
                break;
            }

            builder.append(" ");
        }

        e.setCancelled(true);
        event.sendMessage(player.getName() + " §d-> §7" + builder.toString());
    }

}
