// Guy Buky 208209817
// Bar Weizman 206492449

package Simulation;

public class Clock {
    private static long time = 0;
    private static int ticks_per_day = 1;

    // adding a tick and returns the current time
    public static long now(){
        nextTick();
        return time;
    }

    // incrementing time
    public static void nextTick(){
        time++;
    }

    public static int getTimeDifference(int ticks){
        return Math.round(ticks / ticks_per_day);
    }

    public static void setTicksPerDay(int val){
        ticks_per_day = val;
    }

    public static int getTicksPerDay(){
        return ticks_per_day;
    }
}
