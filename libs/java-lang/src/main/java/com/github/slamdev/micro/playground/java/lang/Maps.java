package com.github.slamdev.micro.playground.java.lang;

import lombok.experimental.UtilityClass;

import java.util.AbstractMap.SimpleEntry;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public final class Maps {

    public static <T, K extends Enum<K>, U> Collector<T, ?, EnumMap<K, U>> toEnumMap(
            Class<K> type,
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper
    ) {
        return Collectors.toMap(keyMapper, valueMapper, throwingMerger(), () -> new EnumMap<>(type));
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        };
    }

    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new SimpleEntry<>(key, value);
    }

    private static <K, U> Collector<Map.Entry<K, U>, ?, Map<K, U>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    @SafeVarargs
    public static <K, V> Map<K, V> of(Map.Entry<K, V>... entries) {
        return Stream.of(entries).collect(toMap());
    }
}
