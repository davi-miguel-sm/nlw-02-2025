package br.com.nlw.events.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.models.Event;
import br.com.nlw.events.repository.EventRepo;

@Service
public class EventService {

  private final EventRepo eventRepo;

  @Autowired
  public EventService(EventRepo eventRepo) {
    this.eventRepo = eventRepo;
  }

  public Event addNewEvent(Event event) {
    // gerando pretty-name
    event.setPrettyName(event.getTitle().toLowerCase().replace(" ", "-"));
    return eventRepo.save(event);
  }

  public List<Event> getAllEvents() {
    return (List<Event>) eventRepo.findAll();
  }

  public Event getByPrettyName(String prettyName) {
    return eventRepo.findByPrettyName(prettyName);
  }
}
