package com.example.tournamentmanager.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");


    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        String formattedDate = formatter.format(date);
        String cleanFormattedDate = removeNonUTF8Characters(formattedDate);
        return new JsonPrimitive(cleanFormattedDate);
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try{
            return formatter.parse(json.getAsString());
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
