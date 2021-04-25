package me.slinng.tribusevent.scoreboard.boards;


import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.scoreboard.ScoreboardFactory;



public class WaitingLobbyScoreboard extends ScoreboardFactory {

    public WaitingLobbyScoreboard(Core core) {
        super("test", "test");

        /*
        List<String> lines = core.getConfigManager().getConfigFile().wLobbyLines;
        String header = core.trans(core.getConfigManager().getConfigFile().wLobbyHeader);
        String footer = core.trans(core.getConfigManager().getConfigFile().wLobbyFooter);

        setTitle(header);
        Collections.reverse(lines);
        lines.forEach(l -> addLine(core.trans(l)));
        setFooter(footer);

         */
    }
}
