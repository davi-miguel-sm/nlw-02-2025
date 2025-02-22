package br.com.nlw.events.services;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.dto.SubscriptionRankingByUser;
import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exceptions.EventNotFoundException;
import br.com.nlw.events.exceptions.SubscriptionConflictException;
import br.com.nlw.events.exceptions.UserIndicatorNotFoundException;
import br.com.nlw.events.models.Event;
import br.com.nlw.events.models.Subscription;
import br.com.nlw.events.models.User;
import br.com.nlw.events.repository.EventRepo;
import br.com.nlw.events.repository.SubscriptionRepo;
import br.com.nlw.events.repository.UserRepo;

@Service
public class SubscriptionService {

  private final EventRepo eventRepo;
  private final UserRepo userRepo;
  private final SubscriptionRepo subRepo;

  @Autowired
  public SubscriptionService(EventRepo eventRepo, UserRepo userRepo, SubscriptionRepo subRepo) {
    this.eventRepo = eventRepo;
    this.userRepo = userRepo;
    this.subRepo = subRepo;
  }

  public SubscriptionResponse createNewSubscription(String prettyName, User user, Integer userId) {
    Event evt = eventRepo.findByPrettyName(prettyName);
    if (evt == null) {
      throw new EventNotFoundException("O evento '" + prettyName + "' não existe.");
    }

    User userRecuperado = userRepo.findByEmail(user.getEmail());
    if (userRecuperado == null) {
      userRecuperado = userRepo.save(user);
    }

    User userIndicador = null;
    if (userId != null) {
      userIndicador = userRepo.findById(userId).orElse(null);
      if (userIndicador == null) {
        throw new UserIndicatorNotFoundException("O usuário" + userId + " não foi encontrado.");
      }
    }

    Subscription subs = new Subscription();
    subs.setEvent(evt);
    subs.setSubscriber(userRecuperado);
    subs.setIndication(userIndicador);

    Subscription subRecuperado = subRepo.findByEventAndSubscriber(evt, userRecuperado);
    if (subRecuperado != null) {
      throw new SubscriptionConflictException("O usuário " +
          userRecuperado.getName() + " já está inscrito neste evento.");
    }
    Subscription res = subRepo.save(subs);
    String urlRetorno = "htpp://codecraft.com/subscription/" + res.getEvent().getPrettyName() + "/"
        + res.getSubscriber().getId();
    return new SubscriptionResponse(res.getSubscriptionNumber(), urlRetorno);
  }

  public List<SubscriptionRankingItem> getCompleteRanking(String prettyName) {
    Event evt = eventRepo.findByPrettyName(prettyName);
    if (evt == null) {
      throw new EventNotFoundException(prettyName);
    }
    return subRepo.generateRanking(evt.getEventId());
  }

  public SubscriptionRankingByUser getRankigByUser(String prettyName, Integer userId) {

    Event evt = eventRepo.findByPrettyName(prettyName);
    if (evt == null) {
      throw new EventNotFoundException(prettyName);
    }

    List<SubscriptionRankingItem> ranking = subRepo.generateRanking(evt.getEventId());

    SubscriptionRankingItem rankingByUser = ranking
        .stream()
        .filter(user -> user.userId().equals(userId))
        .findFirst()
        .orElse(null);

    if (rankingByUser == null) {
      throw new UserIndicatorNotFoundException("Não há indicações para o usuário " + userId);
    }

    Integer posicao = IntStream.range(0, ranking.size())
        .filter(pos -> ranking.get(pos).userId().equals(userId))
        .findFirst().getAsInt();

    return new SubscriptionRankingByUser(posicao + 1, rankingByUser);
  }
}
