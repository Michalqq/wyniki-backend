package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.PenaltyByTeamDTO;
import com.akbp.racescore.model.dto.PenaltyDTO;
import com.akbp.racescore.model.entity.Penalty;
import com.akbp.racescore.model.repository.PenaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PenaltyService {
    private final PenaltyRepository penaltyRepository;

    @Autowired
    public PenaltyService(PenaltyRepository penaltyRepository) {
        this.penaltyRepository = penaltyRepository;
    }

    public Long addPenalty(Penalty penalty) {
        penaltyRepository.save(penalty);
        return 1L;
    }

    public List<PenaltyByTeamDTO> getPenalties(Long eventId) {
        List<PenaltyDTO> penalties = penaltyRepository.findAllByEventId(eventId);

        List<PenaltyByTeamDTO> penaltyDTOS = new ArrayList<>();

        for (PenaltyDTO penalty : penalties) {
            if (penaltyDTOS.stream().filter(x -> x.getNumber() == penalty.getNumber()).findAny().isPresent())
                continue;

            List<PenaltyDTO> teamPenalties = penalties.stream()
                    .filter(x -> x.getNumber() == penalty.getNumber())
                    .sorted(Comparator.comparingInt(x -> x.getNumber()))
                    .collect(Collectors.toList());
            penaltyDTOS.add(new PenaltyByTeamDTO(penalty.getDriver(), penalty.getCoDriver(), penalty.getNumber(), teamPenalties));
        }
        return penaltyDTOS.stream().sorted(Comparator.comparingInt(x -> x.getNumber())).collect(Collectors.toList());
    }

    public boolean removePenalty(Long penaltyId) {
        penaltyRepository.deleteById(penaltyId);
        return true;
    }
}
