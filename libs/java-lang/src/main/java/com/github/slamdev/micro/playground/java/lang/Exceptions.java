package com.github.slamdev.micro.playground.java.lang;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class Exceptions {

    @SneakyThrows
    public static <T, E extends Exception, K extends Exception> T transformException(Supplier<T> action,
                                                                                     Class<E> exceptionType,
                                                                                     Function<E, K> transformer) {
        try {
            return action.get();
        } catch (Exception e) {
            if (exceptionType.isInstance(e)) {
                throw transformer.apply(exceptionType.cast(e));
            } else {
                throw e;
            }
        }
    }
}
