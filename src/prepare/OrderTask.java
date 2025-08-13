package prepare;

import com.google.inject.Inject;
import config.*;

public class OrderTask {
    private final Domain domain;
    @Inject
    public OrderTask(Domain domain) {
        this.domain = domain;
    }
    public static String currTask = "BEANS";
    public static String currSubtask = "BEANS";
    public static String hostName = "null";


    public static String getTask() {
        return currTask;
    }
    public static void setTask(String a) {currTask = a;}
    public static String getSubtask() {
        return currSubtask;
    }
    public static void setSubtask(String a) {currSubtask = a;}
    public static void setHostName(String a) {hostName = a;}
    public static String getHostName() {return hostName;}
}