package me.slinng.tribusevent.commands.sub;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.event.Event;
import me.slinng.tribusevent.event.EventState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinEventCommand extends SubCommand{
    public JoinEventCommand() {
        super("events", "join", "", "");
    }

    @Override
    public void executeSub(CommandSender s, String[] args) {

        Event event = Core.i.getCurrentPlayedEvent();

        if(event == null) {
            s.sendMessage("&6&lTribusMC &8» &cInget event körs för tillfället.");
            return;
        }

        if(event.hasJoined((Player) s)) {
            s.sendMessage("&6&lTribusMC &8» &7Du är redan med i &a" + event + " &7eventet.");
            return;
        }
        if(event.isState(EventState.RUNNING)) {
            s.sendMessage("&6&lTribusMC &8» &7Ett event körs redan, du får vänta tills nästa omgång.");
            return;
        }
        event.join((Player) s);
    }
}
