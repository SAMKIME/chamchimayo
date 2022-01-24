package kr.slub.chamchimayo.controller;

import kr.slub.chamchimayo.dto.request.TeamRequest;
import kr.slub.chamchimayo.dto.response.TeamResponse;
import kr.slub.chamchimayo.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping("update")
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
