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

import com.sreenathsofficial.stockanalyzer.utils.DateUtil;
import java.util.Date;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import org.springframework.util.ObjectUtils;

@Table(value = "stocktimeslice")
public class StockTimeSlice {
    
    @PrimaryKeyColumn(name = "stockcode", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String stockCode;
    
    @PrimaryKeyColumn(name = "recordedon", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private Date recordedDate;
    
    @Column(value = "openvalue")
    private double openValue;
    
    @Column(value = "highvalue")
    private double highValue;
    
    @Column(value = "lowvalue")
    private double lowValue;
    
    @Column(value = "lastvalue")
    private double lastValue;
    
    @Column(value = "closevalue")
    private double closeValue;
    
    @Column(value = "tradedquantity")
    private int tradedQuantity;
    
    @Column(value = "turnover")
    private int turnOver;
    
    @Transient
    private String recordedOnFormatted;

    public String getRecordedOnFormatted() {
        
        if(ObjectUtils.isEmpty(recordedOnFormatted)){
            setRecordedOnFormatted(DateUtil.formatDate(getRecordedDate()));
        }
        
        return recordedOnFormatted;
    }

    public void setRecordedOnFormatted(String recordedOnFormatted) {
        this.recordedOnFormatted = recordedOnFormatted;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Date getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public double getOpenValue() {
        return openValue;
    }

    public void setOpenValue(double openValue) {
        this.openValue = openValue;
    }

    public double getHighValue() {
        return highValue;
    }

    public void setHighValue(double highValue) {
        this.highValue = highValue;
    }

    public double getLowValue() {
        return lowValue;
    }

    public void setLowValue(double lowValue) {
        this.lowValue = lowValue;
    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }

    public double getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(double closeValue) {
        this.closeValue = closeValue;
    }

    public int getTradedQuantity() {
        return tradedQuantity;
    }

    public void setTradedQuantity(int tradedQuantity) {
        this.tradedQuantity = tradedQuantity;
    }

    public int getTurnOver() {
        return turnOver;
    }

    public void setTurnOver(int turnOver) {
        this.turnOver = turnOver;
    }
    
}
