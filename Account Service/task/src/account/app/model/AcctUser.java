package account.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
public class AcctUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastname;

    @NotEmpty
    @Pattern(regexp = ".*@acme.com")
    @ColumnTransformer(write = "LOWER(?)")
    private String email;

    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @NotEmpty
    private String password;

    //    @Enumerated(EnumType.STRING)
//    @ManyToMany
//    @JoinTable(
//            name = "acctUser_role",
//            joinColumns = @JoinColumn(name = "acctUser_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    @ElementCollection
    @Lob
    private EnumSet<ROLE> roles;
//    private List<ROLE> roles;

    public EnumSet<ROLE> getRoles() {
        List<ROLE> collect = roles.stream().sorted().collect(Collectors.toList());
        return EnumSet.copyOf(collect);
    }

    public void setRoles(EnumSet<ROLE> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AcctUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
