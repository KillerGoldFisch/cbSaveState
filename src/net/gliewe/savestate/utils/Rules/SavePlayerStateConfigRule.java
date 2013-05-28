package net.gliewe.savestate.utils.Rules;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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

public class SavePlayerStateConfigRule implements ISavePlayerStateRule{

    private MemorySection _config = null;

    private boolean _default = true;

    private String _name;

    public SavePlayerStateConfigRule(MemorySection config, String name) {
        this._config = config;
        this._name = name;

        if(config.contains("default"))
            this._default = config.getBoolean("default");
    }

    public MemorySection getConfig() {
        return this._config;
    }

    public boolean hasRule(String type) {
        return this._config.contains(type);
    }

    public boolean getRule(String rulename) {
        if(!hasRule(rulename))
            return _default;

        return _config.getBoolean(rulename);
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public int getNameMatch(String savename) {
        if(!hasRule("savename"))
            return -1;

        String pattern = this._config.getString("savename");

        if(pattern.equals(savename))
            return 999;

        if(savename.matches(pattern)) {
            return 0;
        }

        return -999;
    }

    @Override
    public int getPlayerMatch(Player player) {
        if(!hasRule("playername"))
            return -1;

        String pattern = this._config.getString("playername");

        if(pattern.equals(player.getName()))
            return 1000;

        if(player.getName().matches(pattern)) {
            return 0;
        }

        return Integer.MIN_VALUE;
    }

    @Override
    public boolean getSaveLocation() {
        return getRule("location");
    }

    @Override
    public boolean getSaveIsflying() {
        return getRule("isflying");
    }

    @Override
    public boolean getSaveGamemode() {
        return getRule("gamemode");
    }

    @Override
    public boolean getSaveFalldistance() {
        return getRule("falldistance");
    }

    @Override
    public boolean getSaveFireticks() {
        return getRule("fireticks");
    }

    @Override
    public boolean getSaveVelocity() {
        return getRule("velocity");
    }

    @Override
    public boolean getSaveHealth() {
        return getRule("health");
    }

    @Override
    public boolean getSaveFoodlevel() {
        return getRule("foodlevel");
    }

    @Override
    public boolean getSaveExp() {
        return getRule("exp");
    }

    @Override
    public boolean getSaveLevel() {
        return getRule("level");
    }

    @Override
    public boolean getSaveArmor() {
        return getRule("armor");
    }

    @Override
    public boolean getSaveInventory() {
        return getRule("inventory");
    }

    @Override
    public boolean getSavePotions() {
        return getRule("potions");
    }
}
