package kz.hts.ce.security;

import kz.hts.ce.entity.Employee;
import kz.hts.ce.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findByUsernameAndBlocked(username, false);

        GrantedAuthority authority;
        User user;
        if (employee == null) {
            authority = new SimpleGrantedAuthority("ROLE_" + employee.getRole().getName());
            user = new User(employee.getUsername(), employee.getPassword(), Arrays.asList(authority));
        } else throw new UsernameNotFoundException("User " + username + " not found.");
        return user;
    }
}
