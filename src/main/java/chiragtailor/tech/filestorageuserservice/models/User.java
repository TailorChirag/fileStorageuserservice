package chiragtailor.tech.filestorageuserservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User extends BaseModel{

    private String name;
    private String email;
    private String hashedPassord;

}
