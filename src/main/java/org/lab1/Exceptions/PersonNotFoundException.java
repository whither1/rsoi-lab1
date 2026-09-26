package org.lab1.Exceptions;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(Integer id) {
        super("Person not found: " + id);
    }
}
