package fun.milkyway.milkybandwidthsaver;

public class SavingsSettings {
    private int viewDistance;

    public SavingsSettings(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public synchronized int getViewDistance() {
        return viewDistance;
    }

    public synchronized void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }
}
