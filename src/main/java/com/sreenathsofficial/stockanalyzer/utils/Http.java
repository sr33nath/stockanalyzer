/*
 * Copyright (C) 2018 sreenathsofficial.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sreenathsofficial.stockanalyzer.utils;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http {
    
    public static byte[] get(final String url) throws IOException{
        
        final OkHttpClient client = new OkHttpClient();
        
        final Request request = new Request.Builder().url(url).get().build();
        final Call call = client.newCall(request);

        final Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Failed to download file: " + response);
        }

        return response.body().bytes();
    }
    
}
