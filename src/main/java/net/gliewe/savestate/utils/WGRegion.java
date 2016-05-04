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
 *      V-0.1 2013-05-24:
 *          * Fixed "WGRegion.getSize()" bug
 *
 */

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;

import java.io.IOException;


public class WGRegion {

    public static WGRegion getByID(String id) throws NoWorldGuardPluginException {
        return WorldGuardUtils.getRegionFormId(id);
    }

    private String _name = null;
    private World _world = null;
    private ProtectedRegion _region = null;

    public WGRegion(String name, World world, ProtectedRegion region){
        this._name = name;
        this._world = world;
        this._region = region;
    }

    public String getName() {
        return this._name;
    }

    public ProtectedRegion getRegion() {
        return this._region;
    }

    public World getWorld() {
        return this._world;
    }

    public double getXmax(){
        return Math.max(this.getRegion().getMinimumPoint().getX(), this.getRegion().getMaximumPoint().getX());
    }

    public double getYmax(){
        return Math.max(this.getRegion().getMinimumPoint().getY(), this.getRegion().getMaximumPoint().getY());
    }

    public double getZmax(){
        return Math.max(this.getRegion().getMinimumPoint().getZ(), this.getRegion().getMaximumPoint().getZ());
    }

    public double getXmin(){
        return Math.min(this.getRegion().getMinimumPoint().getX(), this.getRegion().getMaximumPoint().getX());
    }

    public double getYmin(){
        return Math.min(this.getRegion().getMinimumPoint().getY(), this.getRegion().getMaximumPoint().getY());
    }

    public double getZmin(){
        return Math.min(this.getRegion().getMinimumPoint().getZ(), this.getRegion().getMaximumPoint().getZ());
    }

    public Vector getOrigin() {
        return new Vector(
                        this.getXmin(),
                        this.getYmin(),
                        this.getZmin()
        );
    }

    public Vector getSize() {
        return new Vector(
                this.getXmax() - this.getXmin() + 1,
                this.getYmax() - this.getYmin() + 1,
                this.getZmax() - this.getZmin() + 1
        );
    }

    public void Save(String savename) throws DataException, IOException, MaxChangedBlocksException {
        RegionUtils.saveRegionState(this, savename);
    }

    public boolean Load(String savename) throws DataException, IOException, MaxChangedBlocksException {
        return RegionUtils.loadRegionState(this, savename);
    }

}
