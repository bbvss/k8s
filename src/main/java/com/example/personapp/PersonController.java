package com.example.personapp;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
class PersonController {

    @GetMapping(path = "/persons")
    public ResponseEntity<List<Person>> getPersons() {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John"));
        persons.add(new Person("David"));
        persons.add(new Person("Peter"));
        persons.add(new Person("Spetzi"));
        persons.add(new Person("Otto"));
        return ResponseEntity.ok().body(persons);
    }

}
