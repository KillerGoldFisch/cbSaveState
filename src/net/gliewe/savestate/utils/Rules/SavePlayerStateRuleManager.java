package net.gliewe.savestate.utils.Rules;

import net.gliewe.savestate.SaveStatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 *      no changes
 */

public class SavePlayerStateRuleManager {

    private static SavePlayerStateRuleManager _instance = null;

    public static SavePlayerStateRuleManager getInstance() { return _instance; }

    public static void INIT(SaveStatePlugin plugin) {
        _instance = new SavePlayerStateRuleManager(plugin);
    }

    private List<ISavePlayerStateRule> _rules = new ArrayList<>();

    public SavePlayerStateRuleManager(SaveStatePlugin plugin) {
        registerRule(new SavePlayerStateDefaultRule());

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
}
