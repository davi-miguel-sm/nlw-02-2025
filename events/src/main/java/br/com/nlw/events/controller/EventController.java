package br.com.nlw.events.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.nlw.events.models.Event;
import br.com.nlw.events.services.EventService;

@RestController
public class EventController {

  private final EventService eventService;

  @Autowired
  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping("/events")
  public Event addNewEvent(@RequestBody Event newEvent) {
    return eventService.addNewEvent(newEvent);
  }

  @GetMapping("/events")
  public List<Event> getAllEvents() {
    return eventService.getAllEvents();
  }

  @GetMapping("/events/{prettyName}")
  public ResponseEntity<Event> getByPrettyName(@PathVariable String prettyName) {
    Event evt = eventService.getByPrettyName(prettyName);
    if (evt != null) {
      return ResponseEntity.ok().body(evt);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
