package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select distinct(cc.name) from race_score.team t " +
            "right join race_score.event_team et on et.team_id = t.team_id " +
            "left join race_score.car_class cc on cc.car_class_id = t.class " +
            "where et.event_id = :eventId", nativeQuery = true)
    List<String> findDistinctClasses(@Param("eventId") Long eventId);
}
