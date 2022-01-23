package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.dto.request.TeamRequest;
import com.slub.chamchimayo.dto.response.TeamResponse;
import com.slub.chamchimayo.entity.Team;
import com.slub.chamchimayo.entity.TeamSpecification;
import com.slub.chamchimayo.service.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<TeamResponse> create(@RequestBody TeamRequest teamRequest) {
        TeamResponse teamResponse = teamService.create(teamRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> searchById(@PathVariable Long id) {
        TeamResponse teamResponse = teamService.searchById(id);
        return ResponseEntity.status(HttpStatus.OK).body(teamResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamResponse>> teamSearch(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String region,
        @RequestParam(required = false) String sports) {

        Specification<Team> spec = (root, query, criteriaBuilder) -> null;
        if (name != null) {
            spec = spec.and(TeamSpecification.likeName(name));
        }
        if (region != null) {
            spec = spec.and(TeamSpecification.equalRegion(region));
        }
        if (sports != null) {
            spec = spec.and(TeamSpecification.equalSports(sports));
        }

        List<TeamResponse> teamResponse = teamService.searchTeam(spec);
        return ResponseEntity.status(HttpStatus.OK).body(teamResponse);
    }

    @PutMapping("/update")
    public ResponseEntity modifyTeamName(@RequestBody TeamRequest teamRequest) {
        teamService.updateTeamName(teamRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        teamService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
