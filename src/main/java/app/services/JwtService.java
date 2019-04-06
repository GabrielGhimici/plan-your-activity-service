package app.services;

import app.response.Login;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    @Value("${jwt.expire.minutes}")
    private Long expireMinutes;

    @Value("${jwt.token.secret}")
    private String plainSecret;
    private String encodedSecret;

    @PostConstruct
    protected void init() {
        this.encodedSecret = generateEncodedSecret(this.plainSecret);
    }

    protected String generateEncodedSecret(String plainSecret)
    {
        if (StringUtils.isEmpty(plainSecret))
        {
            throw new IllegalArgumentException("JWT secret cannot be null or empty.");
        }
        return Base64
                .getEncoder()
                .encodeToString(this.plainSecret.getBytes());
    }

    protected Date getExpirationTime()
    {
        Date now = new Date();
        Long expireInMilis = TimeUnit.MINUTES.toMillis(expireMinutes);
        return new Date(expireInMilis + now.getTime());
    }

    protected Login getUser(String encodedSecret, String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(encodedSecret)
                .parseClaimsJws(token)
                .getBody();
        String userName = claims.getSubject();
        Login securityUser = new Login();
        securityUser.setUserName(userName);
        return securityUser;
    }

    public Login getUser(String token)
    {
        return getUser(this.encodedSecret, token);
    }

    protected String getToken(String encodedSecret, Login jwtUser)
    {
        Date now = new Date();
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(jwtUser.getUserName())
                .setIssuedAt(now)
                .setExpiration(getExpirationTime())
                .signWith(SignatureAlgorithm.HS512, encodedSecret)
                .compact();
    }

    public String getToken(Login jwtUser)
    {
        return getToken(this.encodedSecret, jwtUser);
    }
}