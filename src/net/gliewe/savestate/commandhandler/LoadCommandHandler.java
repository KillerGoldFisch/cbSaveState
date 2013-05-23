package net.gliewe.savestate.commandhandler;

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

import com.github.ryenus.rop.OptionParser;
import net.gliewe.savestate.SaveStatePlugin;
import net.gliewe.savestate.utils.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadCommandHandler implements CommandExecutor {

    private SaveStatePlugin _plugin = null;

    public LoadCommandHandler(SaveStatePlugin plugin){
        this._plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {

            /* TEST for PLAYER */
            boolean isPlayer = sender instanceof Player;

            /* TEST for PERMISSION */
            if(isPlayer && !sender.hasPermission("savestate")){
                sender.sendMessage(ChatColor.RED + "You dont have the permission to do it!");
                return true;
            }

            /* Parse arguments */
            OptionParser parser = new OptionParser(LoadCommand.class);
            LoadCommand p = parser.get(LoadCommand.class);
            parser.parse(args, sender).get(p);

            /* type and name can not be null */
            if(p.type == null || p.name == null) {
                parser.showHelp(sender);
                return true;
            }

            if(p.type.equalsIgnoreCase("player")){

                ////////////////////////////////// PLAYER /////////////////////////////////////////
                /*********************************************************************************/
                Player player = null; //To be restored

                if(p.object == null || p.object.length() == 0){
                    //If no player is set, default is the sender
                    if(isPlayer)
                        player = (Player)sender;
                } else {
                    //Get player by name
                    player = Bukkit.getPlayer(p.object);
                }

                if(player == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found");
                    return true;
                }

                if(_plugin.LoadPlayerState(player, p.name))
                    sender.sendMessage(ChatColor.GREEN + "Loaded");
                else
                    sender.sendMessage(ChatColor.RED + "File not found");

                /*********************************************************************************/

            } else if(p.type.equalsIgnoreCase("region")) {

                ////////////////////////////////// REGION /////////////////////////////////////////
                /*********************************************************************************/
                //Object to be Loaded can't be null
                if(p.object == null) {
                    parser.showHelp(sender);
                    return true;
                }

                //Get region from name
                WGRegion region = WGRegion.getByID(p.object);

                if(region == null) {
                    sender.sendMessage(ChatColor.RED + "Region not found");
                    return true;
                }

                if(region.Load(p.name))
                    sender.sendMessage(ChatColor.GREEN + "Loaded");
                else
                    sender.sendMessage(ChatColor.RED + "File not found");

                /*********************************************************************************/
            } else {
                sender.sendMessage(ChatColor.RED + "Type '" + p.type + "' not supportet, try 'player' or 'region'");
                parser.showHelp(sender);
            }
        }catch(Exception ex) {
            sender.sendMessage(ChatColor.RED + "ERROR");
            ex.printStackTrace();
        }
        return true;
    }
}