package chiragtailor.tech.filestorageuserservice.services;



import chiragtailor.tech.filestorageuserservice.Exceptions.PasswordNotFoundException;
import chiragtailor.tech.filestorageuserservice.models.Token;
import chiragtailor.tech.filestorageuserservice.models.User;
import chiragtailor.tech.filestorageuserservice.repositories.TokenRepository;
import chiragtailor.tech.filestorageuserservice.repositories.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@Primary
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenRepository tokenRepository;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public User signUp(String name, String email, String password){
       User user = new User();
       user.setName(name);
       user.setEmail(email);
       user.setHashedPassord(bCryptPasswordEncoder.encode(password));

       User user1 = userRepository.save(user);
       return user1;

    }
    public Token login(String email, String password) throws PasswordNotFoundException, UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User does not exist");
        }

        User user = optionalUser.get();

        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassord())){
            throw new PasswordNotFoundException("Password does not match");
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = currentDate.plusDays(30);

        // Convert LocalDate to java.util.Date
        Date expiryDateAsDate = Date.from(expiryDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String jws = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiryDateAsDate)
                .signWith(key)
                .compact();

        Token token = new Token();
        token.setUser(user);
        token.setExpiryAt(expiryDateAsDate);
        token.setValue(jws);

        return tokenRepository.save(token);
    }



    public User validateUser(String token){

        Token tkn = new Token();
        SecretKey key = Keys.hmacShaKeyFor(tkn.getValue().getBytes(StandardCharsets.UTF_8)); // Replace "your-secret-key" with your actual secret key

        try {
            // Parse the JWT token
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(key).parseClaimsJws(token);

            // If the parsing is successful and the token has not expired, the token is valid
            // You can retrieve the subject (user's email) from the JWT like this:
            String email = jws.getBody().getSubject();

            // Then, you can use this email to retrieve the corresponding User from your database
            Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtBefore(email,false,tkn.getExpiryAt());

            return optionalToken.map(Token::getUser).orElse(null);

        } catch (JwtException e) {
            // If the parsing fails or the token has expired, the token is invalid
            return null;
        }
    }
}
