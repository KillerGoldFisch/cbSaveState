package net.gliewe.savestate;

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
 *          - added javadoc
 *          - added the rule-function
 */

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import net.gliewe.savestate.commandhandler.LoadCommandHandler;
import net.gliewe.savestate.commandhandler.SaveCommandHandler;
import net.gliewe.savestate.utils.PlayerUtils;
import net.gliewe.savestate.utils.Rules.ISavePlayerStateRule;
import net.gliewe.savestate.utils.Rules.SavePlayerStateRuleManager;
import net.gliewe.savestate.utils.WGRegion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SaveStatePlugin extends JavaPlugin {

    private static SaveStatePlugin _instance = null;

    public static SaveStatePlugin getInstance() { return _instance; }

    @Override
    public void onEnable(){
        _instance = this;

        if(!this.getDataFolder().exists())
            this.getDataFolder().mkdirs();

        SavePlayerStateRuleManager.INIT(this);

        getCommand("savestate").setExecutor(new SaveCommandHandler(this));
        getCommand("loadstate").setExecutor(new LoadCommandHandler(this));
    }

    @Override
    public void onDisable() {
        _instance = null;
    }

    /**
     * Saves the state of the player to drive,
     * while abide by the rules.
     *
     * @param player        The player wich state is to save
     * @param nameofsave    The savename, if its begin with an "#", this save will be shared between all players.
     *                      Else, it will be player specific.
     * @throws IOException  If the File can't de created
     */
    public void SavePlayerState(Player player, String nameofsave) throws IOException {
        PlayerUtils.savePlayerState(player, nameofsave);
    }

    /**
     * Loads the state of the Player from drive
     *
     * @param player       The player wich state is to load
     * @param nameofsave   The savename, if its begin with an "#", this save will be shared between all players.
     *                     Else, it will be player specific.
     * @return             returns "false" if the savename does not exists
     */
    public boolean LoadPlayerState(Player player, String nameofsave) {
        return PlayerUtils.loadPlayerState(player, nameofsave);
    }

    /**
     * Saves the Blocks of the Region to the drive.
     *
     * @param region        The name of the WoldGuard-Region wich is to save
     * @param nameofsave    The savename
     * @throws DataException              If the File can't be created
     * @throws IOException                If the File can't be created
     * @throws MaxChangedBlocksException  If the Region is too big
     */
    public void SaveRegionState(WGRegion region, String nameofsave) throws DataException, IOException, MaxChangedBlocksException {
        region.Save(nameofsave);
    }

    /**
     * loads the Blocks of the Region from the drive.
     *
     * @param region            The name of the WoldGuard-Region wich is to load
     * @param nameofsave        The savename
     * @return                  returns "false" if the savename does not exists
     * @throws DataException                If the File can't be loaded
     * @throws IOException                  If the File can't be loaded
     * @throws MaxChangedBlocksException    If the Region is too big
     */
    public boolean LoadRegionState(WGRegion region, String nameofsave) throws DataException, IOException, MaxChangedBlocksException {
        return region.Load(nameofsave);
    }

    /**
     * Registers a new Rule for the "SavePlayerState" function
     *
     * @param rule  The new Rule
     */
    public void RegisterRule(ISavePlayerStateRule rule) {
        SavePlayerStateRuleManager.getInstance().registerRule(rule);
    }

    /**
     * Removes a Rule by instance
     *
     * @param rule  The instance to remove
     * @return      Returns "true" if the Rule is removed
     */
    public boolean RemoveRule(ISavePlayerStateRule rule) {
        return SavePlayerStateRuleManager.getInstance().removeRule(rule);
    }

    /**
     * Removes a Rule by name
     *
     * @param rulename  The name of the Rule to remove
     * @return          Returns "true" if the Rule is removed
     */
    public boolean RemoveRule(String rulename) {
        return SavePlayerStateRuleManager.getInstance().removeRule(rulename);
    }

    /**
     * Removes multiple Rules by regex matching
     *
     * @param rulename  The regex pattern to match the name of the Rules to remove
     * @return          The Count of Rules where removed
     */
    public int RemoveRuleRegex(String rulename) {
        return SavePlayerStateRuleManager.getInstance().removeRuleRegex(rulename);
    }

}
