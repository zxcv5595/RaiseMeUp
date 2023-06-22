package com.zxcv5595.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        var response = exchange.getResponse();

        if (ex instanceof ResponseStatusException responseStatusException) {
            HttpStatus status = (HttpStatus) responseStatusException.getStatusCode();
            log.error("'Unauthorized': {}", responseStatusException.getMessage());
            response.setStatusCode(status);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return response.writeWith(Mono.just(response.bufferFactory()
                    .wrap(responseStatusException.getMessage().getBytes())));
        }

        // ResponseStatusException 제외 한, 그외 에러
        log.error("An error occurred: {}", ex.getMessage());
        return Mono.error(ex);
    }
}