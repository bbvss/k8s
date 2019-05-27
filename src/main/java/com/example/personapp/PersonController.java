package com.example.personapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
class PersonController {

    @GetMapping(path = "/persons")
    public String getPersons(Model model) {
        model.addAttribute("persons", Arrays.asList("John", "David", "Peter"));
        return "persons";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/";
    }
}
