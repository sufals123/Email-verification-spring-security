package com.boot.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.boot.model.Student;

public class CustomUser implements UserDetails {
    
    private Student student; // Removed @Autowired from here
    
    // Constructor to initialize CustomUser with Student object
    public CustomUser(Student student) {
        this.student = student;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming Student class has a method to get roles or authorities
        // Adjust this part based on how roles are managed in your Student model
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER"); // Example authority
        
        // Return a collection of authorities (roles) for the user
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return student.getPassword(); // Assuming getPassword method exists in Student class
    }

    @Override
    public String getUsername() {
        return student.getEmail(); // Assuming getEmail method exists in Student class
    }

    // Other methods from UserDetails interface can be implemented as needed
    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement account expiration logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement account locking logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement credentials expiration logic if needed
    }

    @Override
    public boolean isEnabled() {
        return student.isEnable(); // Implement account enabled/disabled logic if needed
    }
}
