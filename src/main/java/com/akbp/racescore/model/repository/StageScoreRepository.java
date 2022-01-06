package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.entity.StageScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StageScoreRepository extends JpaRepository<StageScore, Long> {

    StageScore findByStageIdAndTeamId(Long stageId, Long teamId);

    List<StageScore> findByStageIdAndScoreIsNull(Long stageId);

    List<StageScore> findByStageIdAndScoreIsNotNull(Long stageId);

    @Query(value = "select  ss.score sumScore, sum(coalesce(pen.penalty_sec, 0)) penalty, " +
            "ss.team_number number, team.car, team.driver, team.co_driver coDriver, team.team_name teamName, cc.name carClass " +
            "from race_score.stage_score  ss " +
            "left join race_score.team team on team.team_id = ss.team_id " +
            "left join race_score.car_class cc on cc.car_class_id = team.class " +
            "full join race_score.penalty pen on pen.team_id = ss.team_id  and pen.stage_id = :stageId " +
            "where ss.team_id in (select team_id from race_score.stage_score " +
            "where stage_id = :stageId and score is not null ) " +
            "and ss.stage_id = :stageId " +
            "group by ss.team_id, ss.team_number, team.car, team.driver, team.co_driver, team.team_name,  ss.score, cc.name  " +
            "order by sumScore", nativeQuery = true)
    List<StageScoreSumDTO> findScoresInStage(@Param("stageId") Long stageId);

    @Query(value = "select sum(ss.score) sumScore, ss.team_number number, " +
            "team.car, team.driver, team.co_driver coDriver, team.team_name teamName, cc.name carClass, " +
            "coalesce((select sum(coalesce(penalty_sec, 0)) from race_score.penalty " +
            "           where team_id = ss.team_id and stage_id <= :stageId " +
            "           and stage_id in (select stage_id from race_score.stage where event_id = :eventId)),0) as penalty " +
            "from race_score.stage_score  ss " +
            "left join race_score.team team on team.team_id = ss.team_id " +
            "left join race_score.car_class cc on cc.car_class_id = team.class " +
            "where ss.team_id in (select team_id from race_score.stage_score " +
            "where stage_id = :stageId and score is not null ) " +
            "and ss.stage_id in (select stage_id from race_score.stage where event_id = :eventId) " +
            "and ss.stage_id <= :stageId " +
            "group by ss.team_id, ss.team_number, team.car, team.driver, team.co_driver, team.team_name, cc.name " +
            "order by sumScore", nativeQuery = true)
    List<StageScoreSumDTO> findSummedScoreByStageId(@Param("eventId") Long eventId, @Param("stageId") Long stageId);
}
