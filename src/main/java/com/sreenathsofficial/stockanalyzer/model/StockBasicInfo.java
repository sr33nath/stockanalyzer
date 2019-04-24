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
package com.sreenathsofficial.stockanalyzer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "stockbasicinfo")
public class StockBasicInfo {

    @PrimaryKeyColumn(name = "exchangecode", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String exchangeCode;
    
    @PrimaryKeyColumn(name = "stockcode", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String stockCode;
    
    @Column(value = "companyname")
    private String companyName;

    @Column(value = "timeseriesavailablefrom")
    private Date timeSeriesDataAvailFrom;

    @Column(value = "timeseriesavailableto")
    private Date timeSeriesDataAvailTo;
    
    public StockBasicInfo() {
    }
    
    public StockBasicInfo(final String exchangeCode, final String stockCode, final String companyName) {
        this.exchangeCode = exchangeCode;
        this.stockCode = stockCode;
        this.companyName = companyName;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public String getStockCode() {
        return stockCode;
    }

    public String getStockCodeLC() {
        return stockCode.toLowerCase();
    }

    public void setStockCode(String stockCode) {
        System.out.println(stockCode);
        this.stockCode = stockCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyNameLC() {
        return companyName.toLowerCase();
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getTimeSeriesDataAvailFrom() {
        return timeSeriesDataAvailFrom;
    }

    public void setTimeSeriesDataAvailFrom(Date timeSeriesDataAvailFrom) {
        this.timeSeriesDataAvailFrom = timeSeriesDataAvailFrom;
    }

    public Date getTimeSeriesDataAvailTo() {
        return timeSeriesDataAvailTo;
    }

    public void setTimeSeriesDataAvailTo(Date timeSeriesDataAvailTo) {
        this.timeSeriesDataAvailTo = timeSeriesDataAvailTo;
    }

    public static StockBasicInfo fromJSON(final String jsonString) throws IOException{
        
        final ObjectMapper objectMapper = new ObjectMapper();
        final StockBasicInfo stockBasicInfo = objectMapper.readValue(jsonString, StockBasicInfo.class);
        
        /*
        System.out.println("{\"exchangeCode\":\""
                +stockBasicInfo.getExchangeCode()
                +"\",\"stockCode\":\""
                +stockBasicInfo.getStockCode()
                +"\",\"companyName\":\""
                +stockBasicInfo.getCompanyName()+"\"}");
        */
        
        return stockBasicInfo;
    }
}
