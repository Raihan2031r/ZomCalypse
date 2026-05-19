package com.raihan.backend.repositories;

import com.raihan.backend.model.Score;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {

    @Query("SELECT s FROM Score s ORDER BY s.totalScore DESC")
    List<Score> findTopScores(Pageable pageable);
}