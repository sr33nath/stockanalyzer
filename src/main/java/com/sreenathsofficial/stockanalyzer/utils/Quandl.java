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

import com.sreenathsofficial.stockanalyzer.model.StockBasicInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Quandl {
    
    final public static String API_DB_BASE_URL_PROP = "com.sreenathsofficial.stockanalyzer.quandl.api.db.baseUrl";
    final public static String API_DATASET_BASE_URL_PROP = "com.sreenathsofficial.stockanalyzer.quandl.api.dataset.baseUrl";
    final public static String API_KEY_PROP = "com.sreenathsofficial.stockanalyzer.quandl.api.key";
    final public static String API_DATA_DATE_FORMAT_PROP = "com.sreenathsofficial.stockanalyzer.quandl.data.date.format";
    
    @Autowired
    private Environment env;

    public String getDbApiBaseUrl(){
        return env.getProperty(API_DB_BASE_URL_PROP);
    }

    public String getDatasetApiBaseUrl(){
        return env.getProperty(API_DATASET_BASE_URL_PROP);
    }
    
    public String getApiKey(){
        return env.getProperty(API_KEY_PROP);
    }

    public String getDataDateFormat(){
        return env.getProperty(API_DATA_DATE_FORMAT_PROP);
    }
    
    public String getNSECodesetFilename(){
        return "NSECodeset.zip";
    }
    
    public String getNSECodesetUrl(){
        return getDbApiBaseUrl()+"NSE/codes?api_key="+getApiKey();
    }

    public String getNSETimeSeriesUrl(final String stockCode){
        return getDatasetApiBaseUrl()+"NSE/"+stockCode+".json?api_key="+getApiKey();
    }

    public String getNSETimeSeriesUrl(final String stockCode, int year){
         return getNSETimeSeriesUrl(stockCode, 
                 DateUtil.getFirstDayOfYear(year, getDataDateFormat()), 
                 DateUtil.getLastDayOfYear(year, getDataDateFormat()));
    }

    public String getNSETimeSeriesUrl(final String stockCode, final String startDate){
        return getNSETimeSeriesUrl(stockCode, startDate, startDate);
    }
    
    public String getNSETimeSeriesUrl(final String stockCode, final String startDate, final String endDate){
        return getDatasetApiBaseUrl()+"NSE/"+stockCode+".json?start_date="+startDate+"&end_date="+endDate+"&api_key="+getApiKey();
    }
    
    public List<StockBasicInfo> getAllStocksList(final String absoluteNseCodesetFilePath) throws IOException{
        return getAllStocksList(new ZipFile(absoluteNseCodesetFilePath));
    }
    
    public List<StockBasicInfo> getAllStocksList(final ZipFile nseCodesetFile) throws IOException{
        
        List<String> lines = null;
        
        final Enumeration<? extends ZipEntry> entries = nseCodesetFile.entries();
        
        if(entries.hasMoreElements()){
            
            final ZipEntry entry = entries.nextElement();
            final InputStream stream = nseCodesetFile.getInputStream(entry);
            
            final BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            
            lines = br.lines().collect(Collectors.toList());
            
            br.close();
        }
        
        return stockInfoStringToObject(lines);
    }
    
    private List<StockBasicInfo> stockInfoStringToObject(final List<String> lines) throws IOException{
        
        final List<StockBasicInfo> stockInfoList = new ArrayList<>();
        
        for (final String line : lines) {
            
            final String exchangeCode = line.substring(0, line.indexOf('/'));
            final String stockCode = line.substring(line.indexOf('/')+1, line.indexOf(','));
            final String companyName = line.substring(line.indexOf(',')+1, line.length());
            
            stockInfoList.add(new StockBasicInfo(exchangeCode, stockCode, companyName));
            
            //final String stockInfoJson = "{\"exchangeCode\":\""+exchangeCode+"\",\"stockCode\":\""+stockCode+"\",\"companyName\":\""+companyName+"\"}";
            
            //stockInfoList.add(StockBasicInfo.fromJSON(stockInfoJson));
            
            //System.out.println(stockInfoJson);
        }
        
        return stockInfoList;
    }
}
