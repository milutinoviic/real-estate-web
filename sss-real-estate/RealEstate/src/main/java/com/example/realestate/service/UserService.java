package com.example.realestate.service;

import com.example.realestate.model.AgentRate;
import com.example.realestate.model.Tour;
import com.example.realestate.model.User;
import com.example.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgentRateService agentRateService;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public User updateUser(Long id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userDetails.getName());
            user.setLast(userDetails.getLast());
            user.setPhone(userDetails.getPhone());
            user.setAddress(userDetails.getAddress());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setActive(userDetails.isActive());
            user.setRole(userDetails.getRole());

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean checkIfUsernameExists(String email){
        List<User> users = userRepository.findAll();
        for(User user :  users){
            if(user.getEmail().equals(email))
            {
                return true;
            }
        }

        return false;

    };

    public User findByUsernameAndPassword(String email,String password){

        List<User> users = userRepository.findAll();
        for(User user :  users){
            if(user.getEmail().equals(email) && user.getPassword().equals(password))
            {
                return user;
            }
        }
        return null;
    };


    public double calculateAgentScore(double averageAgentRating, int tourCount) {
        double agentRatingScore = averageAgentRating * 0.6;
        double tourScore = tourCount * 0.4;


        double agentScore = agentRatingScore + tourScore;

        return agentScore;
    }

    public int calculateTourCount(List<Tour> tours, User agent) {
        return (int) tours.stream()
                .filter(tour -> tour.getRealEstate().getAgent().equals(agent))
                .count();
    }


    public double getAgentScore(User agent, List<AgentRate> agentRates, List<Tour> tours) {
        double averageRating = agentRateService.calculateAverageAgentRating(
                agentRates.stream()
                        .filter(rate -> rate.getAgent().equals(agent))
                        .collect(Collectors.toList())
        );

        int tourCount = calculateTourCount(tours, agent);

        return calculateAgentScore(averageRating, tourCount);
    }



}
