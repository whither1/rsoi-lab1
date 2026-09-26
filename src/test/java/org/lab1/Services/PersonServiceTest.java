package org.lab1.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lab1.DTO.PersonRequest;
import org.lab1.DTO.PersonResponse;
import org.lab1.Entities.PersonEntity;
import org.lab1.Exceptions.PersonNotFoundException;
import org.lab1.Repositories.PersonRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    // --- findAll ---

    @Test
    void findAll_returnsMappedList() {
        PersonEntity e1 = person(0, "Иван", 30);
        PersonEntity e2 = person(1, "Пётр", 25);
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        List<PersonResponse> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(PersonResponse::name)
                .containsExactly("Иван", "Пётр");
    }

    // --- findById ---

    @Test
    void findById_returnsPerson_whenExists() {
        Integer id = 0;
        when(repository.findById(id)).thenReturn(Optional.of(person(id, "Иван", 30)));

        PersonResponse result = service.findById(id);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Иван");
        assertThat(result.age()).isEqualTo(30);
    }

    @Test
    void findById_throws_whenNotFound() {
        Integer id = 9929;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    // --- create ---

    @Test
    void create_savesAndReturnsResponse() {
        PersonRequest req = request("Иван", 30, "Москва", "Dev");
        when(repository.save(any(PersonEntity.class)))
                .thenAnswer(inv -> {
                    PersonEntity e = inv.getArgument(0);
                    e.setId(0);
                    return e;
                });

        PersonResponse result = service.create(req);

        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo("Иван");
        assertThat(result.age()).isEqualTo(30);
        assertThat(result.address()).isEqualTo("Москва");
        assertThat(result.work()).isEqualTo("Dev");

        verify(repository).save(any(PersonEntity.class));
    }

    // --- helpers ---

    private PersonEntity person(Integer id, String name, Integer age) {
        PersonEntity e = new PersonEntity();
        e.setId(id);
        e.setName(name);
        e.setAge(age);
        return e;
    }

    private PersonRequest request(String name, Integer age, String address, String work) {
        PersonRequest r = new PersonRequest(name, age, address, work);
        return r;
    }
}