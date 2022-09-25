package de.phibsy.eventsystem.event;

import de.phibsy.eventsystem.EventSystem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Event {

    private ProxiedPlayer leader;
    private String name;
    private int size;
    private final List<ProxiedPlayer> players;


    public Event(ProxiedPlayer leader) {
        this(leader, "newEvent");
    }
    public Event(ProxiedPlayer leader, String name) {
        this(leader, name, 64);
    }

    public Event(ProxiedPlayer leader, String name, int size) {
        this.leader = leader;
        this.name = name;
        this.size = size;

        players = new CopyOnWriteArrayList<>();

        players.add(leader);
    }

    public void sendMessage(String message) {
        for(ProxiedPlayer player : players) {
            player.sendMessage(new TextComponent(EventSystem.PREFIX + message));
        }
    }

    public void addPlayer(ProxiedPlayer player) {
        if(players.size() < size) {
            sendMessage(player.getName() + " §7has joined the event!");

            players.add(player);
        } else {
            player.sendMessage(new TextComponent(EventSystem.PREFIX + " §7Event is full"));
        }
    }

    public void removePlayer(ProxiedPlayer player) {
        if(players.size() < 2) {
            EventSystem.getInstance().getPartyManager().deleteEvent(this);

            player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cEvent has ended!"));

            return;
        }

        players.remove(player);
        player.sendMessage(new TextComponent(EventSystem.PREFIX + "§cYou left the event"));

        sendMessage(player.getName() + " §chas left the event!");

        if(leader.equals(player)) {
            int random = ThreadLocalRandom.current().nextInt(0, players.size());
            ProxiedPlayer newLeader = players.get(random);

            setLeader(newLeader);
        }
    }

    public List<ProxiedPlayer> getPlayers() {
        return players;
    }

    public String getName() {
        return this.name;
    }

    public void setLeader(ProxiedPlayer leader) {
        this.leader = leader;

        sendMessage("§dNew Event Host: §7" + leader.getName());
    }

    public ProxiedPlayer getLeader() {
        return leader;
    }
}
