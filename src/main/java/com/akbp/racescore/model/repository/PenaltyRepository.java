package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.dto.PenaltyDTO;
import com.akbp.racescore.model.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    @Query(value = "select stage.name, pen.penalty_id penaltyId, pen.description, pen.penalty_kind penaltyKind, pen.penalty_sec penaltySec, et.number, team.driver, team.co_driver coDriver\n" +
            "from race_score.penalty pen " +
            "left join race_score.event_team et on et.team_id = pen.team_id and et.event_id = :eventId " +
            "left join race_score.team team on team.team_id = pen.team_id " +
            "left join race_score.stage stage on stage.stage_id = pen.stage_id " +
            "left join race_score.penalty_dict pd on pd.id = pen.penalty_kind " +
            "where pen.stage_id in (select stage_id from race_score.stage where event_id = :eventId) " +
            "and pd.disqualification = :disqualification", nativeQuery = true)
    List<PenaltyDTO> findAllByEventIdAndDisqualification(@Param("eventId") Long eventId, @Param("disqualification") boolean disqualification);

    List<Penalty> findByStageIdAndTeamId(Long stageId, Long teamId);

    List<Penalty> findByStageIdInAndTeamId(List<Long> stagesId, Long teamId);

    void deleteByStageIdAndTeamId(Long stageId, Long teamId);
}
