package com.ragnarok.config;

import com.ragnarok.Ragnarok;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Logger;

public class AuthConfig {
    public static String authString = "";
    public static String authServer = "";
    public static String username = "";

    private static Path p;
    private static File f;

    public static void load() {
        p = FabricLoader.getInstance().getConfigDir();
        f = p.resolve(Ragnarok.MOD_ID + "config.properties").toFile();
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                Files.createFile(f.toPath());
            } catch (IOException ignored) {
                System.out.println("Failed to create");
            }
        }
        try {
            Scanner reader = new Scanner(f);
            for( int line = 1; reader.hasNextLine(); line ++ ) {
                String content = reader.nextLine();
                String[] split = content.split("=");
                if (split.length < 2) {
                    continue;
                }
                if (split[0].equals("authString")) {
                    authString = split[1];
                } else if (split[0].equals("authServer")) {
                    authServer = split[1];
                } else if (split[0].equals("username")) {
                    username = split[1];
                }
            }
        } catch (IOException ignored) {
            System.out.println("Failed to read");
        }
    }

    static void updateFile() {
        String content = "authString=" + authString + "\n" + "authServer=" + authServer + "\n" + "username=" + username;
        try {
            Files.write(f.toPath(), content.getBytes());
        } catch (IOException ignored) {
            System.out.println("Failed to write" + ignored);
        }
    }

    public static void setAuthString(String s) {
        authString = s;
        updateFile();
    }

    public static void setAuthServer(String s) {
        authServer = s;
        updateFile();
    }

    public static void setUsername(String s) {
        username = s;
        updateFile();
    }
}
