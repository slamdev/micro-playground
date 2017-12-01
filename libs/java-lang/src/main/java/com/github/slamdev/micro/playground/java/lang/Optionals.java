package com.github.slamdev.micro.playground.java.lang;

import lombok.experimental.UtilityClass;

import java.util.EnumSet;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Locale.ROOT;

@UtilityClass
public final class Optionals {

    public static OptionalInt integer(Integer value) {
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    public static <T> Optional<T> integer(T object, Predicate<T> checker) {
        return checker.test(object) ? Optional.of(object) : Optional.empty();
    }

    public static <T extends Enum<T>> Optional<T> enumeration(Class<T> enumClass, String value) {
        if (value == null) {
            return Optional.empty();
        }
        return EnumSet.allOf(enumClass).stream()
                .filter(t -> t.name().toLowerCase(ROOT).equals(value.toLowerCase(ROOT)))
                .findAny();
    }

    public static Optional<String> string(String string) {
        return string(string, String::trim);
    }

    public static Optional<String> string(String string, Function<String, String> transformer) {
        if (string != null) {
            string = transformer.apply(string);
            if (string.isEmpty()) {
                return Optional.of(string);
            }
        }
        return Optional.empty();
    }
}
