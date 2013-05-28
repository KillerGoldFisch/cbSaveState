package net.gliewe.savestate.utils.Rules;

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

public class SavePlayerStateDefaultRule implements ISavePlayerStateRule {
    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public int getNameMatch(String savename) {
        return -2;
    }

    @Override
    public int getPlayerMatch(Player player) {
        return -2;
    }

    @Override
    public boolean getSaveLocation() {
        return true;
    }

    @Override
    public boolean getSaveIsflying() {
        return true;
    }

    @Override
    public boolean getSaveGamemode() {
        return true;
    }

    @Override
    public boolean getSaveFalldistance() {
        return true;
    }

    @Override
    public boolean getSaveFireticks() {
        return true;
    }

    @Override
    public boolean getSaveVelocity() {
        return true;
    }

    @Override
    public boolean getSaveHealth() {
        return true;
    }

    @Override
    public boolean getSaveFoodlevel() {
        return true;
    }

    @Override
    public boolean getSaveExp() {
        return true;
    }

    @Override
    public boolean getSaveLevel() {
        return true;
    }

    @Override
    public boolean getSaveArmor() {
        return true;
    }

    @Override
    public boolean getSaveInventory() {
        return true;
    }

    @Override
    public boolean getSavePotions() {
        return true;
    }
}
