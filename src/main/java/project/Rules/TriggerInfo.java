package project.Rules;

import java.util.ArrayList;
import java.util.List;

public class TriggerInfo {
    public String objName;
    public List<String> triggerEvents;
    public String helperName;

    public TriggerInfo(String objName, List<String> triggerEvents, String helperName) {
        this.objName = objName;
        this.helperName = helperName;
        this.triggerEvents = new ArrayList<>();
        this.triggerEvents.addAll(triggerEvents);
    }
}
