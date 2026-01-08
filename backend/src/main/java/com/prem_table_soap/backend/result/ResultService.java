package com.prem_table_soap.backend.result;

import com.prem_table_soap.backend.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ResultService {
    private ResultRepository resultRepository;

    public enum Status {
        SUCCESS, FAILURE;
    }

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public List<Result> findAll() {
        List<Result> allResults = resultRepository.findAll();
        return allResults;
    }

    public List<Result> findByTeamId(int id) {

        List<Result> resultList = resultRepository.findMatchesByTeamId(id);

        if (resultList.isEmpty()) {
            String message = String.format("No results exist for this team id");
            throw new ObjectNotFoundException(message);
        }

        return resultList;
    }

    public Status deleteByResultId(int id) {
        if (!resultRepository.existsById(id)) {
            return Status.FAILURE; // Course doesn't exist
        }

        resultRepository.deleteById(id);

        // Optional: Double-check it's gone (useful in tests or critical systems)
        if (!resultRepository.existsById(id)) {
            return Status.SUCCESS;
        } else {
            return Status.FAILURE; // Should not happen, but defensive
        }
    }
}
