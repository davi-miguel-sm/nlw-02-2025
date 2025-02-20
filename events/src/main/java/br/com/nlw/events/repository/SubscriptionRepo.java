package br.com.nlw.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.nlw.events.models.Subscription;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {

  public Subscription findByEmail(String email);

}
