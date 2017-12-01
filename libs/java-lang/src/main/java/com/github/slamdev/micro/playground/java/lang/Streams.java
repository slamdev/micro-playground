package com.github.slamdev.micro.playground.java.lang;

import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

@UtilityClass
public final class Streams {

    public static <T> Stream<T> iterable(Iterable<T> iterable) {
        return stream(iterable.spliterator(), false);
    }
}
