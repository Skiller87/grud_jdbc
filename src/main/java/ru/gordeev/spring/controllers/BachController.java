package ru.gordeev.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gordeev.spring.dao.PersonDAO;

import javax.print.DocFlavor;

@Controller
@RequestMapping("/test-batch-update")
public class BachController {
    private final PersonDAO personDAO;

    @Autowired
    public BachController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index() {
        return "batch/index";
    }

    @GetMapping("/without")
    public String withoutBach(){
        personDAO.testMultipleUpdate();
        return "redirect:/people";
    }
    @GetMapping("/with")
    public String withBach(){
        personDAO.testBachUpdate();
        return "redirect:/people";
    }
}
