package com.example.realestate.service;

import com.example.realestate.model.LikeOrDisLike;
import com.example.realestate.repository.LikeOrDisLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeOrDisLikeService {

    @Autowired
    private LikeOrDisLikeRepository likeOrDisLikeRepository;


    public List<LikeOrDisLike> getAllLikesOrDisLikes() {
        return likeOrDisLikeRepository.findAll();
    }


    public Optional<LikeOrDisLike> getLikeOrDisLikeById(Long id) {
        return likeOrDisLikeRepository.findById(id);
    }


    public LikeOrDisLike saveLikeOrDisLike(LikeOrDisLike likeOrDisLike) {
        return likeOrDisLikeRepository.save(likeOrDisLike);
    }


    public LikeOrDisLike updateLikeOrDisLike(Long id, LikeOrDisLike likeOrDisLikeDetails) {
        Optional<LikeOrDisLike> likeOrDisLikeOptional = likeOrDisLikeRepository.findById(id);

        if (likeOrDisLikeOptional.isPresent()) {
            LikeOrDisLike likeOrDisLike = likeOrDisLikeOptional.get();
            likeOrDisLike.setRealEstate(likeOrDisLikeDetails.getRealEstate());
            likeOrDisLike.setUser(likeOrDisLikeDetails.getUser());
            likeOrDisLike.setFeeling(likeOrDisLikeDetails.getFeeling());

            return likeOrDisLikeRepository.save(likeOrDisLike);
        } else {
            throw new RuntimeException("LikeOrDisLike not found with id: " + id);
        }
    }


    public void deleteLikeOrDisLike(Long id) {
        likeOrDisLikeRepository.deleteById(id);
    }
}
