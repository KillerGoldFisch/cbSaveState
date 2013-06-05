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
 *
 * Authors: Kevin Gliewe
 * Date: 2013-06-02
 *
 * Changelog:
 *    -
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HTTP {
    public static String POST(URL url, Map<String, Object> vars) throws IOException {
        StringBuilder body = new StringBuilder();

        boolean first = true;

        for(String key : vars.keySet()) {
            if(!first)
                body.append('&');
            else
                first = false;

            String value = "null";
            if(vars.get(key) != null)
                value = vars.get(key).toString();

            body.append(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8"));
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod( "POST" );
        connection.setDoInput( true );
        connection.setDoOutput( true );
        connection.setUseCaches( false );
        connection.setRequestProperty( "Content-Type",
                "application/x-www-form-urlencoded" );
        connection.setRequestProperty( "Content-Length", String.valueOf(body.length()) );

        OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );
        writer.write( body.toString() );
        writer.flush();


        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()) );

        StringBuilder out = new StringBuilder();

        first = true;

        for ( String line; (line = reader.readLine()) != null; )
        {
            if(!first)
                out.append('\n');
            else
                first = false;

            out.append(line);
        }

        writer.close();
        reader.close();

        return out.toString();
    }
}
