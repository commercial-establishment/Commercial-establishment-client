package kz.hts.ce.security;

import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        Employee employee = employeeService.findByUsernameAndBlocked(username, false);
        String password = authentication.getCredentials().toString();
        if (employee.getUsername().equals(username) && encoder.matches(password, employee.getPassword())) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + employee.getRole().getName()));
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else {
            return null;
        }
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}