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
package com.sreenathsofficial.stockanalyzer.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ArrayNodeCollector implements Collector<JsonNode, ArrayNode, ArrayNode> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Supplier<ArrayNode> supplier() {
        return mapper::createArrayNode;
    }

    @Override
    public BiConsumer<ArrayNode, JsonNode> accumulator() {
        return ArrayNode::add;
    }

    @Override
    public BinaryOperator<ArrayNode> combiner() {
        return (x, y) -> {
            x.addAll(y);
            return x;
        };
    }

    @Override
    public Function<ArrayNode, ArrayNode> finisher() {
        return accumulator -> accumulator;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }

    public static ArrayNodeCollector toArrayNode(){
        return new ArrayNodeCollector();
    }
}
