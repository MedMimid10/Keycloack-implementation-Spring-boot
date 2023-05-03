package com.example.spring2key.spring2key.Controller;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class SecuredController {

    @GetMapping("/")
    public String notSecured(){
       return "notSecured";
    }
    //secured(): This endpoint maps to the GET request for the /secured path.
    // It returns the template "secured.html"
    @GetMapping("/secured")
    public String secured(Principal principal, Model model) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
        AccessToken accessToken = token.getAccount().getKeycloakSecurityContext().getToken();
        String username = accessToken.getPreferredUsername();
        model.addAttribute("username",username);
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
