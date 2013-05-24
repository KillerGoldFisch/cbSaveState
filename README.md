cbSaveState
===========

Player and Region save Plugin for Bukkit Server

Download:

* Version 0.2 : <http://www.file-upload.net/download-7633344/cbSaveState-0.2.jar.html>
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

Can be *"player"* / *"p"* or *"region"* / *"r"*.

#### Option **"-n"**

A peace of the filename to handle multible States.
This is optional.

## Changelog:

### V-0.2 2013-05-24:

    * Default Value for "name" argument
    * Shortcut for player -> p and region -> r
    * Fixed "WGRegion.getSize()" bug
