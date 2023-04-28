package com.example.spring2key.spring2key.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SecuredController {

    //secured(): This endpoint maps to the GET request for the /secured path.
    // It returns the template "secured.html"
    @GetMapping("/secured")
    public String secured() {
        return "secured";
    }

    //logout(): This endpoint maps to the GET request for the /logout path.
    // It logs out the user by calling request.logout() and then returns the template "logout.html"
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "logout";
    }

}
