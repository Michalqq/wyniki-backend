package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select distinct(cc.name) from race_score.team t " +
            "right join race_score.event_team et on et.team_id = t.team_id " +
            "left join race_score.car_class cc on cc.car_class_id = t.class " +
            "where et.event_id = :eventId", nativeQuery = true)
    List<String> findDistinctClasses(@Param("eventId") Long eventId);

    @Query(value = "select e.* from race_score.event e " +
            "left join race_score.event_referee er on er.event_Id = e.event_Id " +
            "left join race_score.user u on u.user_id = er.user_id " +
            "where e.event_Id = :eventId and u.username = :username", nativeQuery = true)
    Optional<Event> checkIfUserIsReferee(@Param("eventId") Long eventId, @Param("username") String username);
}
