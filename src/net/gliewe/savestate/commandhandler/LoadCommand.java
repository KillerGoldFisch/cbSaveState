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

@OptionParser.Command(name = "load", descriptions="restore object from disc")
public class LoadCommand {
    @OptionParser.Option(description = "object ID", opt = { "-o", "--object" })
    String object;

    @OptionParser.Option(description = "Name of save", opt = { "-n", "--name" })
    String name;

    @OptionParser.Option(description = "Type of object (player|region)", opt = {"-t", "--type" } )
    String type;
}
