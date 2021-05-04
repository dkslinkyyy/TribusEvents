package me.slinng.tribusevent.listeners;

import com.google.common.base.Optional;
import me.jasperjh.animatedscoreboard.AnimatedScoreboard;
import me.jasperjh.animatedscoreboard.AnimatedScoreboardAPI;
import me.jasperjh.animatedscoreboard.config.PlayerScoreboardFile;
import me.jasperjh.animatedscoreboard.objects.PlayerScoreboard;
import me.jasperjh.animatedscoreboard.objects.PlayerScoreboardTemplate;
import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.miscelleanous.timer.CheckTimer;
import me.slinng.tribusevent.miscelleanous.timer.TimerType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Core.i.getEventController().getLMS().unDisguise(e.getPlayer());



    }



    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        AnimatedScoreboardAPI api = AnimatedScoreboard.loadAPI(Core.i);


        e.getPlayer().sendMessage("test");




        CheckTimer ct = new CheckTimer(TimerType.CHECK, 5);

        ct.execute(() -> {
            Optional<PlayerScoreboard> pScoreboard = api.getPlayerScoreboard(e.getPlayer().getUniqueId());
            if(!pScoreboard.isPresent()) {
                return;
            }
            PlayerScoreboard ps = pScoreboard.get();



            ps.getScoreboardPlayer().switchScoreboard(new PlayerScoreboardTemplate(new PlayerScoreboardFile(AnimatedScoreboard.getInstance(), "adminboard"), ""));
        });


    }




}
