cbSaveState
===========

Player and Region save Plugin for Bukkit Server

Download:
    
* Version 0.1 : <http://www.file-upload.net/download-7632473/cbSaveState-0.1.jar.html>

Usage
-----

You can issue ingame the Commands

    /savestate -o <id-of-object> -t <type-of-object> -n <name-to-save>

and

    /loadstate -o <id-of-object> -t <type-of-object> -n <name-to-save>

to save and load an Player or Region state to or from an file.

#### Option **"-o"**

Represents the Name of the Region (defined by **/region define <id-of-region>**) or the Playername.

In case of *"-t player"*, the default Value is the player wich issued the command.

#### Option **"-t"**

Can be *"player"* or *"region"*.

#### Option **"-n"**

A peace of the filename to handle multible States.
