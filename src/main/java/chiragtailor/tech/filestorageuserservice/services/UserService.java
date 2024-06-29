package chiragtailor.tech.filestorageuserservice.services;


import chiragtailor.tech.filestorageuserservice.Exceptions.UserNotFoundException;
import chiragtailor.tech.filestorageuserservice.models.User;
import chiragtailor.tech.filestorageuserservice.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User signUp(String name, String email, String password){
       User user = new User();
       user.setName(name);
       user.setEmail(email);
       user.setHashedPassord(bCryptPasswordEncoder.encode(password));

       User user1 = userRepository.save(user);
       return user1;

    }

    public User login(String email, String password) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(bCryptPasswordEncoder.matches(password, user.getHashedPassord())){
                return user;
            }
        }
        else {
            throw new UserNotFoundException("email not found");
        }


        return null;
    }
}
