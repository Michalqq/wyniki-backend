package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.entity.StageScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StageScoreRepository extends JpaRepository<StageScore, Long> {

    List<StageScore> findByStageIdIn(List<Long> stagesId);

    List<StageScore> findByStageIdAndTeamId(Long stageId, Long teamId);

    List<StageScore> findByStageIdAndScoreIsNullAndDisqualifiedFalse(Long stageId);

    List<StageScore> findByStageIdAndScoreIsNotNullAndDisqualifiedFalse(Long stageId);

    List<StageScore> findByStageIdAndDisqualifiedFalse(Long stageId);

    @Query(value = "select ss.score sumScore, sum(coalesce(pen.penalty_sec, 0)) penalty, " +
            "concat(c.brand, ' ', c.model) car, c.brand, c.drive_type driveType, " +
            "et.number, team.driver, team.co_driver coDriver, team.team_name teamName, team.club, team.co_club coClub, cc.name carClass " +
            "from race_score.stage_score ss " +
            "left join race_score.team team on team.team_id = ss.team_id " +
            "left join race_score.event_team et on et.team_id = ss.team_id and et.event_id = " +
            "   (select event_id from race_score.stage where stage_id = :stageId) " +
            "left join race_score.car c on c.car_id = team.current_car_id " +
            "left join race_score.car_class cc on cc.car_class_id = et.car_class " +
            "full join race_score.penalty pen on pen.team_id = ss.team_id and pen.stage_id = :stageId " +
            "where ss.disqualified = FALSE and ss.team_id in (select team_id from race_score.stage_score " +
            "where stage_id = :stageId and score is not null ) " +
            "and ss.stage_id = :stageId and ss.score is not null " +
            "group by ss.team_id, et.number, c.drive_type, c.brand, c.model, team.driver, team.co_driver, team.team_name, team.club, team.co_club, ss.score, cc.name  " +
            "order by sumScore", nativeQuery = true)
    List<StageScoreSumDTO> findScoresInStage(@Param("stageId") Long stageId);

    @Query(value = "select sum(ss.score) sumScore, et.number, " +
            "concat(c.brand, ' ', c.model) car, c.brand, c.drive_type driveType, " +
            "team.driver, team.co_driver coDriver, team.team_name teamName, team.club, team.co_club coClub, cc.name carClass, " +
            "coalesce((select sum(coalesce(penalty_sec, 0)) from race_score.penalty " +
            "           where team_id = ss.team_id and stage_id <= :stageId " +
            "           and stage_id in (select stage_id from race_score.stage where event_id = :eventId)),0) as penalty " +
            "from race_score.stage_score ss " +
            "left join race_score.team team on team.team_id = ss.team_id " +
            "left join race_score.event_team et on et.team_id = ss.team_id and et.event_id = " +
            "   (select event_id from race_score.stage where stage_id = :stageId) " +
            "left join race_score.car_class cc on cc.car_class_id = et.car_class " +
            "left join race_score.car c on c.car_id = team.current_car_id " +
            "where ss.disqualified = FALSE and ss.team_id in (select team_id from race_score.stage_score " +
            "where stage_id = :stageId and score is not null ) " +
            "and ss.stage_id in (select stage_id from race_score.stage where event_id = :eventId) " +
            "and ss.stage_id <= :stageId and ss.score is not null " +
            "group by ss.team_id, et.number, c.drive_type, c.brand, c.model, team.driver, team.co_driver, team.team_name, team.club, team.co_club, cc.name " +
            "order by sumScore", nativeQuery = true)
    List<StageScoreSumDTO> findSummedScoreByStageId(@Param("eventId") Long eventId, @Param("stageId") Long stageId);

    List<StageScore> findByTeamIdAndStageIdGreaterThanEqual(Long teamId, Long stageId);

    @Modifying
    @Query(value = "delete from race_score.stage_score ss " +
            "where ss.team_id = :teamId and ss.stage_id in " +
            "   (select stage_id from race_score.stage where event_id = :eventId )", nativeQuery = true)
    void removeStageScoresByTeamIdAndEventId(@Param("eventId") Long eventId, @Param("teamId") Long teamId);

    @Query(value = "select * from race_score.stage_score ss " +
            "where ss.stage_id in " +
            "   (select stage_id from race_score.stage where event_id = :eventId )", nativeQuery = true)
    List<StageScore> findAllByEventId(@Param("eventId") Long eventId);

    void deleteByStageIdAndTeamId(Long eventId, Long teamId);
}
