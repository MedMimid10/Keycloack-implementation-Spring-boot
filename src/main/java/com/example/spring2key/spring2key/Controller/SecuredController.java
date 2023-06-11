package com.example.spring2key.spring2key.Controller;

import com.example.spring2key.spring2key.Entity.TodoTask;
import com.example.spring2key.spring2key.Repository.TaskRepository;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
public class SecuredController {

    @Autowired
    private TaskRepository taskRepository;

    @PostConstruct
    public void initializeData(){
        Date now = new Date();
        taskRepository.saveAllAndFlush(List.of(
           new TodoTask(null,"wash the dishes",now,false,"hamzaeg"),
           new TodoTask(null,"wash the dishes",now,false,"hamzaeg"),
           new TodoTask(null,"wash the dishes",now,false,"user1"),
           new TodoTask(null,"Complete auth",now,false,"user1"),
           new TodoTask(null,"Do chores",now,true,"user1"),
           new TodoTask(null,"visit friends",now,true,"user1")
        ));
    }
    @GetMapping("/")
    public String notSecured(Principal principal,Model model){
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
        System.out.println(principal);
        if(principal==null){
            return "notSecured";
        }
        AccessToken accessToken = token.getAccount().getKeycloakSecurityContext().getToken();
        String username= accessToken.getPreferredUsername();
        model.addAttribute("username",username);
       return "notSecured";
    }
    //secured(): This endpoint maps to the GET request for the /secured path.
    // It returns the template "secured.html"
    @GetMapping("/secured")
    public String secured(Principal principal, Model model,@RequestParam(name = "filter", required = false) String filter, @RequestParam(name = "search",required = false)String search) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
        AccessToken accessToken = token.getAccount().getKeycloakSecurityContext().getToken();
        String username = accessToken.getPreferredUsername();
        List<TodoTask> tasks = taskRepository.findAllByUsername(username);
        if(search!=null){
            tasks = tasks.stream().filter(todoTask -> todoTask.getName().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
        }
        if(filter!=null && filter.equals("checked")){
            tasks = tasks.stream().filter((TodoTask::getChecked)).collect(Collectors.toList());
        }else if(filter!=null && filter.equals("unchecked")){
            tasks = tasks.stream().filter(todoTask -> !todoTask.getChecked()).collect(Collectors.toList());
        }
        model.addAttribute("username",username);
        model.addAttribute("tasks",tasks);
        return "secured";
    }
    @GetMapping("/task/{id}")
    public String secured(Principal principal, Model model,@PathVariable("id")Long id) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
        if(token==null) return "redirect:/";
        AccessToken accessToken = token.getAccount().getKeycloakSecurityContext().getToken();
        String username = accessToken.getPreferredUsername();
        TodoTask task = taskRepository.findById(id).orElse(null);
        if(task==null) return "notfound";
        model.addAttribute("username",username);
        model.addAttribute("task",task);
        return "edit-task";
    }

    @GetMapping("/toggle-task/{id}")
    @ResponseBody
    public String toggleTask(@PathVariable("id") Long id){
        TodoTask task = taskRepository.findById(id).orElse(null);
        if(task==null) return "";
        task.setChecked(!task.getChecked());
        taskRepository.saveAndFlush(task);
        return "done";
    }
    @GetMapping("/delete-task/{id}")
    @ResponseBody
    public String deleteTask(@PathVariable("id") Long id){
        taskRepository.deleteById(id);
        return "done";
    }
    @GetMapping("/add-task/{task}/date/{date}")
    @ResponseBody
    public String addTask(Principal principal, @PathVariable("task")String task,@PathVariable("date")String date){
        System.out.println("addTask");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date dateObject;
        try {
            dateObject = formatter.parse(date);
        } catch (ParseException e) {
            return "error";
        }
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
        AccessToken accessToken = token.getAccount().getKeycloakSecurityContext().getToken();
        String username = accessToken.getPreferredUsername();
        TodoTask todoTask = new TodoTask(null,task,dateObject,false,username);
        taskRepository.saveAndFlush(todoTask);
        return "done";
    }
    @GetMapping("/edit-task/{id}/{task}/date/{date}")
    @ResponseBody
    public String editTask(Principal principal,@PathVariable("id")Long id, @PathVariable("task")String task,@PathVariable("date")String date){
        System.out.println("addTask");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date dateObject;
        try {
            dateObject = formatter.parse(date);
        } catch (ParseException e) {
            return "error";
        }
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
        AccessToken accessToken = token.getAccount().getKeycloakSecurityContext().getToken();
        String username = accessToken.getPreferredUsername();
        TodoTask taskObject = taskRepository.findById(id).orElse(null);
        assert taskObject != null;
        taskObject.setName(task);
        taskObject.setDate(dateObject);
        taskRepository.saveAndFlush(taskObject);
        return "done";
    }

    //logout(): This endpoint maps to the GET request for the /logout path.
    // It logs out the user by calling request.logout() and then returns the template "logout.html"
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }

}
