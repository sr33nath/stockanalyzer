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
package com.sreenathsofficial.stockanalyzer.repository;

import com.sreenathsofficial.stockanalyzer.model.StockBasicInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockBasicInfoRepository extends CassandraRepository<StockBasicInfo> {

    @Query("select * from stockbasicinfo where stockcode = ?0")
    Iterable<StockBasicInfo> findByStockCode(String stockCode);

}
