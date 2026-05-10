package com.zmpc.utils;

import com.zmpc.entity.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class OutputObjectUtilsTest {

    @Test
    void demoOutputPersonToStringShort() {
        Person person = getPerson();
        OutputObjectUtils.printObjectShort(person);
    }

    @Test
    void demoOutputPersonToString() {
        Person person = getPerson();
        OutputObjectUtils.printObject(person);
    }

    @Test
    void demoOutputListOfPersonsToString() {
        List<Person> personsList = getPersonsList();
        OutputObjectUtils.printObject(personsList);
    }

    private Person getPerson() {
        return new Person("Alex", "Show", 32, "Kiev, Ukraine", LocalDate.now(), LocalDateTime.now());
    }

    private List<Person> getPersonsList() {
        return List.of(
                new Person("Alex", "Show", 32, null, LocalDate.now(), LocalDateTime.now()),
                new Person("Igor", "Sliv", 31, null, LocalDate.now(), LocalDateTime.now()),
                new Person("Lena", "Savch", 30, "Kiev", LocalDate.now(), LocalDateTime.now()),
                new Person("Olga", "Mirosh", 28, null, LocalDate.now(), LocalDateTime.now())
        );
    }

}