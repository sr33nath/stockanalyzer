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
package com.sreenathsofficial.stockanalyzer.runtime;

import com.sreenathsofficial.stockanalyzer.model.StockBasicInfo;
import com.sreenathsofficial.stockanalyzer.model.StockTimeSlice;
import com.sreenathsofficial.stockanalyzer.service.db.Storage;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("appRuntime")
public class AppRuntime {
    
    private static final Logger LOG 
      = LoggerFactory.getLogger(AppRuntime.class);

    final private int INIT_STATE_SUCCESS = 0;
    final private int INIT_STATE_FAILED = 1;
    
    private int runtimeInitState;
    private String runtimeInitStatus;

    private String operatingDir;

    @Autowired
    private Storage storageService;

    
    private AppRuntime() {
        
        /**
         * Operating directory to be used for temporary storage of generated/downloaded JSON files.
         * Eg: /tmp/stockanalyzer/run-18735875382/
         * 
         * App only works on linux machines because it expects /tmp
         */
        
        LOG.info("Initializing runtime object.");
        
        operatingDir = File.separator
                +"tmp"
                +File.separator
                +"stockanalyzer"
                +File.separator
                +"run-"
                +System.currentTimeMillis()
                +File.separator;
        
        initializeRuntimeConfig();
    }
    
    private void initializeRuntimeConfig(){
        
        try {
            
            final File appRuntimeOperatingDir = new File(operatingDir);
            appRuntimeOperatingDir.mkdirs();
            
            runtimeInitState = INIT_STATE_SUCCESS;
                    
        } catch (Exception e) {
            
            runtimeInitState = INIT_STATE_FAILED;
            runtimeInitStatus = e.getMessage();
            
            e.printStackTrace();
        }
        
    }
    
    public boolean isRuntimeInitialized(){
        return (runtimeInitState == INIT_STATE_SUCCESS);
    }
    
    public String getRuntimeInitStatus(){
        return runtimeInitStatus;
    }
    
    public String getOperatingDir(){
        return operatingDir;
    }
    
    private List<StockBasicInfo> stocks;
    
    public AppRuntime loadStockData(){
        
        try {

            final Iterable<StockBasicInfo> stockList = storageService.getNSEStockList();
            if(stockList instanceof List){
                stocks = (List)stockList;
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        return this;
    }

    public List<StockBasicInfo> getStocks() {
        return stocks;
    }

    private List<StockTimeSlice> stockTimeSeries;
    
    public AppRuntime loadStockTimeSeriesData(){

        try {
            stockTimeSeries = storageService.getNSETimeSeriesFormatted();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public List<StockTimeSlice> getStockTimeSeries(final String stockCode) {
        return stockTimeSeries.stream()
                .filter(sts -> sts.getStockCode().equalsIgnoreCase(stockCode))
                .collect(Collectors.toList());
    }
    
}
