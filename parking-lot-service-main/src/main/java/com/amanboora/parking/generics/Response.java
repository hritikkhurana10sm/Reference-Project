package com.amanboora.parking.generics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
public class Response<T> {
    private final T body;
    private final List<String> messages;

    public Response(T body, @Nullable List<String> messages) {
        this.body = body;
        this.messages = messages;
    }
}
