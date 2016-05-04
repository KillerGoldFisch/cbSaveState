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
 *      V-0.3.2 2013-06-04:
 *          - Added World definition to "getRegionFormId()"
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

    /**
     *
     * @param regionid
     * @return
     * @throws NoWorldGuardPluginException
     */
    public static WGRegion getRegionFormId(String regionid) throws NoWorldGuardPluginException {

        //You can use something like "world::regionid"
        String[] splited = regionid.split("::");


        if(splited.length < 2) {
            //If there is only the region-id
            for(World world : Bukkit.getWorlds()){
                RegionManager regionManager = getWorldGuard().getRegionManager(world);
                if(regionManager.hasRegion(regionid)) {
                    return new WGRegion(regionid, world, regionManager.getRegion(regionid));
                }
            }
        } else if(splited.length == 2) {
            //If there is the World and the region-id
            World world = Bukkit.getWorld(splited[0]);
            if(world != null){
                RegionManager regionManager = getWorldGuard().getRegionManager(world);
                if(regionManager.hasRegion(splited[1])) {
                    return new WGRegion(regionid, world, regionManager.getRegion(splited[1]));
                }
            }
        }


        return null;
    }
}
