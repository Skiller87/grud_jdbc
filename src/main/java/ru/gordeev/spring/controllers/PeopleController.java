package ru.gordeev.spring.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gordeev.spring.dao.PersonDAO;
import ru.gordeev.spring.models.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPersonPage(@ModelAttribute("person") Person person) {  //@ModelAttribute - аннотация, меняет поведение в зависимости от того что она аннотирует. Она может аннотировать как метод, так и атрибуты
        //model.addAttribute("person", new Person());   - это делает аннотация сама.
        return "people/new";
    }
/*    @ModelAttribute("headerMessage")
    public String headerMessage(){
        return "welcome to our website!";
    }*/

/*    @PostMapping()
    public String create(@RequestParam("name") String name, @RequestParam("surname") String surname, @RequestParam("email") String email, Model model) {

        Person person = new Person();

        person.setName(name);
        person.setSurname(surname);
        person.setEmail(email);

        model.addAttribute("person",person);
        //model.addAttribute("person", personDAO.show(id));
        return "successPage";
    }*/
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) { //@Valid включение валидации,
        // bindingResult - дает результат ошибок валидации (есть ли они или нет)
        if (bindingResult.hasErrors())
            return "people/new"; //возвращаем страницу с добавлением ошибок валидации для показа пользователю, требует правков thymeleaf
        personDAO.save(person);
        return "redirect:/people";
    }
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        personDAO.update(id,person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return "redirect:/people";
    }
    @DeleteMapping()
    public String deleteAll(){
        personDAO.deleteAll();
        return "redirect:/people";
    }
}