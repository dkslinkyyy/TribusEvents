package me.slinng.tribusevent.miscelleanous.timer;

import me.slinng.tribusevent.Core;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckTimer {

    private TimerType timerType;

    private int time;

    private RunnableCode whenFinished;

    public CheckTimer(TimerType timerType, int time) {
        this.timerType = timerType;
        this.time = time;

    }


    public CheckTimer whenFinished(RunnableCode whenFinished) {
        this.whenFinished = whenFinished;
        return this;
    }
    public int getTime() {
        return time;
    }

    public CheckTimer execute(RunnableCode runnableCode) {
        switch (timerType) {
            case CHECK:
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(time == 0) {
                            runnableCode.run();
                            this.cancel();
                        }
                        time--;
                    }
                }.runTaskTimer(Core.i, 0,20L);
                break;
            case REPEATABLE:
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(time == 0) {
                            if(whenFinished != null) {
                                whenFinished.run();
                            }
                            this.cancel();
                        }
                        runnableCode.run();
                        time--;
                    }
                }.runTaskTimer(Core.i, 0,20L);
                break;
        }

        return this;
    }
}
