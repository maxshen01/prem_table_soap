package com.prem_table_soap.backend.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TeamService {
    private TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAll() {
        List<Team> allTeams = teamRepository.findAll();
        return allTeams;
    }

    public Team findById(int id) {

        Optional<Team> team = teamRepository.findById(id);

        if (team.isEmpty()) {
            return null;
        }

        Team selectedTeam = team.get();

        return selectedTeam;
    }
}
