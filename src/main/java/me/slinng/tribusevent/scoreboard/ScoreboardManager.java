package me.slinng.tribusevent.scoreboard;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.scoreboard.boards.InGameScoreboard;
import me.slinng.tribusevent.scoreboard.boards.LobbyScoreboard;
import me.slinng.tribusevent.scoreboard.boards.WaitingLobbyScoreboard;


public class ScoreboardManager {

    private InGameScoreboard inGameScoreboard;
    private WaitingLobbyScoreboard waitingLobbyScoreboard;
    private LobbyScoreboard lobbyScoreboard;


    public ScoreboardManager(Core core) {
        this.inGameScoreboard = new InGameScoreboard(core);
        this.waitingLobbyScoreboard = new WaitingLobbyScoreboard(core);
        this.lobbyScoreboard = new LobbyScoreboard(core);
    }

    public InGameScoreboard getInGameScoreboard() {
        return inGameScoreboard;
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return lobbyScoreboard;
    }

    public WaitingLobbyScoreboard getWaitingLobbyScoreboard() {
        return waitingLobbyScoreboard;
    }
}
