package net.gliewe.savestate.utils;

/**
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
 *
 * Authors: Kevin Gliewe
 * Date: 2013-06-02
 *
 * Changelog:
 *    -
 */

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ErrorReporter {

    private static final String REPORTSERVER = "http://gliewe.net/webutils/default/bukkitreport.json";

    private Plugin _plugin = null;

    private boolean _active = true;

    public void setActive(boolean active) { _active = active; }
    public boolean getActive() { return _active; }

    public ErrorReporter(Plugin plugin, boolean active) {
        this._plugin = plugin;
        this._active = active;
    }

    public boolean report(final Exception ex) {
        if(!this._active)
            return false;


        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();

                synchronized (ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);

                    map.put("pluginname", _plugin.getName());
                    map.put("pluginversion", _plugin.getDescription().getVersion());
                    map.put("bukkitversion", Bukkit.getBukkitVersion());
                    map.put("exceptionname", ex.getMessage());
                    map.put("exceptionstacktrace", sw.toString());

                    map.put("additional", "");
                }

                try {
                    HTTP.POST(new URL(REPORTSERVER), map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return true;
    }

}
