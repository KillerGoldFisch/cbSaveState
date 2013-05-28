package net.gliewe.savestate.utils;

/**
 * SaveState - Player an Region save Plugin for Bukkit Server
 *
 * (C) Copyright Kevin Gliewe (http://gliewe.net/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * Authors: Kevin Gliewe
 * Date: 2013-05-23
 *
 * Changelog:
 *      V-0.3 2013-05-28:
 *          - Added "save for all Players"
 *          - Rule Management
 */

import net.gliewe.savestate.SaveStatePlugin;
import net.gliewe.savestate.utils.Rules.ISavePlayerStateRule;
import net.gliewe.savestate.utils.Rules.SavePlayerStateRuleManager;
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
        nameofsave = nameofsave.replace('.', '_').replace('/', '_').replace('\\', '_');
        String dir = saveDir;
        if(plugin != null)
            dir = plugin.getDataFolder() + "/player_";
        if(nameofsave.startsWith("#"))
            return new File(dir + nameofsave + ".yaml");
        return new File(dir + player.getName() + "_" + nameofsave + ".yaml");
    }

    public static void savePlayerState(Player player, String nameofsave) throws IOException {
        ISavePlayerStateRule rule = SavePlayerStateRuleManager.getInstance().getRule(player, nameofsave);

        File outFile = getDatFile(player, nameofsave);

        FileConfiguration config = new YamlConfiguration();

        if(rule.getSaveLocation()) {
            config.set("location.world", player.getLocation().getWorld().getName());
            config.set("location.x", player.getLocation().getX());
            config.set("location.y", player.getLocation().getY());
            config.set("location.z", player.getLocation().getZ());
            config.set("location.pitch", (double)player.getLocation().getPitch());
            config.set("location.yaw", (double)player.getLocation().getYaw());
        }

        if(rule.getSaveIsflying())
            config.set("isflying", player.isFlying());

        if(rule.getSaveGamemode())
            config.set("gamemode", player.getGameMode().getValue());

        if(rule.getSaveFalldistance())
            config.set("falldistance", (double)player.getFallDistance());

        if(rule.getSaveFireticks())
            config.set("fireticks", player.getFireTicks());

        if(rule.getSaveVelocity())
            config.set("velocity", player.getVelocity());

        if(rule.getSaveHealth())
            config.set("health", player.getHealth());

        if(rule.getSaveFoodlevel())
            config.set("foodlevel", player.getFoodLevel());

        if(rule.getSaveExp())
            config.set("exp", (double)player.getExp());

        if(rule.getSaveLevel())
            config.set("level", player.getLevel());

        if(rule.getSaveArmor()) {
            config.set("armor.helmet", player.getInventory().getHelmet());
            config.set("armor.chestplate", player.getInventory().getChestplate());
            config.set("armor.leggings", player.getInventory().getLeggings());
            config.set("armor.boots", player.getInventory().getBoots());
        }

        if(rule.getSaveInventory()) {

            List<ItemStack> items = new ArrayList<>();

            for(int i = 0 ; i < player.getInventory().getSize() ; i++){
                items.add(player.getInventory().getItem(i));
            }

            config.set("inventory", items);
        }

        if(rule.getSavePotions()) {

            List<PotionEffect> potions = new ArrayList<>();

            for(PotionEffect potion: player.getActivePotionEffects()) {
                potions.add(potion);
            }

            config.set("potions", potions);
        }

        config.save(outFile);
    }

    public static boolean loadPlayerState(Player player, String nameofsave) {
        File inFile = getDatFile(player, nameofsave);
        if(!inFile.exists())
            return false;

        FileConfiguration config = YamlConfiguration.loadConfiguration(inFile);

        if(config.contains("location.world")) {
            World world = Bukkit.getWorld(config.getString("location.world"));

            Location playerLoc = new Location(world,
                    config.getDouble("location.x"),
                    config.getDouble("location.y"),
                    config.getDouble("location.z"),
                    (float)config.getDouble("location.yaw"),
                    (float)config.getDouble("location.pitch"));

            player.teleport(playerLoc);
        }

        if(config.contains("isflying"))
            player.setFlying(config.getBoolean("isflying"));

        if(config.contains("gamemode"))
            player.setGameMode(GameMode.getByValue(config.getInt("gamemode")));

        if(config.contains("health"))
            player.setHealth(config.getInt("health"));

        if(config.contains("foodlevel"))
            player.setFoodLevel(config.getInt("foodlevel"));

        if(config.contains("exp"))
            player.setExp((float)config.getDouble("exp"));

        if(config.contains("level"))
            player.setLevel(config.getInt("level"));

        if(config.contains("falldistance"))
            player.setFallDistance((float)config.getDouble("falldistance"));

        if(config.contains("fireticks"))
            player.setFireTicks(config.getInt("fireticks"));

        if(config.contains("velocity"))
            player.setVelocity(config.getVector("velocity"));

        if(config.contains("armor.helmet")) {
            player.getInventory().setHelmet(config.getItemStack("armor.helmet"));
            player.getInventory().setChestplate(config.getItemStack("armor.chestplate"));
            player.getInventory().setLeggings(config.getItemStack("armor.leggings"));
            player.getInventory().setBoots(config.getItemStack("armor.boots"));
        }

        if(config.contains("inventory")) {
            List<ItemStack> items = (List<ItemStack>)config.getList("inventory");

            for(int i = 0 ; i < items.size() ; i++){
                player.getInventory().setItem(i, items.get(i));
            }
        }

        if(config.contains("potions")) {
            List<PotionEffect> potions = (List<PotionEffect>)config.getList("potions");

            for(PotionEffectType potiontype : PotionEffectType.values())
                if(potiontype != null)
                    if(player.hasPotionEffect(potiontype))
                        player.removePotionEffect(potiontype);

            for(PotionEffect potion : potions)
                player.addPotionEffect(potion);
        }

        return true;
    }
}
