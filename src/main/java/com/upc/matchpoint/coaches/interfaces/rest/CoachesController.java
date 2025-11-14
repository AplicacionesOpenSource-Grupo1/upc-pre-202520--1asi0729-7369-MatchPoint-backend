package com.upc.matchpoint.coaches.interfaces.rest;

import com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachCommand;
import com.upc.matchpoint.coaches.domain.model.queries.GetAllCoachesQuery;
import com.upc.matchpoint.coaches.domain.model.queries.GetCoachByIdQuery;
import com.upc.matchpoint.coaches.domain.services.CoachCommandService;
import com.upc.matchpoint.coaches.domain.services.CoachQueryService;
import com.upc.matchpoint.coaches.interfaces.rest.resources.CoachResource;
import com.upc.matchpoint.coaches.interfaces.rest.resources.CreateCoachResource;
import com.upc.matchpoint.coaches.interfaces.rest.resources.UpdateCoachResource;
import com.upc.matchpoint.coaches.interfaces.rest.transform.CoachResourceFromEntityAssembler;
import com.upc.matchpoint.coaches.interfaces.rest.transform.CreateCoachCommandFromResourceAssembler;
import com.upc.matchpoint.coaches.interfaces.rest.transform.UpdateCoachCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/coaches", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Coaches", description = "Coach Management Endpoints")
public class CoachesController {
    private final CoachCommandService coachCommandService;
    private final CoachQueryService coachQueryService;

    public CoachesController(CoachCommandService coachCommandService, CoachQueryService coachQueryService) {
        this.coachCommandService = coachCommandService;
        this.coachQueryService = coachQueryService;
    }

    @PostMapping
    public ResponseEntity<CoachResource> createCoach(@RequestBody CreateCoachResource resource) {
        var command = CreateCoachCommandFromResourceAssembler.toCommandFromResource(resource);
        var coach = coachCommandService.handle(command);
        return coach.map(c -> new ResponseEntity<>(CoachResourceFromEntityAssembler.toResourceFromEntity(c), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<CoachResource>> getAllCoaches() {
        var query = new GetAllCoachesQuery();
        var coaches = coachQueryService.handle(query);
        var coachResources = coaches.stream()
                .map(CoachResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(coachResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachResource> getCoachById(@PathVariable Long id) {
        var query = new GetCoachByIdQuery(id);
        var coach = coachQueryService.handle(query);
        return coach.map(c -> ResponseEntity.ok(CoachResourceFromEntityAssembler.toResourceFromEntity(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachResource> updateCoach(@PathVariable Long id, @RequestBody UpdateCoachResource resource) {
        var command = UpdateCoachCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedCoach = coachCommandService.handle(command);
        return updatedCoach.map(c -> ResponseEntity.ok(CoachResourceFromEntityAssembler.toResourceFromEntity(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoach(@PathVariable Long id) {
        var command = new DeleteCoachCommand(id);
        coachCommandService.handle(command);
        return ResponseEntity.ok("Coach deleted successfully.");
    }
}
