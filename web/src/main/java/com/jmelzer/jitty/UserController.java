package com.jmelzer.jitty;

import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for User
 */
@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository repository;

    @RequestMapping("/users")
    public @ResponseBody
    List<User> getUserList() {
        return repository.findAll();
    }

//    @RequestMapping("/user")
//    public User users(@RequestParam(value = "id") String id) {
//        return new User("dummy", "", "");
//
//    }
}
