package br.com.nlw.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.nlw.events.models.Event;

public interface EventRepo extends CrudRepository<Event, Integer> {

  public Event findByPrettyName(String prettyName);

}
