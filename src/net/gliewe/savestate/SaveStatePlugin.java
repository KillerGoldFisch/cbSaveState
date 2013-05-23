package net.gliewe.savestate;

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

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import net.gliewe.savestate.commandhandler.LoadCommandHandler;
import net.gliewe.savestate.commandhandler.SaveCommandHandler;
import net.gliewe.savestate.utils.PlayerUtils;
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

        getCommand("savestate").setExecutor(new SaveCommandHandler(this));
        getCommand("loadstate").setExecutor(new LoadCommandHandler(this));
    }

    @Override
    public void onDisable() {
        _instance = null;
    }

    public void SavePlayerState(Player player, String nameofsave) throws IOException {
        PlayerUtils.savePlayerState(player, nameofsave);
    }

    public boolean LoadPlayerState(Player player, String nameofsave) {
        return PlayerUtils.loadPlayerState(player, nameofsave);
    }

    public void SaveRegionState(WGRegion region, String nameofsave) throws DataException, IOException, MaxChangedBlocksException {
        region.Save(nameofsave);
    }

    public boolean LoadRegionState(WGRegion region, String nameofsave) throws DataException, IOException, MaxChangedBlocksException {
        return region.Load(nameofsave);
    }
}
