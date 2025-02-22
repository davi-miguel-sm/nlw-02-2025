package br.com.nlw.events.exceptions;

public class EventNotFoundException extends RuntimeException {

  public EventNotFoundException(String prettyName) {
    super("O evento '" + prettyName + "' não existe.");
  }

}