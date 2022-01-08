package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.network.Header;
import com.slub.chamchimayo.network.request.TeamApiRequest;
import com.slub.chamchimayo.network.response.TeamApiResponse;
import com.slub.chamchimayo.service.TeamService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/new")
    public Header<TeamApiResponse> create(@RequestBody Header<TeamApiRequest> request) {

        return teamService.create(request);
    }

    @GetMapping("/name/{name}")
    public Header<TeamApiResponse> searchByName(@PathVariable String name) {

        return teamService.searchByName(name);
    }

    @GetMapping("/area/{area}")
    public Header<TeamApiResponse> searchByArea(@PathVariable String area) {

        return teamService.searchByName(area);
    }

    @GetMapping("/sports/{sports}")
    public Header<TeamApiResponse> searchBySports(@PathVariable String sports) {

        return teamService.searchByName(sports);
    }

    @PostMapping("/update")
    public Header<TeamApiResponse> update(@RequestBody Header<TeamApiRequest> request) {

        return teamService.update(request);
    }

    @DeleteMapping("/delete/{name}")
    public Header<TeamApiResponse> delete(@PathVariable String name) {

        return teamService.delete(name);
    }
}
