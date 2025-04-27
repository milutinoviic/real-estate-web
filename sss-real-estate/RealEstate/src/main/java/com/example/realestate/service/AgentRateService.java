package com.example.realestate.service;

import com.example.realestate.model.AgentRate;
import com.example.realestate.repository.AgentRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentRateService {

    @Autowired
    private AgentRateRepository agentRateRepository;


    public List<AgentRate> getAllAgentRates() {
        return agentRateRepository.findAll();
    }


    public Optional<AgentRate> getAgentRateById(Long id) {
        return agentRateRepository.findById(id);
    }


    public AgentRate saveAgentRate(AgentRate agentRate) {
        return agentRateRepository.save(agentRate);
    }


    public AgentRate updateAgentRate(Long id, AgentRate agentRateDetails) {
        Optional<AgentRate> agentRateOptional = agentRateRepository.findById(id);

        if (agentRateOptional.isPresent()) {
            AgentRate agentRate = agentRateOptional.get();
            agentRate.setAgent(agentRateDetails.getAgent());
            agentRate.setUser(agentRateDetails.getUser());
            agentRate.setRate(agentRateDetails.getRate());
            agentRate.setDescription(agentRateDetails.getDescription());

            return agentRateRepository.save(agentRate);
        } else {
            throw new RuntimeException("AgentRate not found with id: " + id);
        }
    }

    // Brisanje ocene agenta
    public void deleteAgentRate(Long id) {
        agentRateRepository.deleteById(id);
    }

    public double calculateAverageAgentRating(List<AgentRate> agentRates) {
        return agentRates.stream()
                .mapToInt(AgentRate::getRate)
                .average()
                .orElse(0.0); // Ako nema ocena, vraća 0
    }

}
