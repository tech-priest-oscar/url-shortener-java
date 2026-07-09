package techpriest.Url_Shortener.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends Base {
    @Column(name = "email")
    private String email;
    
    @Column(name = "first_name")    
    private String firstName;

    @Column(name = "last_name")    
    private String lastName;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;
}
