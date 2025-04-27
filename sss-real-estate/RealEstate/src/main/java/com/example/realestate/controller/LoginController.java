package com.example.realestate.controller;

import com.example.realestate.enums.Role;
import com.example.realestate.model.User;
import com.example.realestate.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
@Controller
public class LoginController implements ServletContextAware {
    @Autowired
    ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext){ this.servletContext = servletContext; }

    private String bURL;

    @PostConstruct
    public void init(){ bURL = servletContext.getContextPath(); }

    @Autowired
    UserService personService;

    @GetMapping(value = "/login")
    public ModelAndView index(){
        ModelAndView result = new ModelAndView("login");
        return result;
    }


    @PostMapping(value = "/login")
    public ModelAndView signup(HttpSession session, HttpServletResponse response, @RequestParam String email,
                               @RequestParam String password) throws IOException {

        User user = personService.findByUsernameAndPassword(email, password);


        ModelAndView result = new ModelAndView("login");

        if (user != null && user.isActive()) {
          /*  if (!person.isActive()){

                result.addObject("blockError", "The specified account has been blocked!");
                return result;
            }*/
            session.setAttribute("user", user);
            response.sendRedirect(bURL + "/realEstates");
        }
        else {

            result.addObject("error", "Wrong credentials, try again.");
        }
        return result;
    }




    @GetMapping(value = "/register")
    public ModelAndView register(){
        ModelAndView result = new ModelAndView("register");
        return result;
    }

    @PostMapping(value ="/register")
    public ModelAndView add(HttpServletResponse response, HttpSession session,
                            @RequestParam String password, @RequestParam String name,
                            @RequestParam String last, @RequestParam String email,
                            @RequestParam String phone, @RequestParam String address) throws IOException {

        ModelAndView result = new ModelAndView("register");


        if (!personService.checkIfUsernameExists(email)) {
            User user = new User(name, last, phone, address, email, password, true, Role.valueOf("USER"));
            personService.saveUser(user);
            System.out.println("Saved user");
            response.sendRedirect(bURL + "/login");
        }
            else{

            result.addObject("error", "Specified email  already exists.");
         }

        return result;
    }


    @GetMapping(value = "/registerA")
    public ModelAndView registerA(){
        ModelAndView result = new ModelAndView("registerAgent");
        return result;
    }

    @PostMapping(value ="/registerA")
    public ModelAndView addA(HttpServletResponse response, HttpSession session,
                            @RequestParam String password, @RequestParam String name,
                            @RequestParam String last, @RequestParam String email,
                            @RequestParam String phone, @RequestParam String address) throws IOException {

        ModelAndView result = new ModelAndView("registerAgent");


        if (!personService.checkIfUsernameExists(email)) {
            User user = new User(name, last, phone, address, email, password, true, Role.valueOf("AGENT"));
            personService.saveUser(user);
            System.out.println("Saved Agent");
            response.sendRedirect(bURL + "/login");
        }
        else{

            result.addObject("error", "Specified email  already exists.");
        }

        return result;
    }


    @GetMapping(value = "/registerO")
    public ModelAndView registerAO(){
        ModelAndView result = new ModelAndView("registerO");
        return result;
    }

    @PostMapping(value ="/registerO")
    public ModelAndView addAO(HttpServletResponse response, HttpSession session,
                             @RequestParam String password, @RequestParam String name,
                             @RequestParam String last, @RequestParam String email,
                             @RequestParam String phone, @RequestParam String address) throws IOException {

        ModelAndView result = new ModelAndView("registerO");


        if (!personService.checkIfUsernameExists(email)) {
            User user = new User(name, last, phone, address, email, password, true, Role.valueOf("OWNER"));
            personService.saveUser(user);
            System.out.println("Saved Owner");
            response.sendRedirect(bURL + "/login");
        }
        else{

            result.addObject("error", "Specified email  already exists.");
        }

        return result;
    }

}
