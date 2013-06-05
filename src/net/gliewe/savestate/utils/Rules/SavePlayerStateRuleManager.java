package net.gliewe.savestate.utils.Rules;

import net.gliewe.savestate.SaveStatePlugin;
import net.gliewe.savestate.utils.IO;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
 * Date: 2013-05-28
 *
 * Changelog:
 *      V-0.3.2 2013-06-04:
 *          - Added default rule files
 */

public class SavePlayerStateRuleManager {

    private static SavePlayerStateRuleManager _instance = null;

    public static SavePlayerStateRuleManager getInstance() { return _instance; }

    public static void INIT(SaveStatePlugin plugin) throws IOException {
        _instance = new SavePlayerStateRuleManager(plugin);
    }

    private List<ISavePlayerStateRule> _rules = new ArrayList<>();

    private SaveStatePlugin _plugin;

    public SavePlayerStateRuleManager(SaveStatePlugin plugin) {
        _plugin = plugin;

        registerRule(new SavePlayerStateDefaultRule());


        try {
            this.copyDefaultRules(plugin);
        } catch (IOException e) {
            Bukkit.getLogger().warning("[SaveState] Error while copying default rules:");
            e.printStackTrace();
        }

        for(File file: plugin.getDataFolder().listFiles())
            if(file.isFile()) {
                String filename = file.getName();
                if(filename.matches("^rule_[\\w]*\\.yml$")) {

                    registerRule(new SavePlayerStateConfigRule(
                            YamlConfiguration.loadConfiguration(file),
                            filename
                    ));
                }
            }
    }

    public void registerRule(ISavePlayerStateRule rule) {
        Bukkit.getLogger().info("[SaveState] new rule: " + rule.getName());
        _rules.add(rule);
    }

    public boolean removeRule(ISavePlayerStateRule rule) {
        if(_rules.contains(rule)) {
            _rules.remove(rule);
            Bukkit.getLogger().info("[SaveState] rule removed: " + rule.getName());
            return true;
        }
        return false;
    }

    public boolean removeRule(String rulename) {
        for(ISavePlayerStateRule rule: _rules)
            if(rule.getName().equals(rulename)) {
                removeRule(rule);
                return true;
            }
        return false;
    }

    public int removeRuleRegex(String rulename) {
        int removed = 0;

        for(ISavePlayerStateRule rule: _rules)
            if(rule.getName().matches(rulename))
            {
                removeRule(rule);
                removed += 1;
            }

        return removed;
    }

    public ISavePlayerStateRule getRule(Player player, String savename) {
        ArrayList<Copmparer> list = new ArrayList<>();

        for(ISavePlayerStateRule rule: this._rules)
            list.add(new SavePlayerStateRuleManager.Copmparer(rule, player, savename));

        Collections.sort(list);

        return list.get(0).Rule;
    }

    private class Copmparer implements Comparable<Copmparer> {
        public int Value = 0;

        public ISavePlayerStateRule Rule;

        public Copmparer(ISavePlayerStateRule rule, Player player, String savename) {
            Rule = rule;
            Value = rule.getPlayerMatch(player) + rule.getNameMatch(savename);
        }

        @Override
        public int compareTo(Copmparer o) {
            return o.Value - Value;
        }
    }

    public void copyDefaultRules(Plugin plugin) throws IOException {

        //Get all files from jar
        CodeSource src = plugin.getClass().getProtectionDomain().getCodeSource();
        //and put all rules into this list
        Map<String,ZipEntry> loadlist = new HashMap<>();

        if (src == null)
            return;

        URL jar = src.getLocation();
        Bukkit.getLogger().info(jar.toString());
        ZipFile zipfile = new ZipFile(jar.getFile());
        ZipInputStream zip = new ZipInputStream(jar.openStream());

        ZipEntry ze = null;

        //Search for rules
        while( ( ze = zip.getNextEntry() ) != null ) {
            String entryName = ze.getName();
            if( entryName.matches("^defaultrules/rule_[\\w]*\\.yml$") ) {
                Bukkit.getLogger().info("[SaveState] found default rule: " + entryName);
                loadlist.put(entryName, ze);
            }
        }

        //Make sure the folder exists
        _plugin.getDataFolder().mkdirs();

        for(String filepath : loadlist.keySet()) {
            //Get directory an name
            String[] splited = filepath.split("/");

            //The destination file
            File destination = new File(_plugin.getDataFolder().getPath() + "/" + splited[1]);

            if(destination.exists())
                continue;

            Bukkit.getLogger().info("[SaveState] copy default rule: " + filepath);

            //Copy the recource to file
            InputStream in = zipfile.getInputStream(zipfile.getEntry(filepath));
            if(in != null)
                IO.copy(in, destination);
            else
                Bukkit.getLogger().warning("[SaveState] cannot find recource " + filepath);
        }
    }


}
