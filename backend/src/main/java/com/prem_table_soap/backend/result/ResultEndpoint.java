package com.prem_table_soap.backend.result;

import com.prem_table_soap.backend.ObjectNotFoundException;
import com.prem_table_soap.backend.xsdobjects.results.*;
import com.prem_table_soap.backend.xsdobjects.team.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

@Endpoint
public class ResultEndpoint {
    private ResultService service;

    @Autowired
    public ResultEndpoint(ResultService resultService) {
        this.service = resultService;
    }


    @PayloadRoot(namespace = "http://prem_table_soap.com/backend/xsdObjects/results", localPart ="GetAllResultsRequest")
    @ResponsePayload
    public GetAllResultsResponse processAllResults(@RequestPayload GetAllResultsRequest request) {

        List<Result> allResults = service.findAll();

        GetAllResultsResponse response = new GetAllResultsResponse();

        //add Result details list to response object
        for(Result Result : allResults) {
            ResultDetails ResultDetailsMapped = mapResult(Result);

            response.getResultDetails().add(ResultDetailsMapped);

        }

        return response;
    }

    @PayloadRoot(namespace = "http://prem_table_soap.com/backend/xsdObjects/results", localPart ="GetResultsForTeamRequest")
    @ResponsePayload
    public GetResultsForTeamResponse processTeamResults(@RequestPayload GetResultsForTeamRequest request) {

        List<Result> allTeamResults = service.findByTeamId(request.getTeamId());

        GetResultsForTeamResponse response = new GetResultsForTeamResponse();

        //add Result details list to response object
        for(Result Result : allTeamResults) {
            ResultDetails ResultDetailsMapped = mapResult(Result);

            response.getResultDetails().add(ResultDetailsMapped);

        }

        return response;
    }

    @PayloadRoot(namespace = "http://prem_table_soap.com/backend/xsdObjects/results", localPart = "DeleteResultsRequest")
    @ResponsePayload
    public DeleteResultsResponse processDeleteResult(@RequestPayload DeleteResultsRequest request) {

        ResultService.Status status = service.deleteByResultId(request.getResultId());

        DeleteResultsResponse response = new DeleteResultsResponse();

        response.setStatus(mapStatus(status));

        return response;
    }

    // Code to convert java enumerator to xml enumerator
    private Status mapStatus(ResultService.Status status) {
        if (status == ResultService.Status.FAILURE)
            return com.prem_table_soap.backend.xsdobjects.results.Status.FAILURE;
        return com.prem_table_soap.backend.xsdobjects.results.Status.SUCCESS;
    }


    private ResultDetails mapResult(Result result) {
        ResultDetails ResultDetails = new ResultDetails();
        ResultDetails.setResultId(result.getResult_id());
        ResultDetails.setHomeTeamId(result.getHome_team_id());
        ResultDetails.setAwayTeamId(result.getAway_team_id());
        ResultDetails.setHomeTeamGoals(result.getHome_team_goals());
        ResultDetails.setAwayTeamGoals(result.getAway_team_goals());
        ResultDetails.setResultDate(toXMLGregorianCalendar(result.getResult_date()));

        return ResultDetails;
    }

    public XMLGregorianCalendar toXMLGregorianCalendar(LocalDate localDate) {
        if (localDate == null) return null;

        GregorianCalendar gCal = GregorianCalendar.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()));
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal);
        } catch (Exception e) {
            throw new RuntimeException("Error converting LocalDate to XMLGregorianCalendar", e);
        }
    }
}
