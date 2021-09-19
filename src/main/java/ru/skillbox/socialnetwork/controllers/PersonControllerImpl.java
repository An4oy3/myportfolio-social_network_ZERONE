package ru.skillbox.socialnetwork.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socialnetwork.model.dto.PersonRequest;
import ru.skillbox.socialnetwork.model.dto.PersonResponse;
import ru.skillbox.socialnetwork.model.dto.ResidencyRequest;
import ru.skillbox.socialnetwork.model.dto.ResidencyResponse;
import ru.skillbox.socialnetwork.services.PersonService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class PersonControllerImpl implements PersonController {

    private final PersonService userService;
    private final Logger logger = LoggerFactory.getLogger(PersonControllerImpl.class);

    @Override
    @GetMapping("/me")
    //   @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PersonResponse> getPersonDetail(Principal principal) {
        logger.info("Call GET /api/v1/users/me");
        return ResponseEntity.ok(userService.getPersonDetail(principal));
    }

    @Override
    @GetMapping("/me/residency")
    public ResponseEntity<ResidencyResponse> getPersonResidency(ResidencyRequest request) {
        logger.info("Call GET /api/v1/users/me/residency");
        return ResponseEntity.ok(userService.getPersonResidency(request));
    }

    @Override
    @PutMapping("/me")
    public ResponseEntity<PersonResponse> putPersonDetail(@Valid @RequestBody PersonRequest personRequest, Principal principal) {
        logger.info("Call PUT /api/v1/users/me");
        return ResponseEntity.ok(userService.putPersonDetail(personRequest, principal));
    }

    @Override
    @DeleteMapping("/me")
    public ResponseEntity<PersonResponse> deletePerson(Principal principal) {
        logger.info("Call DELETE /api/v1/users/me");
        return ResponseEntity.ok(userService.deletePerson(principal));
    }

}
