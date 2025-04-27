package com.example.realestate.repository;

import com.example.realestate.model.LikeOrDisLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeOrDisLikeRepository extends JpaRepository<LikeOrDisLike, Long> {
}
