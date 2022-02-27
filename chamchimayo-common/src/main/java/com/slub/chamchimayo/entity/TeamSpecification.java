package com.slub.chamchimayo.entity;

import org.springframework.data.jpa.domain.Specification;

public class TeamSpecification {

    public static Specification<Team> likeName(String name) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Team> equalRegion(String region) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("region"), region);
    }

    public static Specification<Team> equalSports(String sports) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sports"), sports);
    }
}
