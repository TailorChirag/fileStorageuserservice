package chiragtailor.tech.filestorageuserservice.controllers;


import chiragtailor.tech.filestorageuserservice.Exceptions.UserNotFoundException;
import chiragtailor.tech.filestorageuserservice.dtos.LoginRequestDto;
import chiragtailor.tech.filestorageuserservice.dtos.SignUpRequestDto;
import chiragtailor.tech.filestorageuserservice.models.User;
import chiragtailor.tech.filestorageuserservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public User signUp(@RequestBody() SignUpRequestDto request)  {

        String name = request.getName();
        String email = request.getEmail();
        String password = request.getPassword();

        return userService.signUp(name,email,password);
    }


    @PostMapping("/login")
    public User login(@RequestBody() LoginRequestDto requestDto) throws UserNotFoundException {
        return userService.login(requestDto.getEmail(), requestDto.getPassword());
    }

}
