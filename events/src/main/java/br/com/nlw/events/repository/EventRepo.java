package br.com.nlw.events.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.repository.CrudRepository;
import br.com.nlw.events.models.Event;

public interface EventRepo extends CrudRepository<Event, Integer> {

  Event findByPrettyName(String prettyName);

  Event findByTitle(String title);

  Event findByLocation(String location);

  Event findByPrice(Double price);

  Event findByStartDate(LocalDate startDate);

  Event findByEndDate(LocalDate endDate);

  Event findByStartTime(LocalTime startTime);

  Event findByEndTime(LocalTime endTime);

}
