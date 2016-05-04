import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import net.gliewe.savestate.SaveStatePlugin;
import net.gliewe.savestate.utils.NoWorldGuardPluginException;
import net.gliewe.savestate.utils.Rules.SavePlayerStateConfigRule;
import net.gliewe.savestate.utils.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

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
 * Date: 2013-05-28
 *
 * Changelog:
 *      no changes
 */

public class APISample extends JavaPlugin {

    public SaveStatePlugin getSaveStatePlugin() {
        Plugin plugin = getServer().getPluginManager().getPlugin("SaveState");

        if (plugin == null || !(plugin instanceof SaveStatePlugin)) {
            return null;
        }

        return (SaveStatePlugin) plugin;
    }

    public void registerMyRule() {
        MemorySection ruleConfig = new MemoryConfiguration();

        //Remember, all rule-key are optional!
        //If a key is not set, it will automaticly "true"
        //The default value can be changed like so:
        ruleConfig.set("default", false);

        //The rule matches for savenames like "test1" or "test42"
        ruleConfig.set("savename", "^test[\\d]+$");

        //The Rule only matches, if the playername is "KillerGoldFisch"
        //A Regex pattern is possible
        ruleConfig.set("playername", "KillerGoldFisch");

        //Save the Location of the Player
        ruleConfig.set("location", true);

        //Save if the Player is flying
        ruleConfig.set("isflying", true);

        //Save the players gamemode
        ruleConfig.set("gamemode", true);

        //Save the current fall distance of the Player
        ruleConfig.set("falldistance", true);

        //Save if the Player is on fire
        ruleConfig.set("fireticks", true);

        //Save the velocity of the Player
        ruleConfig.set("velocity", true);

        //Save the heathbar of the Player
        ruleConfig.set("health", true);

        //Save the foodlevel of the Player
        ruleConfig.set("foodlevel", true);

        //Save the experience of the Player
        ruleConfig.set("exp", true);

        //Save the level of the Player
        ruleConfig.set("level", true);

        //Save the armor-inventory of the Player
        ruleConfig.set("armor", true);

        //Save the inventory of the Player (not the armor)
        ruleConfig.set("inventory", true);

        //Save the potion effects of the Player
        ruleConfig.set("potions", true);

        //register the rule in the rule-manager
        getSaveStatePlugin().RegisterRule(
                new SavePlayerStateConfigRule(ruleConfig, "<unique-name-of-my-rule>")
        );
    }

    public void SpawnPlayer(Player player) {
        //loads a SHARED (starts with "#") state
        //you can create this State ingame with the commend "/savestate -t p -n #<unique-name-of-my-state>"
        if(!getSaveStatePlugin().LoadPlayerState(
                player,
                "#" + "<unique-name-of-my-state>"
        )) {
            //The File could not be found
            player.sendMessage(ChatColor.RED + "The Spawnpoint does not exist!");
        }
    }

    public void SavePlayersCheckpoint(Player player) {
        try {
            //Saves a player specific state
            getSaveStatePlugin().SavePlayerState(
                    player,
                    "<unique-name-of-my-checkpoint>"
            );
        } catch (IOException e) {
            //File could not be written
            player.sendMessage(ChatColor.RED + "Oh oh, please contact the boss!");
            e.printStackTrace();
        }
    }

    public void LoadPlayersLastCheckpoint(Player player) {
        //loads a player specific state
        if(!getSaveStatePlugin().LoadPlayerState(
                player,
                "<unique-name-of-my-checkpoint>"
        )) {
            //The File could not be found
            player.sendMessage(ChatColor.RED + "The Checkpoint does not exist!");
        }
    }

    public void TakeASnapshotOfTheArena() {
        try {
            getSaveStatePlugin().SaveRegionState(
                    WGRegion.getByID("<the-worldguard-regin-name>"),
                    "<unique-name-of-my-snapshot>"
            );
        } catch (DataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MaxChangedBlocksException e) {
            Bukkit.getLogger().warning(ChatColor.RED + "The region is too big");
        } catch (NoWorldGuardPluginException e) {
            Bukkit.getLogger().warning(ChatColor.RED + "WorldGuard is required to take a region snapshot");
        }
    }

    public void ResetTheArenaToSnapshot() {
        try {
            if(!getSaveStatePlugin().LoadRegionState(
                    WGRegion.getByID("<the-worldguard-regin-name>"),
                    "<unique-name-of-my-snapshot>"
            )) {
                Bukkit.getLogger().warning(ChatColor.RED + "The region file could not be found");
            }
        } catch (DataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MaxChangedBlocksException e) {
            Bukkit.getLogger().warning(ChatColor.RED + "The region is too big");
        } catch (NoWorldGuardPluginException e) {
            Bukkit.getLogger().warning(ChatColor.RED + "WorldGuard is required to take a region snapshot");
        }
    }

    @Override
    public void onEnable(){

    }

    @Override
    public void onDisable() {

    }
}
