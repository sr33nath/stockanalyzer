CREATE KEYSPACE IF NOT EXISTS stockanalyzer WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
USE stockanalyzer;
CREATE TABLE stockbasicinfo (exchangecode text, stockcode text, companyname text, timeseriesavailablefrom timestamp, timeseriesavailableto timestamp, PRIMARY KEY (exchangecode, stockcode));
CREATE TABLE stocktimeslice (stockcode text, recordedon timestamp, openvalue double, highvalue double, lowvalue double, lastvalue double, closevalue double, tradedquantity int, turnover int, PRIMARY KEY (stockcode, recordedon));
