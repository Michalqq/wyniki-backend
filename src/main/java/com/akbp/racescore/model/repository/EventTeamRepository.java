package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.EventTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventTeamRepository extends JpaRepository<EventTeam, Long> {

    @Query(value = "SELECT COALESCE(MAX(number),0) number FROM race_score.event_team WHERE event_id = :eventId", nativeQuery = true)
    int getMaxNumberByEventId(@Param("eventId") Long eventId);

    void deleteByEventIdAndTeamId(Long eventId, Long teamId);

    EventTeam findByEventIdAndTeamId(Long eventId, Long teamId);
}
