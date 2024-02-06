package ru.itmentor.spring.boot_security.demo.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column
    private String username;

    @Column
    private String surname;

    @Column int age;

    @Column
    private String password;

    @ManyToMany
            @JoinTable(
                    name = "users_roles",
                    joinColumns = @JoinColumn(name = "users_id"),
                    inverseJoinColumns = @JoinColumn(name = "roles_id")
            )
    Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String password,Set<Role> roles, String surname, int age) {
        this.username = username;
        this.surname = surname;
        this.age = age;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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
}
