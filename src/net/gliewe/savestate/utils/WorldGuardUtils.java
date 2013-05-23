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

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static com.sk89q.worldguard.bukkit.BukkitUtil.toVector;

public class WorldGuardUtils {

    private static WorldGuardPlugin _worldGuard = null;

    public static WorldGuardPlugin getWorldGuard() throws NoWorldGuardPluginException {
        if(_worldGuard != null)
            return _worldGuard;

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            throw new NoWorldGuardPluginException();
        }

        _worldGuard = (WorldGuardPlugin) plugin;
        return _worldGuard;
    }

    public static boolean regionDissimilarity(Location from, Location to) throws NoWorldGuardPluginException {
        return regionDissimilarity(from, to, new ArrayList<String>());
    }

    public static boolean regionDissimilarity(Location from, Location to, List<String> regionWhitelist) throws NoWorldGuardPluginException {

        RegionManager regionManager = getWorldGuard().getRegionManager(from.getWorld());

        Vector fromVector = toVector(from);
        Vector toVector = toVector(to);

        //Die RegionSets werden von der Ziel- und Istposition ermittelt
        ApplicableRegionSet fromRegionSet = regionManager.getApplicableRegions(fromVector);
        ApplicableRegionSet toRegionSet = regionManager.getApplicableRegions(toVector);

        //In dieser Liste werden die Regionen von der Ist-Position abgelegt und
        //die Regionen der Ziel-Position abgezogen. wenn dabei eine Region übrig bleibt,
        //wird aus einer Region heraus geschoben.
        List fromRegionList = new ArrayList<ProtectedRegion>();

        for (ProtectedRegion region : fromRegionSet) {
            if(!regionWhitelist.contains(region.getId())){
                fromRegionList.add(region);
            }
        }

        for (ProtectedRegion region : toRegionSet) {
            if(!regionWhitelist.contains(region.getId())){
                fromRegionList.remove(region);
            }
        }

        //Wenn eine Region übrig bleibt, gibt es eine region-Differenz
        return fromRegionList.size() != 0;
    }

    public static Boolean isValidRegionId(String id){
        return ProtectedRegion.isValidId(id);
    }

    //Von der Region-ID wird die Region und die World gefunden
    public static WGRegion getRegionFormId(String regionid) throws NoWorldGuardPluginException {
        for(World world : Bukkit.getWorlds()){
            RegionManager regionManager = getWorldGuard().getRegionManager(world);
            if(regionManager.hasRegion(regionid)) {
                return new WGRegion(regionid, world, regionManager.getRegion(regionid));
            }
        }
        return null;
    }
}
