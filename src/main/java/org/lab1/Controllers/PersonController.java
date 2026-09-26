package org.lab1.Controllers;

import jakarta.validation.Valid;
import org.lab1.DTO.PersonRequest;
import org.lab1.DTO.PersonResponse;
import org.lab1.Services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public List<PersonResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PersonResponse get(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(
            @Valid @RequestBody PersonRequest req,
            UriComponentsBuilder uriBuilder
    ) {
        PersonResponse created = service.create(req);
        URI location = uriBuilder.path("/api/v1/persons/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);  // 201 + Location
    }

    @PatchMapping("/{id}")
    public PersonResponse patch(@PathVariable Integer id,
                                @RequestBody Map<String, Object> patch) {
//                                        PersonRequest req) {
        return service.update(id, patch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();  // 204
    }
}