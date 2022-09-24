package de.phibsy.eventsystem.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {

    private final List<Event> events;

    public EventManager() {
        events = new CopyOnWriteArrayList<>();
    }

    public Event createEvent(ProxiedPlayer leader, String name, int size) {
        Event event = new Event(leader, name, size);
        events.add(event);
        return event;
    }

    public void deleteEvent(Event event) {
        events.remove(event);
    }

    public Event getEventByPlayer(ProxiedPlayer player) {
        for(Event event : events) {
            if(!event.getPlayers().contains(player)) {
                continue;
            }

            return event;
        }

        return null;
    }

    public Event getEventByName(String name) {
        for(Event event : events) {
            if(event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }

}
