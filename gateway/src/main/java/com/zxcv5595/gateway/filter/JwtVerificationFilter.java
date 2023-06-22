package com.zxcv5595.gateway.filter;

import com.zxcv5595.gateway.filter.JwtVerificationFilter.Config;
import com.zxcv5595.gateway.type.ErrorCode;
import com.zxcv5595.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

@Component
public class JwtVerificationFilter extends AbstractGatewayFilterFactory<Config> {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    @Value("${spring.jwt.secret}")
    private String secretKey;
    public JwtVerificationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // JWT 검증 및 memberId 추출 로직 수행
            String token = resolveTokenFromRequest(exchange.getRequest());
            if (token == null || !JwtUtil.validateToken(token, secretKey)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        ErrorCode.getMessage(ErrorCode.INVALID_TOKEN));
            }

            //토큰에서 memberId get
            Integer memberId = getMemberId(token);

            //헤더에 memberId 추가
            ServerHttpRequest modifiedRequest = addHeaderMemberId(exchange, memberId);

            // 수정된 요청으로 교체
            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

            // 다음 필터 또는 라우팅 진행
            return chain.filter(modifiedExchange);
        };
    }

    private Integer getMemberId(String token) {
        Claims claims = JwtUtil.paresClaims(token, secretKey);
        Integer memberId = claims.get("memberId", Integer.class);

        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    ErrorCode.getMessage(ErrorCode.INVALID_TOKEN));
        }

        return memberId;
    }

    private ServerHttpRequest addHeaderMemberId(ServerWebExchange exchange,
            Integer memberId) {

        return exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.add("X-memberId", memberId.toString()))
                .build();
    }

    private String resolveTokenFromRequest(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get(TOKEN_HEADER);
        if (headers != null && !headers.isEmpty()) {
            String token = headers.get(0);
            if (token != null && token.startsWith(TOKEN_PREFIX)) {
                return token.substring(TOKEN_PREFIX.length());
            }
        }

        return null;
    }

    public static class Config {

    }
}

