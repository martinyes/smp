package com.github.martinyes.smp.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class GeneralUtils {

    public Optional<UUID> getPlayerUUID(String name) {
        String backend = "https://api.minetools.eu/uuid/" + name;

        try {
            URL url = new URL(backend);
            URLConnection connection = url.openConnection();
            connection.connect();

            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(new InputStreamReader((InputStream) connection.getContent()));
            JsonObject object = root.getAsJsonObject();

            if (object.get("status").getAsString().equals("ERR")) {
                return Optional.empty();
            }

            String uuid = object.get("id").getAsString()
                    .replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5");
            return Optional.of(UUID.fromString(uuid));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}