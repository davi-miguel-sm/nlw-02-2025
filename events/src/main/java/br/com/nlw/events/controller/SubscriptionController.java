package br.com.nlw.events.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.nlw.events.dto.ErrorMessage;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exceptions.EventNotFoundException;
import br.com.nlw.events.exceptions.SubscriptionConflictException;
import br.com.nlw.events.exceptions.UserIndicatorNotFoundException;
import br.com.nlw.events.models.User;
import br.com.nlw.events.services.SubscriptionService;

@RestController
public class SubscriptionController {

  private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

  private final SubscriptionService subscriptionService;

  @Autowired
  public SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @PostMapping({ "/subscription/{prettyname}", "/subscription/{prettyname}/{userId}" })
  public ResponseEntity<?> createSubscription(
      @PathVariable String prettyname,
      @PathVariable(required = false) Integer userId,
      @RequestBody User subscriber) {
    logger.info("Creating subscription for event: {}", prettyname);
    try {
      SubscriptionResponse res = subscriptionService.createNewSubscription(prettyname, subscriber, userId);
      if (res != null) {
        logger.info("Subscription created successfully for event: {}", prettyname);
        return ResponseEntity.ok(res);
      }
    } catch (EventNotFoundException e) {
      logger.error("Event not found: {}", prettyname, e);
      return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));

    } catch (SubscriptionConflictException e) {
      logger.error("Subscription conflict: {}", prettyname, e);
      return ResponseEntity.status(409).body(new ErrorMessage(e.getMessage()));

    } catch (UserIndicatorNotFoundException e) {
      logger.error("User indicator not found: {}", userId, e);
      return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
    }
    return ResponseEntity.badRequest().build();
  }
}