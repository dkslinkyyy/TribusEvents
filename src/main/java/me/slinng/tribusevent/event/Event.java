package me.slinng.tribusevent.event;

import com.cryptomorin.xseries.XPotion;
import com.google.common.base.Optional;
import me.jasperjh.animatedscoreboard.AnimatedScoreboard;
import me.jasperjh.animatedscoreboard.AnimatedScoreboardAPI;
import me.jasperjh.animatedscoreboard.config.PlayerScoreboardFile;
import me.jasperjh.animatedscoreboard.objects.PlayerScoreboardTemplate;
import me.jasperjh.animatedscoreboard.objects.ScoreboardPlayer;
import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.event.ePlayer.EPlayer;
import me.slinng.tribusevent.event.ePlayer.EPlayerManager;
import me.slinng.tribusevent.objects.storage.MapStorage;
import me.slinng.tribusevent.objects.PlayableMap;
import me.slinng.tribusevent.miscelleanous.timer.CheckTimer;
import me.slinng.tribusevent.miscelleanous.timer.TimerType;
import me.slinng.tribusevent.placeholders.PlaceholderImpl;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Event implements Listener {

    private final int ID;

    protected String eventName;
    private int minimumPlayers, maximumPlayers;
    private EventState state;
    private EventOccasion eventOccasion;
    protected final EPlayerManager eventPlayerManager;
    private boolean check = true;
    private int alive = 0;
    private CheckTimer timerOnStart, timerOnSearch;


    protected PlayableMap playableMap;


    public Event(String eventName, int minimumPlayers, int maximumPlayers, EventState eventState, EventOccasion eventOccasion) {
        this.ID = (int) Math.ceil(Math.random() * 1000) + 2000;

        this.eventName = eventName;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
        this.state = eventState;
        this.eventOccasion = eventOccasion;


        this.eventPlayerManager = new EPlayerManager();

        this.timerOnStart = new CheckTimer(TimerType.REPEATABLE, 5);
        this.timerOnSearch = new CheckTimer(TimerType.CHECK, 20);

        try {
            Core.i.registerPlaceholderOwner(this);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        Core.i.getPlaceholderCollector().addPlaceholder(eventName, new PlaceholderImpl[]{
                new PlaceholderImpl() {
                    @Override
                    public String getPlaceholder() {
                        return "name";
                    }

                    @Override
                    public String getResult(Player p) {
                        return eventName;
                    }
                },
                new PlaceholderImpl() {
                    @Override
                    public String getPlaceholder() {
                        return "total";
                    }

                    @Override
                    public String getResult(Player p) {
                        return String.valueOf(getMaximumPlayers());
                    }
                },
                new PlaceholderImpl() {
                    @Override
                    public String getPlaceholder() {
                        return "totalingame";
                    }

                    @Override
                    public String getResult(Player p) {
                        return String.valueOf(getPlayersInEvent());
                    }
                },
                new PlaceholderImpl() {
                    @Override
                    public String getPlaceholder() {
                        return "status";
                    }

                    @Override
                    public String getResult(Player p) {
                        return String.valueOf(state.getText());
                    }
                },
                new PlaceholderImpl() {
                    @Override
                    public String getPlaceholder() {
                        return "timeuntilstart";
                    }

                    @Override
                    public String getResult(Player p) {
                        return timerOnSearch.getTime() + "s";
                    }
                }
            }
        );


        Core.i.getPlaceholderCollector().addPlaceholder(eventName, getPlaceholders());




        Core.i.getServer().getPluginManager().registerEvents(this, Core.i);
    }


    protected abstract void onJoin(Player p);

    protected abstract void onStart(List<EPlayer> ePlayers);

    protected abstract void onFinish(List<EPlayer> ePlayers);

    protected abstract void onPlayerDeath(PlayerDeathEvent e, Player p, Player killer, List<EPlayer> ePlayers);

    protected abstract void onPlayerDamageEvent(EntityDamageEvent e, Player p);

    protected abstract void onPlayerDamagePlayer(EntityDamageByEntityEvent e, Player p);

    protected abstract PlaceholderImpl[] getPlaceholders();

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        onPlayerDamagePlayer(e, (Player) e.getEntity());
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

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
        return this.state != state;
    }

    public EventState getState() {
        return state;
    }


    //Players

    public void join(Player player) {
        if (isState(EventState.WAITING)) return;
        if (hasJoined(player)) return;
        if (isFull()) return;

        Core.i.getTextUtil().sendToAll("&6&lTribusMC &8» &a" + player.getName() + " &7gick med i LMS eventet.");


        Core.i.switchScoreboard(player, "EVENT_board");

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
            Core.i.switchScoreboard(o.getBukkitPlayer(), eventName.toUpperCase() + "_board");


            XPotion speed = XPotion.SPEED;

            o.getBukkitPlayer().addPotionEffect(speed.parsePotion(35000, 1));

            Random r = new Random();
            o.getBukkitPlayer().teleport(playableMap.getSpawnLocations().get(r.nextInt(playableMap.getSpawnLocations().size())));
        });

        alive = eventPlayerManager.getEventTotalPlayers();


        setState(EventState.STARTING);

        timerOnStart.execute(() -> {
            getPlayers().forEach(o -> {
                if (timerOnStart.getTime() > 0)
                    Core.i.getTextUtil().sendTitleMessage(countdownColorCodes()[timerOnStart.getTime() == 0 ? timerOnStart.getTime() : timerOnStart.getTime()-1] + timerOnStart.getTime(), 5, 5, 5, getBukkitPlayer(o));
                getBukkitPlayer(o).playSound(o.getBukkitPlayer().getLocation(), Sound.CLICK, 1.3f, 1f);
            });

        }).whenFinished(() -> {
            getPlayers().forEach(o -> o.getBukkitPlayer().playSound(o.getBukkitPlayer().getLocation(), Sound.NOTE_PLING, 1.3f, 1f));
            setState(EventState.RUNNING);
        });

    }



    private String[] countdownColorCodes() {
        return new String[] {
                "&4",
                "&c",
                "&e",
                "&a",
                "&2"
        };
    }

    private void reset() {
        check = false;
    }

    public void finish() {
        onFinish(eventPlayerManager.getEventPlayers());
        eventPlayerManager.getEventPlayers().forEach(e -> Core.i.switchScoreboard(e.getBukkitPlayer(), "defaultscoreboard"));
        eventPlayerManager.getEventPlayers().clear();
        setState(EventState.UNREACHABLE);
    }

    public void beginSearchForPlayers() {
        if (!MapStorage.exists(eventName)) {
            reset();
            Core.i.getLogger().warning("Could not start " + eventName + " due to no playable maps avaialable.");
            return;
        }


        Core.i.setCurrentEvent(this);

        playableMap = MapStorage.fetch(eventName);

        Core.i.getTextUtil().sendToAll("&6&lTribusMC &8» &a&lEtt " + eventName + " event börjar snart! &7Skriv &e/event join &7för att delta!\n");

        setState(EventState.WAITING);

        timerOnSearch.execute(this::start);

    }

    public boolean canStart() {
        return getPlayersInEvent() > minimumPlayers;
    }


}
