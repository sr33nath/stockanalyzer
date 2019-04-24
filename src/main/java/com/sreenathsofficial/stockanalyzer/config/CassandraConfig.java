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
package com.sreenathsofficial.stockanalyzer.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "com.sreenathsofficial.stockanalyzer.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {
    private static final Log LOGGER = LogFactory.getLog(CassandraConfig.class);

    @Autowired
    private Environment env;

    @Override
    protected String getKeyspaceName() {
        return env.getProperty("com.sreenathsofficial.stockanalyzer.cassandra.keyspace");
    }

    @Override
    @Bean
    public CassandraClusterFactoryBean cluster() {
        final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(env.getProperty("com.sreenathsofficial.stockanalyzer.cassandra.contactpoints"));
        cluster.setPort(Integer.parseInt(env.getProperty("com.sreenathsofficial.stockanalyzer.cassandra.port")));
        LOGGER.info("Cluster created with contact points ["  + env.getProperty("com.sreenathsofficial.stockanalyzer.cassandra.contactpoints") + "] " 
                + "& port [" + Integer.parseInt(env.getProperty("com.sreenathsofficial.stockanalyzer.cassandra.port")) + "].");
        return cluster;
    }

    @Override
    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }
}
