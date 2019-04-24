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
package com.sreenathsofficial.stockanalyzer.service.quandl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sreenathsofficial.stockanalyzer.model.StockBasicInfo;
import com.sreenathsofficial.stockanalyzer.model.StockTimeSlice;
import com.sreenathsofficial.stockanalyzer.runtime.AppRuntime;
import com.sreenathsofficial.stockanalyzer.utils.DateUtil;
import com.sreenathsofficial.stockanalyzer.utils.Http;
import com.sreenathsofficial.stockanalyzer.utils.Quandl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class Api {

    @Autowired
    private Quandl quandl;
    
    @Autowired
    private AppRuntime appRuntime;

    
    public List<StockBasicInfo> getNSECodeset() throws FileNotFoundException, IOException{
        
        final String nseCodesetFilePath = appRuntime.getOperatingDir()+quandl.getNSECodesetFilename();

        final FileOutputStream fos = new FileOutputStream(nseCodesetFilePath);
        fos.write(Http.get(quandl.getNSECodesetUrl()));
        fos.close();

        return quandl.getAllStocksList(nseCodesetFilePath);

    }
    
    public List<StockTimeSlice> getStockTimeSeries(final StockBasicInfo stockInfo) throws Exception{
        
        List<StockTimeSlice> stockTimeSeries = new ArrayList<>();
        
        final JsonNode fullJson = new ObjectMapper().readTree(getTimeSeriesJSON(stockInfo.getStockCode()));
        final JsonNode dataSetJson = fullJson.get("dataset");
        final JsonNode dataJson = dataSetJson.get("data");

        final String newestRecordDate = dataSetJson.get("newest_available_date").asText();
        final String oldestRecordDate = dataSetJson.get("oldest_available_date").asText();

        stockInfo.setTimeSeriesDataAvailTo(DateUtil.getDate(newestRecordDate, quandl.getDataDateFormat()));
        stockInfo.setTimeSeriesDataAvailFrom(DateUtil.getDate(oldestRecordDate, quandl.getDataDateFormat()));
        
        //System.out.println("Newest record date:"+newestRecordDate);
        //System.out.println("Oldest record date:"+oldestRecordDate);
        //System.out.println("Number of time series records:"+dataJson.size());

        for(int i=0; i<dataJson.size(); i++){
            
            final JsonNode timeSeriesJson = dataJson.get(i);
            final StockTimeSlice stockTimeSlice = new StockTimeSlice();
            
            stockTimeSlice.setStockCode(stockInfo.getStockCode());
            
            final String recordedOn = timeSeriesJson.hasNonNull(0)?timeSeriesJson.get(0).asText():"";
            final double open = timeSeriesJson.hasNonNull(1)?timeSeriesJson.get(1).asDouble():0.0d;
            final double high = timeSeriesJson.hasNonNull(2)?timeSeriesJson.get(2).asDouble():0.0d;
            final double low = timeSeriesJson.hasNonNull(3)?timeSeriesJson.get(3).asDouble():0.0d;
            final double last = timeSeriesJson.hasNonNull(4)?timeSeriesJson.get(4).asDouble():0.0d;
            final double close = timeSeriesJson.hasNonNull(5)?timeSeriesJson.get(5).asDouble():0.0d;
            final int traded = timeSeriesJson.hasNonNull(6)?timeSeriesJson.get(6).asInt():0;
            final int turnover = timeSeriesJson.hasNonNull(7)?timeSeriesJson.get(7).asInt():0;
            
            stockTimeSlice.setRecordedDate(DateUtil.getDate(recordedOn, quandl.getDataDateFormat()));
            stockTimeSlice.setOpenValue(open);
            stockTimeSlice.setHighValue(high);
            stockTimeSlice.setLowValue(low);
            stockTimeSlice.setLastValue(last);
            stockTimeSlice.setCloseValue(close);
            stockTimeSlice.setTradedQuantity(traded);
            stockTimeSlice.setTurnOver(turnover);
          
            /*
            System.out.print("Date:"+timeSeriesJson.get(0).asText());
            System.out.print(", Open:"+timeSeriesJson.get(1).asDouble());
            System.out.print(", High:"+timeSeriesJson.get(2).asDouble());
            System.out.print(", Low:"+timeSeriesJson.get(3).asDouble());
            System.out.print(", Last:"+timeSeriesJson.get(4).asDouble());
            System.out.print(", Close:"+timeSeriesJson.get(5).asDouble());
            System.out.print(", Total Trade Quantity:"+timeSeriesJson.get(6).asInt());
            System.out.println(", Turnover (Lacs):"+timeSeriesJson.get(7).asInt());
            */

            stockTimeSeries.add(stockTimeSlice);
        }

        //System.out.println(fullJson);
        
        return stockTimeSeries;
    }

    public JsonNode getTimeSeries(final String stockCode) throws Exception{
        
        return new ObjectMapper().readTree(getTimeSeriesJSON(stockCode));
    }
    
    public String getTimeSeriesJSON(final String stockCode) throws Exception{
        
        return new String(Http.get(quandl.getNSETimeSeriesUrl(stockCode)));
    }
    
    public JsonNode getTimeSeries(final String stockCode, int year) throws Exception{
        
        return new ObjectMapper().readTree(getTimeSeriesJSON(stockCode, year));
    }
    
    public String getTimeSeriesJSON(final String stockCode, int year) throws Exception{
        
        return new String(Http.get(quandl.getNSETimeSeriesUrl(stockCode, year)));
    }
    
    public JsonNode getTimeSeries(final String stockCode, final String year) throws Exception{
        
        return new ObjectMapper().readTree(getTimeSeriesJSON(stockCode, year));
        
    }
    
    public String getTimeSeriesJSON(final String stockCode, final String year) throws Exception{
        
        return getTimeSeriesJSON(stockCode, Integer.parseInt(year));
        
    }
    
    public JsonNode getTimeSeries(final String stockCode, final String year, final String startDate, final String endDate) throws Exception{
        
        if(ObjectUtils.isEmpty(stockCode)){
            throw new Exception("Parameter stockCode is empty.");
        }
        
        if(!ObjectUtils.isEmpty(year)){
            return getTimeSeries(stockCode, year);
        }

        if(!ObjectUtils.isEmpty(startDate) && !ObjectUtils.isEmpty(endDate)){
            return getTimeSeries(stockCode, startDate, endDate);
        }

        return getTimeSeries(stockCode);
        
    }
    
    public JsonNode getTimeSeries(final String stockCode, final String startDate, final String endDate) throws Exception{
        
        return new ObjectMapper().readTree(getTimeSeriesJSON(stockCode, startDate, endDate));
        
    }
    
    public String getTimeSeriesJSON(final String stockCode, final String startDate, final String endDate) throws Exception{
        
        return new String(Http.get(quandl.getNSETimeSeriesUrl(stockCode, startDate, endDate)));
        
    }
}
