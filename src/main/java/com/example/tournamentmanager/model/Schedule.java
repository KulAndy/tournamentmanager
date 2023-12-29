package com.example.tournamentmanager.model;

import com.google.gson.*;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Schedule extends ArrayList<Schedule.ScheduleElement> implements Serializable {
    private ScheduleElement briefing;
    private ScheduleElement closing;

    public Schedule() {
        setBriefing(new ScheduleElement(ScheduleElement.Type.BRIEFING));
        setClosing(new ScheduleElement(ScheduleElement.Type.CLOSING_CEREMONY));
    }

    public Schedule(ArrayList<SwsxTournament.SwsxEvent> events) {
        String pattern = "round (\\d+)";
        Pattern regex = Pattern.compile(pattern);
        for (SwsxTournament.SwsxEvent event : events) {
            switch (event.getType()) {
                case BRIEFING ->
                        setBriefing(new ScheduleElement(ScheduleElement.Type.BRIEFING, (byte) 0, event.getDate()));
                case ENDING ->
                        setClosing(new ScheduleElement(ScheduleElement.Type.CLOSING_CEREMONY, (byte) 0, event.getDate()));
                default -> {
                    Matcher matcher = regex.matcher(event.getName());
                    if (matcher.find()) {
                        String numberStr = matcher.group(1);
                        byte number = Byte.parseByte(numberStr);
                        add(new ScheduleElement(ScheduleElement.Type.ROUND, number, event.getDate()));
                    } else {
                        add(new ScheduleElement(ScheduleElement.Type.ROUND));
                    }

                }
            }
        }
    }

    public static Schedule createFromDates(ArrayList<Date> dates) {
        Schedule schedule = new Schedule();
        for (int i = 0; i < dates.size(); i++) {
            schedule.add(new ScheduleElement(ScheduleElement.Type.ROUND, (byte) (i + 1), dates.get(i)));
        }
        return schedule;
    }

    public ArrayList<ScheduleElement> getRounds() {
        ArrayList<ScheduleElement> elements = new ArrayList<>();
        for (ScheduleElement element : this) {
            if (element.getType() == ScheduleElement.Type.ROUND) {
                elements.add(element);
            }
        }
        return elements;
    }

    @Override
    public boolean add(ScheduleElement element) {
        switch (element.getType()) {
            case BRIEFING -> {
                setBriefing(element);
                return true;
            }
            case CLOSING_CEREMONY -> {
                setClosing(element);
                return true;
            }
            default -> {
                return super.add(element);
            }
        }

    }


    public ScheduleElement getBriefing() {
        return briefing;
    }

    public void setBriefing(ScheduleElement briefing) {
        this.briefing = briefing;
        this.briefing.setType(ScheduleElement.Type.BRIEFING);
    }

    public ScheduleElement getClosing() {
        return closing;
    }

    public void setClosing(ScheduleElement closing) {
        this.closing = closing;
        this.closing.setType(ScheduleElement.Type.CLOSING_CEREMONY);
    }

    @Override
    public Iterator<ScheduleElement> iterator() {
        return new CustomIterator();
    }

    public static class ScheduleDeserializer implements JsonDeserializer<Schedule> {
        @Override
        public Schedule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Schedule schedule = new Schedule();
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                ScheduleElement element1 = context.deserialize(element, ScheduleElement.class);
                switch (element1.getType()) {
                    case BRIEFING -> schedule.setBriefing(element1);
                    case CLOSING_CEREMONY -> schedule.setClosing(element1);
                    default -> schedule.add(element1);
                }
            }
            return schedule;
        }
    }

    public static class ScheduleElement implements Serializable {
        private Type type;
        private byte roundNo;
        private Date date;

        public ScheduleElement(Type type) {
            this(type, (byte) 0);
        }

        public ScheduleElement(Type type, byte roundNo) {
            this(type, roundNo, new Date());
        }

        public ScheduleElement(Type type, byte roundNo, Date date) {
            setType(type);
            setRoundNo(roundNo);
            setDate(date);
        }

        public String getEventName() {
            if (roundNo == 0) {
                return getType().toString();
            }
            return getType() + " " + getRoundNo();
        }

        @Override
        public String toString() {
            return getEventName() + " " + getDate();
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public byte getRoundNo() {
            return roundNo;
        }

        public void setRoundNo(byte roundNo) {
            this.roundNo = roundNo;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ScheduleElement element = (ScheduleElement) o;
            return type == element.type && Objects.equals(roundNo, element.roundNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, roundNo);
        }


        public enum Type implements Serializable {
            BRIEFING,
            ROUND,
            CLOSING_CEREMONY;

            @Override
            public String toString() {
                return super.toString().replace('_', ' ');
            }
        }

    }

    private class CustomIterator implements Iterator<ScheduleElement>, Serializable {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size() + 2;
        }

        @Override
        public ScheduleElement next() {
            if (!hasNext()) {
                throw new IndexOutOfBoundsException("No more elements available");
            }
            if (currentIndex == 0) {
                currentIndex++;
                return getBriefing();
            }
            if (currentIndex == size() + 1) {
                currentIndex++;
                return getClosing();
            }
            return get(currentIndex++ - 1);
        }
    }
}
