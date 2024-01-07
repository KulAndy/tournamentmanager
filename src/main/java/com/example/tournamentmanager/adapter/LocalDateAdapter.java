package com.example.tournamentmanager.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, uuuu, hh:mm:ss a");

    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        String formattedDate = formatter.format(date);
        String cleanFormattedDate = removeNonUTF8Characters(formattedDate);
        return new JsonPrimitive(cleanFormattedDate);
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return LocalDate.parse(json.getAsString(), formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private String removeNonUTF8Characters(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder cleanString = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (ch >= 0x20 && ch <= 0x7F) {
                cleanString.append(ch);
            } else {
                cleanString.append(' ');
            }
        }

        return cleanString.toString();
    }
}
