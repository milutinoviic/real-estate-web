package com.example.realestate.service;

import com.example.realestate.model.Tour;
import com.example.realestate.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;


    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }


    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }


    public Tour saveTour(Tour tour) {
        return tourRepository.save(tour);
    }


    public Tour updateTour(Long id, Tour tourDetails) {
        Optional<Tour> tourOptional = tourRepository.findById(id);

        if (tourOptional.isPresent()) {
            Tour tour = tourOptional.get();
            tour.setDate(tourDetails.getDate());
            tour.setStatus(tourDetails.getStatus());
            tour.setRealEstate(tourDetails.getRealEstate());
            tour.setUser(tourDetails.getUser());

            return tourRepository.save(tour);
        } else {
            throw new RuntimeException("Tour not found with id: " + id);
        }
    }


    public void deleteTour(Long id) {
        tourRepository.deleteById(id);
    }

}
