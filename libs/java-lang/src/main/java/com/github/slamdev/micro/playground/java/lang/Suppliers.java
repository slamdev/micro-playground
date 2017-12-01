package com.github.slamdev.micro.playground.java.lang;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class Suppliers {

    public static <T> Supplier<T> lazily(Supplier<T> supplier) {
        return new Supplier<T>() {
            private T value;

            @Override
            public T get() {
                if (value == null) {
                    value = supplier.get();
                }
                return value;
            }
        };
    }
}
