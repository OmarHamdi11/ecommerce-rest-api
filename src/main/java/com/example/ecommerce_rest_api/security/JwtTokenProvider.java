package com.example.ecommerce_rest_api.security;

import com.example.ecommerce_rest_api.common.exception.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-millisecond}")
    private long jwtExpirationDate;

    // generate JWT Token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        // بيرجع التوكن النهائي كنص (String) جاهز للإرسال للعميل

        return Jwts.builder()    // بيبدأ يبني التوكن
                .subject(username)       // بيسجّل اسم المستخدم في الـ payload (claim: sub)
                .issuedAt(new Date())    // بيحدد وقت إصدار التوكن
                .expiration(expireDate)  // بيحدد وقت انتهاء صلاحية التوكن
                .signWith(key())         // بيوقّع التوكن بالمفتاح السري
                .compact();          // بيرجع التوكن النهائي كنص (String) جاهز للإرسال للعميل

    }


    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // get username from JWT token
    public String getUsername(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token){

        try{
            // if there is a problem it will throw exception
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);

            // else it will return true
            return true;
        } catch (MalformedJwtException malformedJwtException) {
            throw new TokenException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        } catch (ExpiredJwtException expiredJwtException){
            throw new TokenException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
        } catch (UnsupportedJwtException unsupportedJwtException){
            throw new TokenException(HttpStatus.BAD_REQUEST, "Unsupported JWT Token");
        } catch (IllegalArgumentException illegalArgumentException){
            throw new TokenException(HttpStatus.BAD_REQUEST, "JWT Token claims string is null or empty");
        }

    }



}
