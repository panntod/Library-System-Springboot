package panntod.core.library.library_system.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import panntod.core.library.library_system.enums.TokenType;
import panntod.core.library.library_system.enums.UserRole;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private static final String SECRET = "super_rahasia_panjang_banget_minimal_32_char!";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    private JwtUtil() {
        // prevent instantiation
    }

    private static final long ACCESS_TOKEN_EXP = 1000 * 60 * 15; // 15 minutes
    private static final long REFRESH_TOKEN_EXP = 1000 * 60 * 60 * 24 * 7; // 7 days

    public static String generateAccessToken(UUID userId, String email, UserRole role) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)
                .claim("role", role)
                .claim("type", TokenType.ACCESS)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP))
                .signWith(key)
                .compact();
    }

    public static String generateRefreshToken(UUID userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("type", TokenType.REFRESH)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP))
                .signWith(key)
                .compact();
    }

    public static Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public static UUID getUserIdFromToken(String token, TokenType expectedType) {
        Claims claims = parseToken(token).getBody();
        String tokenTypeStr = claims.get("type", String.class);

        TokenType tokenType;
        try {
            tokenType = TokenType.valueOf(tokenTypeStr); // konversi String ke Enum
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new JwtException("Invalid token type value in JWT: " + tokenTypeStr, e);
        }

        if (!expectedType.equals(tokenType)) {
            throw new JwtException("Invalid token type. Expected " + expectedType + " but got " + tokenType);
        }

        return UUID.fromString(claims.getSubject());
    }

}
