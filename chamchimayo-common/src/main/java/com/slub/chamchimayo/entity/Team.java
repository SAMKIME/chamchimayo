package com.slub.chamchimayo.entity;

import com.slub.chamchimayo.network.request.TeamApiRequest;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@EqualsAndHashCode(of = {"id"})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String area;

    private String sports;

    @Builder
    public Team(String name, String area, String sports) {
        this.name = name;
        this.area = area;
        this.sports = sports;
    }

    public void updateTeam(TeamApiRequest request) {
        this.name = request.getName();
        this.area = request.getArea();
        this.sports = request.getSports();
    }
}
