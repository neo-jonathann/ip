public class Event extends Task {
    private final String timePeriod1;
    private final String timePeriod2;

    public Event(String description, String timePeriod1, String timePeriod2) {
        super(description);
        this.timePeriod1 = timePeriod1;
        this.timePeriod2 = timePeriod2;
    }

    public Event(String description, boolean isDone, String timePeriod1, String timePeriod2) {
        super(description, isDone);
        this.timePeriod1 = timePeriod1;
        this.timePeriod2 = timePeriod2;
    }

    public String getTimePeriod1() {
        return timePeriod1;
    }

    public String getTimePeriod2() {
        return timePeriod2;
    }

    @Override
    public String toString() {
        return "[E]" +  super.toString() + " (from: " + timePeriod1 + " to: " + timePeriod2 + ")";
    }
}
