package com.example.personapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getPersons() {
        List<Person> persons = restTemplate.getForObject("/persons", List.class);
        assertTrue(persons.size() == 4);
//        assertEquals("[{name=John}, {name=David}, {name=Peter}, {name=Walter}]", persons.toString());
    }
}