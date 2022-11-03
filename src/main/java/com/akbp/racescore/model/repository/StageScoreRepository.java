package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.entity.StageScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StageScoreRepository extends JpaRepository<StageScore, Long> {

    List<StageScore> findByStageIdAndPenaltyIsNotNull(Long stageId);

    List<StageScore> findByStageIdIn(List<Long> stagesId);

    List<StageScore> findByStageIdAndTeamId(Long stageId, Long teamId);

    List<StageScore> findByStageIdAndTeamNumber(Long stageId, Integer teamNumber);

    List<StageScore> findByStageIdAndScoreIsNullAndDisqualifiedFalseAndTeamIdIn(Long stageId, List<Long> teamIds);

    List<StageScore> findByStageIdAndScoreIsNotNullAndDisqualifiedFalseAndTeamIdIn(Long stageId, List<Long> teamIds);

    List<StageScore> findByStageIdAndDisqualifiedFalseAndTeamIdIn(Long stageId, List<Long> teamIds);

    List<StageScore> findByStageIdAndDisqualifiedFalseAndPenaltyIsNullAndTeamIdIn(Long stageId, List<Long> teamIds);

    @Query(value = "select ss.score sumScore, ss.penalty tariff, sum(coalesce(pen.penalty_sec, 0)) penalty, " +
            "concat(c.brand, ' ', c.model) car, c.brand, c.drive_type driveType, " +
            "et.number, et.driver, et.co_driver coDriver, et.team_name teamName, et.club, et.co_club coClub, cc.name carClass " +
            "from race_score.stage_score ss " +
            "left join race_score.event_team et on et.team_id = ss.team_id and et.event_id = " +
            "   (select event_id from race_score.stage where stage_id = :stageId) " +
            "left join race_score.car c on c.car_id = et.car_id " +
            "left join race_score.car_class cc on cc.car_class_id = et.car_class " +
            "full join race_score.penalty pen on pen.team_id = ss.team_id and pen.stage_id = :stageId " +
            "where ss.disqualified = FALSE and ss.team_id in (select team_id from race_score.stage_score " +
            "where stage_id = :stageId and score is not null ) " +
            "and ss.stage_id = :stageId and ss.score is not null " +
            "group by ss.team_id, ss.penalty, et.number, c.drive_type, c.brand, c.model, et.driver, et.co_driver, et.team_name, et.club, et.co_club, ss.score, cc.name  " +
            "order by sumScore", nativeQuery = true)
    List<StageScoreSumDTO> findScoresInStage(@Param("stageId") Long stageId);

    @Query(value = "select sum(ss.score) sumScore, et.number, " +
            "concat(c.brand, ' ', c.model) car, c.brand, c.drive_type driveType, " +
            "et.driver, et.co_driver coDriver, et.team_name teamName, et.club, et.co_club coClub, cc.name carClass, " +
            "(select penalty from race_score.stage_score where team_id = ss.team_id and stage_id = :stageId ) as tariff, " +
            "coalesce((select sum(coalesce(penalty_sec, 0)) from race_score.penalty " +
            "           where team_id = ss.team_id and stage_id <= :stageId " +
            "           and stage_id in (select stage_id from race_score.stage where event_id = :eventId)),0) as penalty " +
            "from race_score.stage_score ss " +
            "left join race_score.event_team et on et.team_id = ss.team_id and et.event_id = " +
            "   (select event_id from race_score.stage where stage_id = :stageId) " +
            "left join race_score.car_class cc on cc.car_class_id = et.car_class " +
            "left join race_score.car c on c.car_id = et.car_id " +
            "where ss.disqualified = FALSE and ss.team_id in (select team_id from race_score.stage_score " +
            "where stage_id = :stageId and score is not null ) " +
            "and ss.stage_id in (select stage_id from race_score.stage where event_id = :eventId) " +
            "and ss.stage_id <= :stageId and ss.score is not null " +
            "and et.team_id not in " +
            "                   (select team_id from race_score.stage_score " +
            "                       where score is null and stage_id < :stageId and stage_id in " +
            "                                               (select stage_id from race_score.stage where event_id = :eventId) ) " +
            "group by ss.team_id, et.number, c.drive_type, c.brand, c.model, et.driver, et.co_driver, et.team_name, et.club, et.co_club, cc.name " +
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

    void deleteByStageId(Long stageId);
}
