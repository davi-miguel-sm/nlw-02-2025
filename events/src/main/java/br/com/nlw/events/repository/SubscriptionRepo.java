package br.com.nlw.events.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.models.Event;
import br.com.nlw.events.models.Subscription;
import br.com.nlw.events.models.User;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {
  public Subscription findByEventAndSubscriber(Event event, User user);

  @Query(value = "SELECT user_name, count(subscription_number) AS quantidade, indication_user_id" +
      " FROM db_events.tbl_subscription" +
      " INNER JOIN db_events.tbl_user" +
      " ON db_events.tbl_subscription.indication_user_id = db_events.tbl_user.user_id" +
      " WHERE indication_user_id IS NOT NULL " +
      " AND event_id = :eventId" +
      " GROUP BY indication_user_id" +
      " ORDER BY quantidade DESC;", nativeQuery = true)
  public List<SubscriptionRankingItem> generateRanking(@Param("eventId") Integer eventId);
}
