package br.com.nlw.events.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.nlw.events.dto.ErrorMessage;
import br.com.nlw.events.dto.SubscriptionRankingByUser;
import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exceptions.EventNotFoundException;
import br.com.nlw.events.exceptions.SubscriptionConflictException;
import br.com.nlw.events.exceptions.UserIndicatorNotFoundException;
import br.com.nlw.events.models.User;
import br.com.nlw.events.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class SubscriptionController {
  private static final String EVENTNOTFOUND = "Event not found: ";
  private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

  private final SubscriptionService subscriptionService;

  @Autowired
  public SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @PostMapping({ "/subscription/{prettyname}", "/subscription/{prettyname}/{userId}" })
  @Operation(summary = "Create a new subscription in an event")
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
      logger.error(EVENTNOTFOUND, prettyname, e);
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

  @GetMapping("/subscription/{prettyname}/ranking")
  @Operation(summary = "Get top 3 affiliates")
  public ResponseEntity<?> getSubscriptionRanking(@PathVariable String prettyname) {
    logger.info("Getting subscription ranking for event: {}", prettyname);
    try {
      logger.info("Ranking found for event: {}", prettyname);
      List<SubscriptionRankingItem> ranking = subscriptionService
          .getCompleteRanking(prettyname)
          .subList(0, 3);

      if (ranking != null) {
        return ResponseEntity.ok(ranking);
      }

    } catch (EventNotFoundException e) {
      logger.error(EVENTNOTFOUND, prettyname, e);
      return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
    }
    return ResponseEntity.badRequest().build();
  }

  @GetMapping("/subscription/{prettyname}/ranking/{userId}")
  @Operation(summary = "Get ranking of a specific affiliate")
  public ResponseEntity<?> getSubscriptionRankingByUser(@PathVariable String prettyname, @PathVariable Integer userId) {
    logger.info("Getting subscription ranking for event: {} by user: {}", prettyname, userId);
    try {
      logger.info("Ranking found for event: {} by user: {}", prettyname, userId);
      SubscriptionRankingByUser ranking = subscriptionService.getRankigByUser(prettyname, userId);

      if (ranking != null) {
        return ResponseEntity.ok(ranking);
      }

    } catch (Exception e) {
      logger.error(EVENTNOTFOUND, prettyname, e);
      return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
    }

    return ResponseEntity.badRequest().build();
  }
}