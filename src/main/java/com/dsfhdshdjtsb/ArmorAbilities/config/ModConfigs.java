package com.dsfhdshdjtsb.ArmorAbilities.config;
import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.mojang.datafixers.util.Pair;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static boolean FOCUS;
    public static boolean PULVERIZE;
    public static boolean TELEKINESIS;

    public static boolean CLEANSE;
    public static boolean EXPLODE;

    public static boolean DASH;
    public static boolean RUSH;
    public static boolean DODGE;

    public static boolean BLINK;
    public static boolean FROST_STOMP;
    public static boolean FIRE_STOMP;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(ArmorAbilities.modid + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        //configs.addKeyValuePair(new Pair<>("transcend.enabled", true));
        configs.addKeyValuePair(new Pair<>("pulverize.enabled", true));
        configs.addKeyValuePair(new Pair<>("telekinesis.enabled", true));

        configs.addKeyValuePair(new Pair<>("cleanse.enabled", true));
        configs.addKeyValuePair(new Pair<>("explode.enabled", true));

        configs.addKeyValuePair(new Pair<>("dash.enabled", true));
        configs.addKeyValuePair(new Pair<>("rush.enabled", true));
        configs.addKeyValuePair(new Pair<>("dodge.enabled", true));

        configs.addKeyValuePair(new Pair<>("blink.enabled", true));
        configs.addKeyValuePair(new Pair<>("frost_stomp.enabled", true));
        configs.addKeyValuePair(new Pair<>("fire_stomp.enabled", true));

    }

    private static void assignConfigs() {
        //TRANSCEND = CONFIG.getOrDefault("transcend.enabled", true);
        PULVERIZE= CONFIG.getOrDefault("pulverize.enabled", true);
        TELEKINESIS= CONFIG.getOrDefault("telekinesis.enabled", true);

        CLEANSE = CONFIG.getOrDefault("cleanse.enabled", true);
        EXPLODE = CONFIG.getOrDefault("explode.enabled", true);

        DASH = CONFIG.getOrDefault("dash.enabled", true);
        RUSH = CONFIG.getOrDefault("rush.enabled", true);
        DODGE = CONFIG.getOrDefault("dodge.enabled", true);

        BLINK = CONFIG.getOrDefault("blink.enabled", true);
        FROST_STOMP = CONFIG.getOrDefault("frost_stomp.enabled", true);
        FIRE_STOMP = CONFIG.getOrDefault("fire_stomp.enabled", true);


        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}