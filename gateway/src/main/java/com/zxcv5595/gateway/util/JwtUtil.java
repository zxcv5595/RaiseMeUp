package com.zxcv5595.gateway.util;

import com.zxcv5595.gateway.type.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = paresClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    public Claims paresClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    ErrorCode.is(ErrorCode.INVALID_TOKEN));
        }

    }

}
