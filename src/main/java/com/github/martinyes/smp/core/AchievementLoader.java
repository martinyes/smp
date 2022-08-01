package com.github.martinyes.smp.core;

import com.github.martinyes.smp.Main;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.bukkit.advancement.Advancement;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

/**
 * Loads all the achievements' message into a {@link Map}.
 *
 * @author martin
 */
public class AchievementLoader {

    @Getter private static Map<String, String> achievements;

    /**
     * Loads the achievements' messages into a {@link Map}
     * <p>
     * If the folder does not exist, this method automatically does that and then moves the file there.
     *
     * @throws IOException - if the file does not exist
     */
    public static void load() throws IOException {
        File folder = Main.getInstance().getDataFolder();

        if (!folder.exists())
            folder.mkdirs();

        File file = new File(Main.getInstance().getDataFolder() + "/en_gb.json");

        if (!file.exists())
            Files.copy(Main.class.getResourceAsStream("/en_gb.json"), Paths.get(file.getAbsolutePath()));

        Reader reader = Files.newBufferedReader(file.toPath());

        Type token = new TypeToken<Map<String, String>>() {}.getType();
        achievements = Main.getGson().fromJson(reader, token);
    }

    /**
     * Gets the name of the provided advancement according to its key.
     *
     * @param advancement - an instance of the target advancement
     * @return - an {@link Optional<String>} of the name
     */
    public static Optional<String> getName(Advancement advancement) {
        if (advancement == null)
            return Optional.empty();

        String key = "advancements.%s.title";
        String value = String.format(key, advancement.getKey().getKey().replaceAll("/", "."));

        if (achievements.containsKey(value))
            return Optional.of(achievements.get(value));

        return Optional.empty();
    }

    /**
     * Gets the description of the provided advancement according to its key.
     *
     * @param advancement - an instance of the target advancement
     * @return - an {@link Optional<String>} of the description
     */
    public static Optional<String> getDescription(Advancement advancement) {
        if (advancement == null)
            return Optional.empty();

        String key = "advancements.%s.description";
        String value = String.format(key, advancement.getKey().getKey().replaceAll("/", "."));

        if (achievements.containsKey(value))
            return Optional.of(achievements.get(value));

        return Optional.empty();
    }
}