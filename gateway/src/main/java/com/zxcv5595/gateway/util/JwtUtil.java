package com.zxcv5595.gateway.util;

import com.zxcv5595.gateway.type.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

public class JwtUtil {

    public static boolean validateToken(String token, String secretKey) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = paresClaims(token, secretKey);
        return !claims.getExpiration().before(new Date());
    }

    public static Claims paresClaims(String token, String secretKey) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    ErrorCode.getMessage(ErrorCode.INVALID_TOKEN));
        }

    }

}
