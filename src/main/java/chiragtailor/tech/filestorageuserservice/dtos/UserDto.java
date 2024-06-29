package chiragtailor.tech.filestorageuserservice.dtos;

import chiragtailor.tech.filestorageuserservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class UserDto {

    private String email;
    private Date createdAt;


    public static UserDto from(User user){
        if (user == null ) return null;
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

}
