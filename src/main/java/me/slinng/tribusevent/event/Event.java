package me.slinng.tribusevent.event;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.event.ePlayer.EPlayer;
import me.slinng.tribusevent.event.ePlayer.EPlayerManager;
import me.slinng.tribusevent.objects.storage.MapStorage;
import me.slinng.tribusevent.objects.PlayableMap;
import me.slinng.tribusevent.miscelleanous.timer.CheckTimer;
import me.slinng.tribusevent.miscelleanous.timer.TimerType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public abstract class Event implements Listener {

    private final int ID;

    protected String eventName;
    private int minimumPlayers, maximumPlayers;
    private EventState state;
    private EventOccasion eventOccasion;
    protected final EPlayerManager eventPlayerManager;
    private boolean check = true;
    private int alive = 0;

    protected PlayableMap playableMap;


    public Event(String eventName, int minimumPlayers, int maximumPlayers, EventState eventState, EventOccasion eventOccasion) {
        this.ID = (int) Math.ceil(Math.random() * 1000) + 2000;

        this.eventName = eventName;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
        this.state = eventState;
        this.eventOccasion = eventOccasion;


        this.eventPlayerManager = new EPlayerManager();

        Core.i.getServer().getPluginManager().registerEvents(this, Core.i);
    }


    protected abstract void onJoin(Player p);
    protected abstract void onStart(List<EPlayer> ePlayers);
    protected abstract void onFinish(EPlayer ep);

    protected abstract void onPlayerDeath(PlayerDeathEvent e, Player p, Player killer, List<EPlayer> EPlayers);
    protected abstract void onPlayerDamageEvent(EntityDamageEvent e, Player p);
    protected abstract void onPlayerDamagePlayer(EntityDamageByEntityEvent e, Player p);


    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        onPlayerDamagePlayer(e, (Player) e.getEntity());    }



    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        onPlayerDamageEvent(e, (Player) e.getEntity());

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        onPlayerDeath(e, e.getEntity(), e.getEntity().getKiller(), eventPlayerManager.getEventPlayers());
    }

    public int getID() {
        return ID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    //Minimum & Maximum players in a Event;

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    //EventState
    public void setState(EventState state) {
        this.state = state;
    }

    public boolean isState(EventState state) {
        return this.state == state;
    }

    public EventState getState() {
        return state;
    }


    //Players

    public void join(Player player) {
        if (!isState(EventState.WAITING)) return;
        if(hasJoined(player)) return;
        if (isFull()) return;

        Core.i.getTextUtil().sendToAll("&6&lTribusMC &8» &a" + player.getName() +" &7gick med i LMS eventet.");


        onJoin(player);
        eventPlayerManager.add(new EPlayer(player));

        player.teleport(playableMap.getLobbyLocation());

    }

    public boolean hasJoined(Player p) {
        return eventPlayerManager.contains(p);
    }

    public void leave(Player player) {
        eventPlayerManager.remove(eventPlayerManager.fetchByPlayer(player));
    }

    public int getPlayersInEvent() {
        return eventPlayerManager.getEventTotalPlayers();
    }

    public List<EPlayer> getPlayers() {
        return eventPlayerManager.getEventPlayers();
    }

    public boolean isFull() {
        return eventPlayerManager.getEventTotalPlayers() == maximumPlayers;
    }


    public EventOccasion getOccasion() {
        return eventOccasion;
    }

    public void setOccasion(EventOccasion eventOccasion) {
        this.eventOccasion = eventOccasion;
    }


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Player getBukkitPlayer(EPlayer ep) {
        return ep.getBukkitPlayer();
    }

    public void start() {
        if (!canStart()) {
            eventOccasion.update();
            //     Core.i.getTextUtil().sendDelayedMessage("Next " + eventName + " event is in: " + eventOccasion.getDuration(), 20*2);
            reset();
            System.out.println("Could not start " + eventName + ", event due to few players in game.");
            Core.i.getTextUtil().sendToAll("&6&lTribusMC &8» &cInga spelare anslöt till eventet, event avbrytet.");
            return;
        }

        onStart(eventPlayerManager.getEventPlayers());

        eventPlayerManager.getEventPlayers().forEach(o -> {
            Random r = new Random();
            o.getBukkitPlayer().teleport(playableMap.getSpawnLocations().get(r.nextInt(playableMap.getSpawnLocations().size())));
        });

        alive = eventPlayerManager.getEventTotalPlayers();

        CheckTimer checkTimer = new CheckTimer(TimerType.REPEATABLE, 10);

        setState(EventState.STARTING);

        checkTimer.execute(() -> {
                getPlayers().forEach(o -> {
                    if(checkTimer.getTime() > 0) Core.i.getTextUtil().sendTitleMessage("&e&l" + checkTimer.getTime(), 5, 5, 5, getBukkitPlayer(o));
                    getBukkitPlayer(o).playSound(o.getBukkitPlayer().getLocation(), Sound.CLICK, 1.3f, 1);
                });

        }).whenFinished(() -> {
            getPlayers().forEach(o -> o.getBukkitPlayer().playSound(o.getBukkitPlayer().getLocation(), Sound.NOTE_PLING, 1.3f, 1));
            setState(EventState.RUNNING);
        });

    }

    private void reset() {
        check = false;
    }

    public void finish() {
        eventPlayerManager.getEventPlayers().clear();
        setState(EventState.UNREACHABLE);
    }

    public void beginSearchForPlayers() {
        if (!MapStorage.exists(eventName)) {
            reset();
            Core.i.getLogger().warning("Could not start " + eventName + " due to no playable maps avaialable.");
        }

        playableMap = MapStorage.fetch(eventName);

        Core.i.getTextUtil().sendToAll("&6&lTribusMC &8» &a&lEtt " + eventName + " event börjar snart! &7Skriv &e/event join &7för att delta!\n");

        setState(EventState.WAITING);

        CheckTimer ct = new CheckTimer(TimerType.CHECK, 20);
        ct.execute(this::start);

    }

    public boolean canStart() {
        return getPlayersInEvent() > minimumPlayers;
    }


}
