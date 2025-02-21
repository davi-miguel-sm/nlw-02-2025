package br.com.nlw.events.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.nlw.events.models.User;

public interface UserRepo extends CrudRepository<User, Integer> {

  public User findByEmail(String email);

  public Optional<User> findById(Integer id);

}
