package com.raihan.backend.controller;

import com.raihan.backend.dtos.Leaderboard;
import com.raihan.backend.dtos.ScoreSubmit;
import com.raihan.backend.model.Player;
import com.raihan.backend.model.Score;
import com.raihan.backend.repositories.ScoreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ScoreController {

    private final ScoreRepository scoreRepository;

    public ScoreController(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @PostMapping("/scores")
    public ResponseEntity<?> submitScore(@RequestBody ScoreSubmit request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player currentUser = (Player) authentication.getPrincipal();

        Score newScore = new Score();
        newScore.setPlayer(currentUser);
        newScore.setTotalScore(request.getValue());
        newScore.setZombiesKilled(request.getZombies_killed());
        newScore.setDaysSurvived(request.getDays_Passed());

        scoreRepository.save(newScore);

        return ResponseEntity.ok(Map.of("message", "Score berhasil disimpan ke Database!"));
    }

    @GetMapping("/players/leaderboard/high-score")
    public ResponseEntity<List<Leaderboard>> getLeaderboard(@RequestParam(defaultValue = "5") int limit) {
        List<Score> topScores = scoreRepository.findTopScores(PageRequest.of(0, limit));
        List<Leaderboard> response = topScores.stream()
                .map(score -> new Leaderboard(score.getPlayer().getUsername(), score.getTotalScore()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}