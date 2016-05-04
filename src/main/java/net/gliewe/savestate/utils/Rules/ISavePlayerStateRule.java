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

public interface ISavePlayerStateRule {
    String getName();
    int getNameMatch(String savename);
    int getPlayerMatch(Player player);
    boolean getSaveLocation();
    boolean getSaveIsflying();
    boolean getSaveGamemode();
    boolean getSaveFalldistance();
    boolean getSaveFireticks();
    boolean getSaveVelocity();
    boolean getSaveHealth();
    boolean getSaveFoodlevel();
    boolean getSaveExp();
    boolean getSaveLevel();
    boolean getSaveArmor();
    boolean getSaveInventory();
    boolean getSavePotions();
}
