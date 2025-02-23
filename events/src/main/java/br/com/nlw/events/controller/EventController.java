package br.com.nlw.events.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.nlw.events.models.Event;
import br.com.nlw.events.services.EventService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class EventController {
  private static final Logger logger = LoggerFactory.getLogger(EventController.class);
  private final EventService eventService;

  @Autowired
  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping("/events")
  @Operation(summary = "Create a new event")
  public Event addNewEvent(@RequestBody Event newEvent) {
    logger.info("Creating new event: {}", newEvent.getPrettyName());
    return eventService.addNewEvent(newEvent);
  }

  @GetMapping("/events")
  @Operation(summary = "Get all events")
  public List<Event> getAllEvents() {
    logger.info("Getting all events");
    return eventService.getAllEvents();
  }

  @GetMapping("/events/{prettyName}")
  @Operation(summary = "Get event by pretty name")
  public ResponseEntity<Event> getByPrettyName(@PathVariable String prettyName) {
    Event evt = eventService.getByPrettyName(prettyName);
    if (evt != null) {
      logger.info("Getting event by pretty name: {}", prettyName);
      return ResponseEntity.ok().body(evt);
    } else {
      logger.error("Event not found by pretty name: {}", prettyName);
      return ResponseEntity.notFound().build();
    }
  }
}
