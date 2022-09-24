package de.phibsy.eventsystem.command;

import de.phibsy.eventsystem.EventSystem;
import de.phibsy.eventsystem.event.Event;
import de.phibsy.eventsystem.event.EventManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class EventCommand extends Command {

    private final EventManager eventManager;

    public EventCommand(EventManager eventManager) {
        super("event", null, "e");

        this.eventManager = eventManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(EventSystem.PREFIX + "§cOnly available to players!"));

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(args.length > 0) {
            Event event = eventManager.getEventByPlayer(player);

            if(args.length > 1) {
                ProxiedPlayer secondPlayer = ProxyServer.getInstance().getPlayer(args[1]);
                Event eventToJoin = eventManager.getEventByName(args[1]);

                switch (args[0].toUpperCase()) {
                    case "CREATE":
                        if(!player.hasPermission("Minecap.admin")) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + " §cNo permission to use that command!"));
                            return;
                        }
                        if(args.length < 3) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + " §cNot enough args!"));
                            return;
                        }
                        int n = 0;
                        try {
                            n = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + " §cSize is not a number!"));
                            return;
                        }
                        eventManager.createEvent(player, args[1], n);
                        return;
                    case "JOIN":
                        if(event != null) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cYou are already in a event! Leave with §7/event leave"));

                            return;
                        }


                        if(eventToJoin == null) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cEvent with this name does not exist!"));

                            return;
                        }

                        eventToJoin.addPlayer(player);


                        return;
                    case "KICK":
                        if(event == null) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cYou are in no event!"));

                            return;
                        }

                        if(!player.hasPermission("Minecap.admin")) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + " §cNo permission to use that command!"));
                            return;
                        }

                        if(!event.getPlayers().contains(secondPlayer)) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cThis player is not part of the event!"));

                            return;
                        }

                        if(secondPlayer.getName().equals("Phibsy")) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cdon't do bad choices!"));
                            return;
                        }

                        event.removePlayer(secondPlayer);

                        return;
                    case "PROMOTE":
                        if(event == null) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cYou are in no event!"));

                            return;
                        }

                        if(!event.getLeader().equals(player) || !player.hasPermission("Minecap.admin")) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cNo permission to use that command!"));

                            return;
                        }

                        if(!event.getPlayers().contains(secondPlayer)) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cThe player is not part of the event!"));

                            return;
                        }

                        event.setLeader(secondPlayer);
                        return;

                    case "SEND":
                        if(!player.hasPermission("Minecap.admin")) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + " §cNo permission to use that command!"));
                            return;
                        }
                        ServerInfo serverInfo = EventSystem.getInstance().getServerByName(args[1]);
                        if(serverInfo == null) {
                            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cServer does not exist!"));
                            return;
                        }
                        for(ProxiedPlayer playerMember : event.getPlayers()) {
                            playerMember.connect(serverInfo);
                        }
                        return;
                }
            }

            switch (args[0].toUpperCase()) {
                case "LIST":
                    if(event == null) {
                        player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cYou are in no event!"));

                        return;
                    }

                    StringBuilder builder = new StringBuilder();
                    builder.append(EventSystem.PREFIX + "§dMembers of the event:\n");

                    for(int i = 0; i < event.getPlayers().size(); i++) {
                        builder.append(EventSystem.PREFIX + "§d- §7" + event.getPlayers().get(i).getName());

                        if(i == event.getPlayers().size() - 1) {
                            break;
                        }

                        builder.append("\n");
                    }

                    player.sendMessage(new TextComponent(builder.toString()));

                    return;
                case "LEAVE":
                    if(event == null) {
                        player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cYou are in no event!"));

                        return;
                    }

                    event.removePlayer(player);
                    return;
                case "DELETE":
                    if(event == null) {
                        player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cYou are in no event!"));

                        return;
                    }
                    if(!player.hasPermission("Minecap.admin")) {
                        player.sendMessage(new TextComponent(EventSystem.PREFIX + " §cNo permission to use that command!"));
                        return;
                    }
                    for(ProxiedPlayer playerMember : event.getPlayers()) {
                        playerMember.sendMessage(new TextComponent(EventSystem.PREFIX + " §eNThe event has ended, thanks for playing!"));
                    }
                    eventManager.deleteEvent(event);
            }
        }

        if(player.hasPermission("Minecap.admin")) {
            player.sendMessage(new TextComponent(
                    EventSystem.PREFIX + "§d/event help §8× §7lists all commands\n" +
                    EventSystem.PREFIX + "§d/event create <Name> <Size> §8× §7create a event\n" +
                    EventSystem.PREFIX + "§d/event join <Name> §8× §7join a event\n" +
                    EventSystem.PREFIX + "§d/event kick <Player> §8× §7kick a player\n" +
                    EventSystem.PREFIX + "§d/event send <ServerName> §8× §7send players to a server\n" +
                    EventSystem.PREFIX + "§d/event list §8× §7lists all players in the event\n" +
                    EventSystem.PREFIX + "§d/event leave §8× §7leave the event\n" +
                    EventSystem.PREFIX + "§d/event delete §8× §7end & delete the event\n" +
                    EventSystem.PREFIX + "§d#e <Message> §8× §7send a message in the event chat"));
        } else {
            player.sendMessage(new TextComponent(
                    EventSystem.PREFIX + "§d/event help §8× §7lists all commands\n" +
                    EventSystem.PREFIX + "§d/event join <Name> §8× §7join a event\n" +
                    EventSystem.PREFIX + "§d/event list §8× §7lists all players in the event\n" +
                    EventSystem.PREFIX + "§d/event leave §8× §7leave the event\n" +
                    EventSystem.PREFIX + "§d#e <Message> §8× §7send a message in the event chat"));
        }


    }

}
