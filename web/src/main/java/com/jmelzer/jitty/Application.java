package com.jmelzer.jitty;

import com.jmelzer.jitty.dao.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class Application {

    @Resource
    UserRepository userRepository;

    @RequestMapping("/resource")
    public Map<String, Object> home(HttpSession session) {
        Map<String, Object> model = new HashMap<String, Object>();
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null &&
                context.getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {

            System.out.println("    context.getAuthentication().getPrincipal() = " + context.getAuthentication().getPrincipal());
            String user = (String) context.getAuthentication().getPrincipal();
            com.jmelzer.jitty.model.User userFromDB = userRepository.findByLoginName(user);
            model.put("tname", userFromDB.getLastUsedTournament().getName());

        }
        return model;
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
