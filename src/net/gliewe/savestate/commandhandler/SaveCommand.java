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
 *      no changes
 */

import com.github.ryenus.rop.OptionParser.Command;
import com.github.ryenus.rop.OptionParser.Option;

@Command(name = "save", descriptions="Save object to disc")
public class SaveCommand {
    @Option(description = "object ID", opt = { "-o", "--object" })
    String object;

    @Option(description = "Name of save", opt = { "-n", "--name" })
    String name;

    @Option(description = "Type of object (player|region)", opt = {"-t", "--type" } )
    String type;
}
