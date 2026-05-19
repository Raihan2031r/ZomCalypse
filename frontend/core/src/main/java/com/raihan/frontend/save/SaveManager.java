package com.raihan.frontend.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveManager {
    private static final Json json = new Json();

    public static void save(SaveDTO data) {
        json.setOutputType(JsonWriter.OutputType.json);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = formatter.format(new Date());

        String fileName = "savegame_" + timestamp + ".json";

        FileHandle file = Gdx.files.external(".zomcalypse/saves/" + fileName);

        file.writeString(json.prettyPrint(data), false);
        System.out.println("Game Saved to: " + file.path());
    }

    public static SaveDTO load(String fileName) {
        FileHandle file = Gdx.files.external(".zomcalypse/saves/" + fileName);
        if (!file.exists()) return null;
        try {
            return json.fromJson(SaveDTO.class, file.readString());
        } catch (Exception e) {
            System.err.println("Save file corrupt: " + e.getMessage());
            return null;
        }
    }

    public static FileHandle[] getAllLocalSaves() {
        FileHandle dir = Gdx.files.external(".zomcalypse/saves/");
        if (!dir.exists()) {
            dir.mkdirs();
            return new FileHandle[0];
        }
        return dir.list(".json");
    }
}
