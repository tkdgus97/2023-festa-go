package com.festago.auth.infrastructure;

import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.exception.ErrorCode;
import com.festago.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

public class JwtAuthExtractor implements AuthExtractor {

    private static final String MEMBER_ID_KEY = "memberId";

    private final JwtParser jwtParser;

    public JwtAuthExtractor(String secretKey) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build();
    }

    @Override
    public AuthPayload extract(String token) {
        Claims claims = getClaims(token);
        Long memberId = claims.get(MEMBER_ID_KEY, Long.class);
        return new AuthPayload(memberId);
    }

    private Claims getClaims(String code) {
        try {
            return jwtParser.parseClaimsJws(code)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_AUTH_TOKEN);
        } catch (JwtException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_AUTH_TOKEN);
        }
    }
}