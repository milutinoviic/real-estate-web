package com.example.realestate.service;

import com.example.realestate.enums.Feeling;
import com.example.realestate.enums.TypeRealEstate;
import com.example.realestate.enums.TypeSale;
import com.example.realestate.model.LikeOrDisLike;
import com.example.realestate.model.RealEstate;
import com.example.realestate.model.Tour;
import com.example.realestate.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class RealEstateService {

    @Autowired
    private RealEstateRepository realEstateRepository;


    public List<RealEstate> getAllRealEstates() {
        return realEstateRepository.findAll();
    }


    public Optional<RealEstate> getRealEstateById(Long id) {
        return realEstateRepository.findById(id);
    }


    public RealEstate saveRealEstate(RealEstate realEstate) {
        return realEstateRepository.save(realEstate);
    }


    public RealEstate updateRealEstate(Long id, RealEstate realEstateDetails) {
        Optional<RealEstate> realEstateOptional = realEstateRepository.findById(id);

        if (realEstateOptional.isPresent()) {
            RealEstate realEstate = realEstateOptional.get();
            realEstate.setLocation(realEstateDetails.getLocation());
            realEstate.setSurfaceArea(realEstateDetails.getSurfaceArea());
            realEstate.setPrice(realEstateDetails.getPrice());
            realEstate.setTypeSales(realEstateDetails.getTypeSales());
            realEstate.setTypeRealEstate(realEstateDetails.getTypeRealEstate());
            realEstate.setPicture(realEstateDetails.getPicture());
            realEstate.setAgency(realEstateDetails.getAgency());
            realEstate.setAgent(realEstateDetails.getAgent());
            realEstate.setActive(realEstateDetails.isActive());

            return realEstateRepository.save(realEstate);
        } else {
            throw new RuntimeException("Real estate not found with id: " + id);
        }
    }


    public void deleteRealEstate(Long id) {
        realEstateRepository.deleteById(id);
    }

    public List<RealEstate> search(List<RealEstate> realEstates, String location, Integer minArea, Integer maxArea , Double minPrice, Double maxPrice, TypeSale typeOfUse, TypeRealEstate typeOfRealEstate) {
        List<RealEstate> filteredRealEstates = new ArrayList<>();

        for (RealEstate j : realEstates) {
            boolean matches = true;
            if(location!=null && !location.isEmpty()  && !location.equalsIgnoreCase(j.getLocation()))
            {
                matches=false;
            }
            if(minArea!=null && j.getSurfaceArea()<minArea ){
                matches=false;
            }
            if(maxArea !=null && j.getSurfaceArea()>maxArea){
                matches=false;
            }
            if(minPrice !=null && j.getPrice()<minPrice){
                matches=false;
            }
            if(maxPrice!=null && j.getPrice()>maxPrice){
                matches=false;
            }
            if(typeOfUse!=null && !j.getTypeSales().equals(typeOfUse)){
                matches=false;
            }
            if(typeOfRealEstate !=null && !j.getTypeRealEstate().equals(typeOfRealEstate)){
                matches=false;
            }
            if(matches){
                filteredRealEstates.add(j);
            }
        }
        return filteredRealEstates;
    }



    public double calculateRealEstatePopularity(RealEstate realEstate, List<LikeOrDisLike> likes, List<Tour> tours) {
        // Broj lajkova
        long likeCount = likes.stream()
                .filter(like -> like.getRealEstate().getId().equals(realEstate.getId()) && like.getFeeling() == Feeling.LIKED)
                .count();

        // Broj dislajkova
        long dislikeCount = likes.stream()
                .filter(like -> like.getRealEstate().getId().equals(realEstate.getId()) && like.getFeeling() == Feeling.DISLIKED)
                .count();

        // Broj zahteva za obilazak
        long tourCount = tours.stream()
                .filter(tour -> tour.getRealEstate().getId().equals(realEstate.getId()))
                .count();

        // Definiši ponder za svaku metriku
        double likeScore = likeCount * 0.5;
        double dislikePenalty = dislikeCount * 0.2; // negativan uticaj dislajkova
        double tourScore = tourCount * 0.3;

        // Konačni score popularnosti
        return likeScore - dislikePenalty + tourScore;
    }

}
