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
package com.sreenathsofficial.stockanalyzer.controllers.api;

import com.sreenathsofficial.stockanalyzer.model.StockBasicInfo;
import com.sreenathsofficial.stockanalyzer.model.StockTimeSlice;
import com.sreenathsofficial.stockanalyzer.service.db.Storage;
import com.sreenathsofficial.stockanalyzer.service.quandl.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dataload")
public class DataLoadController {

    @Autowired
    private Api apiService;
    
    @Autowired
    private Storage storageService;
    
    @RequestMapping(method = RequestMethod.GET,  path="/full")
    public String start() {
        
        try {
            
            /**
             * Pulling stock details from api
             * AND
             * Saving those details into db.
             */
            storageService.saveNSEStockList(apiService.getNSECodeset());
            
            int count = 1;
            
            for (final StockBasicInfo stockInfo: storageService.getNSEStockList()){
                
                /**
                 * Pulling stock time series data from api.
                 */
                final List<StockTimeSlice> stockTimeSeries = apiService.getStockTimeSeries(stockInfo);

                /**
                 * Saving stock newest and oldest data available dates.
                 */
                storageService.saveNSEStockInfoRecord(stockInfo);
                
                /**
                 * Saving stock time series data.
                 */
                storageService.saveStockTimeSeries(stockTimeSeries);
                
                System.out.println(count+") Company:"+stockInfo.getCompanyName()+", Code:"+stockInfo.getStockCode()+", Status: Data load completed.");
                
                count++;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "{\"action\":\"dataload/full\", \"status\":\"completed\"}";
    }
    
    @RequestMapping(method = RequestMethod.GET,  path="/stocks")
    public String loadDatasets() {
        
        try {
            storageService.saveNSEStockList(apiService.getNSECodeset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "{\"msg\":\"Shazam\"}";
    }

    @RequestMapping(method = RequestMethod.GET,  path="/test")
    public String test() {
        return "{\"msg\":\"test\"}";
    }
    
    @RequestMapping(method = RequestMethod.GET,  path="/stocktimeseries")
    public String loadTimeSeries(@RequestParam final String stockCode
            , @RequestParam(required = false) final String year
            , @RequestParam(required = false) final String startDate
            , @RequestParam(required = false) final String endDate) {
        
        try {

            apiService.getTimeSeries(stockCode, year, startDate, endDate);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "{\"msg\":\"Shazam\"}";
    }
    
    @RequestMapping(method = RequestMethod.GET,  path="/fulltimeseries")
    public String loadAllStockHistory() {
        
        try {

            int count = 1;
            
            for (final StockBasicInfo stockInfo: storageService.getNSEStockList()){
                
                System.out.println(count+") Company:"+stockInfo.getCompanyName()+", Code:"+stockInfo.getStockCode());
                
                if("LUPIN".equalsIgnoreCase(stockInfo.getStockCode())){
                    
                    final List<StockTimeSlice> stockTimeSeries = apiService.getStockTimeSeries(stockInfo);

                    storageService.saveNSEStockInfoRecord(stockInfo);
                    storageService.saveStockTimeSeries(stockTimeSeries);
                }
                
                count++;
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "{\"msg\":\"Shazam\"}";
    }
    
}