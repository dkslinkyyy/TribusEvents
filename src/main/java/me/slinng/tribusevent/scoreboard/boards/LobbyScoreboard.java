package me.slinng.tribusevent.scoreboard.boards;



import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.scoreboard.ScoreboardFactory;

import java.util.Collections;
import java.util.List;

public class LobbyScoreboard  extends ScoreboardFactory {


    public LobbyScoreboard(Core core) {
        super("Title", "Footer");
        /*
        List<String> lines = core.getConfigManager().getConfigFile().mainLines;
        String header = core.trans(core.getConfigManager().getConfigFile().mainHeader);
        String footer = core.trans(core.getConfigManager().getConfigFile().mainFooter);

        setTitle(header);
        Collections.reverse(lines);
        addLine("%players%");
        lines.forEach(l -> addLine(core.trans(l)));
        setFooter(footer);


         */

    }
}
