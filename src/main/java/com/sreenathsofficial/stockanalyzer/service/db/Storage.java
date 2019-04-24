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
package com.sreenathsofficial.stockanalyzer.service.db;

import com.sreenathsofficial.stockanalyzer.model.StockBasicInfo;
import com.sreenathsofficial.stockanalyzer.model.StockTimeSlice;
import com.sreenathsofficial.stockanalyzer.repository.StockBasicInfoRepository;
import com.sreenathsofficial.stockanalyzer.repository.StockTimeSliceRepository;
import com.sreenathsofficial.stockanalyzer.utils.DateUtil;
import com.sreenathsofficial.stockanalyzer.utils.Quandl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Storage {
    
    private static final Logger LOG 
      = LoggerFactory.getLogger(Storage.class);
    
    @Autowired
    private Quandl quandl;
    
    @Autowired
    private StockBasicInfoRepository stockBasicInfoRepository;

    @Autowired
    private StockTimeSliceRepository stockTimeSliceRepository;
    
    public void saveNSEStockList(final Iterable<StockBasicInfo> nseStockList){
        for(final StockBasicInfo nseStockInfoRecord: nseStockList){
           saveNSEStockInfoRecord(nseStockInfoRecord);
        }
    }

    public void saveNSEStockInfoRecord(final StockBasicInfo nseStockInfoRecord){
        stockBasicInfoRepository.save(nseStockInfoRecord);
    }
    
    public Iterable<StockBasicInfo> getNSEStockList(){
        return stockBasicInfoRepository.findAll();
    }
    
    public void saveStockTimeSeries(final Iterable<StockTimeSlice> stockTimeSeries){
        for(final StockTimeSlice stockTimeSlice: stockTimeSeries){
            saveStockTimeSlice(stockTimeSlice);
        }
    }
    
    public void saveStockTimeSlice(final StockTimeSlice stockTimeSlice){
        stockTimeSliceRepository.save(stockTimeSlice);
    }

    public Iterable<StockTimeSlice> getNSETimeSeries(){
        return stockTimeSliceRepository.findAll();
    }
    
    public List<StockTimeSlice> getNSETimeSeriesFormatted(){
        
        long startMillis = System.currentTimeMillis();
        
        LOG.info("Loading time series data...");
        
        final Iterable<StockTimeSlice> stockTimeSeriesIterable = getNSETimeSeries();
        
        long dataLoadMillis = System.currentTimeMillis();
        
        LOG.info("NSE Stock time series data: loaded in "+(dataLoadMillis-startMillis)+"ms.");
        
        List<StockTimeSlice> stockTimeSeriesList = null;
                
        if(stockTimeSeriesIterable instanceof List){

            stockTimeSeriesList = (List)stockTimeSeriesIterable;
            
            LOG.info("NSE Stock time series data: loaded "+stockTimeSeriesList.size()+" records.");
            
        }
        
        return stockTimeSeriesList;
    }
}
