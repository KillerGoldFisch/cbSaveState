package net.gliewe.savestate.commandhandler;

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
 *          * Default Value for "name" argument
 *          * Shortcut for player -> p and region -> r
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

public class SaveCommandHandler implements CommandExecutor {

    private SaveStatePlugin _plugin = null;

    public SaveCommandHandler(SaveStatePlugin plugin){
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
            OptionParser parser = new OptionParser(SaveCommand.class);
            SaveCommand p = parser.get(SaveCommand.class);
            parser.parse(args, sender).get(p);

            /* type can not be null */
            if(p.type == null) {
                parser.showHelp(sender);
                return true;
            }

            /* if no name is set, use "default" */
            if(p.name == null)
                p.name = "default";

            if(p.type.equalsIgnoreCase("player") || p.type.equalsIgnoreCase("p")){

                ////////////////////////////////// PLAYER /////////////////////////////////////////
                /*********************************************************************************/
                Player player = null; //To be saved

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

                _plugin.SavePlayerState(player, p.name);
                sender.sendMessage(ChatColor.GREEN + "Saved");

                /*********************************************************************************/

            } else if(p.type.equalsIgnoreCase("region") || p.type.equalsIgnoreCase("r")) {

                ////////////////////////////////// REGION /////////////////////////////////////////
                /*********************************************************************************/
                //Object to be Saved can't be null
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

                region.Save(p.name);
                sender.sendMessage(ChatColor.GREEN + "Saved");

                /*********************************************************************************/

            } else {
                sender.sendMessage(ChatColor.RED + "Type '" + p.type + "' not supportet, try 'player', 'mob' or 'region'");
                parser.showHelp(sender);
            }
        }catch(Exception ex) {
            sender.sendMessage(ChatColor.RED + "ERROR");
            ex.printStackTrace();
        }
        return true;
    }
}
