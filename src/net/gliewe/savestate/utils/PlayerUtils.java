package net.gliewe.savestate.utils;

/**
 * SaveState - Player an Region save Plugin for Bukkit Server
 *
 * Copyright (C) 2013 Kevin Gliewe
 *
 * This file is part of SaveState.
 *
 * "SaveState" is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * "SaveState" is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details. You should have received a copy of the GNU
 * General Public License along with SaveState. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authors: Kevin Gliewe
 * Date: 2013-05-23
 *
 * Changelog:
 *      no changes
 */

import net.gliewe.savestate.SaveStatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {

    public static String saveDir = "./plugins/savestate/player_";

    public static File getDatFile(Player player, String nameofsave) {
        SaveStatePlugin plugin = SaveStatePlugin.getInstance();
        String dir = saveDir;
        if(plugin != null)
            dir = plugin.getDataFolder() + "/player_";
        return new File(dir + player.getName() + "_" + nameofsave + ".yaml");
    }

    public static void savePlayerState(Player player, String nameofsave) throws IOException {
        File outFile = getDatFile(player, nameofsave);

        FileConfiguration config = new YamlConfiguration();

        config.set("location.world", player.getLocation().getWorld().getName());
        config.set("location.x", player.getLocation().getX());
        config.set("location.y", player.getLocation().getY());
        config.set("location.z", player.getLocation().getZ());
        config.set("location.pitch", (double)player.getLocation().getPitch());
        config.set("location.yaw", (double)player.getLocation().getYaw());

        config.set("isflying", player.isFlying());
        config.set("gamemode", player.getGameMode().getValue());

        config.set("falldistance", (double)player.getFallDistance());
        config.set("fireticks", player.getFireTicks());
        config.set("velocity", player.getVelocity());


        config.set("health", player.getHealth());
        config.set("foodlevel", player.getFoodLevel());
        config.set("exp", (double)player.getExp());
        config.set("level", player.getLevel());

        config.set("armor.helmet", player.getInventory().getHelmet());
        config.set("armor.chestplate", player.getInventory().getChestplate());
        config.set("armor.leggings", player.getInventory().getLeggings());
        config.set("armor.boots", player.getInventory().getBoots());

        List<ItemStack> items = new ArrayList<>();

        for(int i = 0 ; i < player.getInventory().getSize() ; i++){
            items.add(player.getInventory().getItem(i));
        }

        config.set("inventory", items);

        List<PotionEffect> potions = new ArrayList<>();

        for(PotionEffect potion: player.getActivePotionEffects()) {
            potions.add(potion);
        }

        config.set("potions", potions);

        config.save(outFile);
    }

    public static boolean loadPlayerState(Player player, String nameofsave) {
        File inFile = getDatFile(player, nameofsave);
        if(!inFile.exists())
            return false;

        FileConfiguration config = YamlConfiguration.loadConfiguration(inFile);

        World world = Bukkit.getWorld(config.getString("location.world"));

        Location playerLoc = new Location(world,
                config.getDouble("location.x"),
                config.getDouble("location.y"),
                config.getDouble("location.z"),
                (float)config.getDouble("location.yaw"),
                (float)config.getDouble("location.pitch"));

        player.teleport(playerLoc);

        player.setFlying(config.getBoolean("isflying"));
        player.setGameMode(GameMode.getByValue(config.getInt("gamemode")));

        player.setHealth(config.getInt("health"));
        player.setFoodLevel(config.getInt("foodlevel"));
        player.setExp((float)config.getDouble("exp"));
        player.setLevel(config.getInt("level"));

        player.setFallDistance((float)config.getDouble("falldistance"));
        player.setFireTicks(config.getInt("fireticks"));
        player.setVelocity(config.getVector("velocity"));

        player.getInventory().setHelmet(config.getItemStack("armor.helmet"));
        player.getInventory().setChestplate(config.getItemStack("armor.chestplate"));
        player.getInventory().setLeggings(config.getItemStack("armor.leggings"));
        player.getInventory().setBoots(config.getItemStack("armor.boots"));

        List<ItemStack> items = (List<ItemStack>)config.getList("inventory");

        for(int i = 0 ; i < items.size() ; i++){
            player.getInventory().setItem(i, items.get(i));
        }

        List<PotionEffect> potions = (List<PotionEffect>)config.getList("potions");

        for(PotionEffectType potiontype : PotionEffectType.values())
            if(potiontype != null)
                if(player.hasPotionEffect(potiontype))
                    player.removePotionEffect(potiontype);

        for(PotionEffect potion : potions)
            player.addPotionEffect(potion);

        return true;
    }
}
