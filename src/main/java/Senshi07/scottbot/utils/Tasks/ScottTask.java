package Senshi07.scottbot.utils.Tasks;

public abstract class ScottTask implements Runnable {

    private final String taskName;

    public ScottTask(String taskName, long delay, long interval) {
        this.taskName = taskName;
        repeat(delay, interval);
    }

    public boolean repeat(long delay, long interval) {
        return Scheduler.scheduleRepeating(this, taskName, delay, interval);
    }

    public boolean cancel() {
        return Scheduler.cancelTask(taskName);
    }
}
