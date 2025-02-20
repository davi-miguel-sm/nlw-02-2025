package br.com.nlw.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.nlw.events.models.User;

public interface UserRepo extends CrudRepository<User, Integer> {

  public User findByEmail(String email);

}
