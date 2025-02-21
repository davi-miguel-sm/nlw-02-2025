package br.com.nlw.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.nlw.events.models.Event;
import br.com.nlw.events.models.Subscription;
import br.com.nlw.events.models.User;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {
  public Subscription findByEventAndSubscriber(Event event, User user);
}
