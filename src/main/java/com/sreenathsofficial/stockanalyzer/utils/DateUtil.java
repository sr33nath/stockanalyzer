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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    
    public static String OUTPUT_DATE_FORMAT = "yyyy-MM-dd";
    
    public static Date getFirstDayOfYear(int year){
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);    
        return cal.getTime();
    }

    public static String getFirstDayOfYear(int year, final String format){
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);    
        return formatDate(cal.getTime(), format);
    }
    
    public static Date getLastDayOfYear(int year){
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return cal.getTime();
    }
    
    public static String getLastDayOfYear(int year, final String format){
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return formatDate(cal.getTime(), format);
    }
    
    public static String formatDate(final Date date){
        return formatDate(date, OUTPUT_DATE_FORMAT);
    }
    
    public static String formatDate(Date date, final String format){
        return new SimpleDateFormat(format).format(date);
    }
    
    public static Date getDate(String date, final String format) throws Exception{
        final SimpleDateFormat sd = new SimpleDateFormat(format);
        sd.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sd.parse(date);
    }
}
