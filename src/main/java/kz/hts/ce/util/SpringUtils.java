package kz.hts.ce.util;

import kz.hts.ce.config.UrlConfiguration;
import kz.hts.ce.security.AuthenticationService;
import kz.hts.ce.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    public static String getPrincipal() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public void authorize(String username, String password) {
        UserDetails user = authenticationService.loadUserByUsername(username);
        Authentication authToken = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        Authentication authenticate = customAuthenticationProvider.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public static UrlConfiguration getScreensConfiguration() {
        ApplicationContext context = AppContextSingleton.getInstance();
        return context.getBean(UrlConfiguration.class);
    }
}