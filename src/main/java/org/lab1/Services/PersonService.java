package org.lab1.Services;

import org.lab1.DTO.PersonRequest;
import org.lab1.DTO.PersonResponse;
import org.lab1.Entities.PersonEntity;
import org.lab1.Exceptions.PersonNotFoundException;
import org.lab1.Repositories.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@Service
@Transactional
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonResponse findById(Integer id) {
        PersonEntity entity = repository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        return toResponse(entity);
    }

    public PersonResponse create(PersonRequest req) {
        PersonEntity entity = new PersonEntity();
        applyRequest(entity, req);
        PersonEntity saved = repository.save(entity);
        return toResponse(saved);
    }

//    public PersonResponse update(Integer id, PersonRequest req) {
//        PersonEntity entity = repository.findById(id)
//                .orElseThrow(() -> new PersonNotFoundException(id));
//        applyRequest(entity, req);   // PATCH: перезаписываем все поля, что пришли
//        return toResponse(entity);   // save() не обязателен — @Transactional сделает flush
//    }

    public PersonResponse update(Integer id, Map<String, Object> patch) {
        PersonEntity entity = repository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        if (patch.containsKey("name")) {
            entity.setName((String) patch.get("name"));
        }
        if (patch.containsKey("age")) {
            Object raw = patch.get("age");
            entity.setAge(raw == null ? null : ((Number) raw).intValue());
        }
        if (patch.containsKey("address")) {
            entity.setAddress((String) patch.get("address"));
        }
        if (patch.containsKey("work")) {
            entity.setWork((String) patch.get("work"));
        }

        return toResponse(entity);   // @Transactional + dirty checking сохранит изменения
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new PersonNotFoundException(id);
        }
        repository.deleteById(id);
    }

    // --- мапперы и утилиты ---

    private void applyRequest(PersonEntity entity, PersonRequest req) {
        entity.setName(req.name());
        entity.setAge(req.age());
        entity.setAddress(req.address());
        entity.setWork(req.work());
    }

    private PersonResponse toResponse(PersonEntity e) {
        return new PersonResponse(
                e.getId(), e.getName(), e.getAge(), e.getAddress(), e.getWork()
        );
    }
}