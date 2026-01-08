package com.prem_table_soap.backend.team;

import com.prem_table_soap.backend.ObjectNotFoundException;
import com.prem_table_soap.backend.xsdobjects.team.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class TeamEndpoint {
    private TeamService service;

    @Autowired
    public TeamEndpoint(TeamService teamService) {
        this.service = teamService;
    }

    //tells spring to use this method when root element of xml has this namespace and element name is localpart
    @PayloadRoot(namespace = "http://prem_table_soap.com/backend/xsdObjects/team", localPart = "GetTeamByIdRequest")

    //specifies spring to convert return of function to config xml
    @ResponsePayload

    //@requestPayload tells spring to convert input xml to java object.
    public GetTeamByIdResponse processTeamDetailsRequest(@RequestPayload GetTeamByIdRequest request) {

        Team team = service.findById(request.getTeamId());

        //error handling
        if (team==null) {
            throw new ObjectNotFoundException("Invalid Team Id " + request.getTeamId());
        }

        //initialise response object
        GetTeamByIdResponse response = new GetTeamByIdResponse();

        TeamDetails teamDetails =  mapTeam(team);
        response.setTeamDetails(teamDetails);

        return response;
    }

    @PayloadRoot(namespace = "http://prem_table_soap.com/backend/xsdObjects/team", localPart ="GetAllTeamsRequest")
    @ResponsePayload
    public GetAllTeamsResponse processAllTeamsRequest(@RequestPayload GetAllTeamsRequest request) {

        List<Team> allTeams = service.findAll();

        GetAllTeamsResponse response = new GetAllTeamsResponse();

        //add Team details list to response object
        for(Team Team:allTeams) {
            TeamDetails TeamDetailsMapped = mapTeam(Team);

            response.getTeamDetails().add(TeamDetailsMapped);

        }

        return response;
    }

    private TeamDetails mapTeam(Team team) {
        TeamDetails TeamDetails = new TeamDetails();
        TeamDetails.setTeamId(team.getTeam_id());
        TeamDetails.setTeamName(team.getTeam_name());

        return TeamDetails;
    }
}
