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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.sreenathsofficial.stockanalyzer.runtime.AppRuntime;
import com.sreenathsofficial.stockanalyzer.utils.json.ArrayNodeCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/find")
public class SearchController {
    
    @Autowired
    private AppRuntime appRuntime;

    
    @RequestMapping(method = RequestMethod.GET,  path="/stocks")
    public String stocks(@RequestParam String term) {

        if(ObjectUtils.isEmpty(term)){
            return "[]";
        }
        
        final String termLC = term.trim().toLowerCase();
        
        final ArrayNode jsonArr = appRuntime.getStocks().stream()
                .filter((st) -> st.getStockCodeLC().contains(termLC) || st.getCompanyNameLC().contains(termLC))
                .map(st -> JsonNodeFactory.instance.objectNode().put("label", st.getCompanyName()).put("value", st.getStockCode()))
                .collect(ArrayNodeCollector.toArrayNode());
        
        return jsonArr.toString();
    }
    
    @RequestMapping(method = RequestMethod.GET,  path="/stocktimeseries")
    public String stockTimeSeries(@RequestParam String stockCode) {

        if(ObjectUtils.isEmpty(stockCode)){
            return "[]";
        }
        
        final ArrayNode jsonArr = appRuntime.getStockTimeSeries(stockCode.trim()).stream()
                .map(sts -> JsonNodeFactory.instance.objectNode()
                        .put("stockCode", sts.getStockCode())
                        .put("date", sts.getRecordedOnFormatted())
                        .put("open", sts.getOpenValue())
                        .put("high", sts.getHighValue())
                        .put("low", sts.getLowValue())
                        .put("close", sts.getCloseValue())
                        .put("last", sts.getLastValue())
                        .put("tradedQuantity", sts.getTradedQuantity())
                        .put("turnover", sts.getTurnOver()))
                .collect(ArrayNodeCollector.toArrayNode());
        
        return jsonArr.toString();
    }
}
