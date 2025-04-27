package com.example.realestate.service;

import com.example.realestate.model.Agency;
import com.example.realestate.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgencyService {

    @Autowired
    private AgencyRepository agencyRepository;


    public List<Agency> getAllAgencies() {
        return agencyRepository.findAll();
    }


    public Optional<Agency> getAgencyById(Long id) {
        return agencyRepository.findById(id);
    }


    public Agency saveAgency(Agency agency) {
        return agencyRepository.save(agency);
    }


    public Agency updateAgency(Long id, Agency agencyDetails) {
        Optional<Agency> agencyOptional = agencyRepository.findById(id);

        if (agencyOptional.isPresent()) {
            Agency agency = agencyOptional.get();
            agency.setName(agencyDetails.getName());
            agency.setOwner(agencyDetails.getOwner());

            return agencyRepository.save(agency);
        } else {
            throw new RuntimeException("Agency not found with id: " + id);
        }
    }


    public void deleteAgency(Long id) {
        agencyRepository.deleteById(id);
    }
}
