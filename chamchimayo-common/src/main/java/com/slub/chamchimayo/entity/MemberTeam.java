package com.slub.chamchimayo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MemberTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberteam_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
