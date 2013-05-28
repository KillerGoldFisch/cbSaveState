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
 *      no changes
 */

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import net.gliewe.savestate.SaveStatePlugin;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class RegionUtils {

    public static String saveDir = "./plugins/savestate/region_";

    public static File getDatFile(WGRegion region, String nameofsave) {
        SaveStatePlugin plugin = SaveStatePlugin.getInstance();
        String dir = saveDir;
        if(plugin != null)
            dir = plugin.getDataFolder() + "/region_";
        return new File(dir + region.getName() + "_" + nameofsave + ".schematic");
    }


    private static void saveRegionState(WGRegion region, File file) throws DataException, IOException, MaxChangedBlocksException {
        EditSession es = new EditSession(new BukkitWorld((World)region.getWorld()), 999999999);
        CuboidClipboard cc = new CuboidClipboard(
                region.getSize(),
                region.getOrigin()
        );
        cc.copy(es);
        SchematicFormat.MCEDIT.save(cc ,file);
    }

    public static void saveRegionState(WGRegion region, String nameofsave) throws DataException, IOException, MaxChangedBlocksException {
        File outFile = getDatFile(region, nameofsave);
        saveRegionState(region, outFile);
    }

    private static void loadRegionState(WGRegion region, File file) throws DataException, IOException, MaxChangedBlocksException {
        EditSession es = new EditSession(new BukkitWorld(region.getWorld()), 999999999);
        CuboidClipboard cc = SchematicFormat.MCEDIT.load(file);
        cc.paste(es, region.getOrigin(), false);
    }

    public static boolean loadRegionState(WGRegion region, String nameofsave) throws DataException, IOException, MaxChangedBlocksException {
        File inFile = getDatFile(region, nameofsave);
        if(!inFile.exists())
            return false;
        loadRegionState(region, inFile);
        return true;
    }
}
