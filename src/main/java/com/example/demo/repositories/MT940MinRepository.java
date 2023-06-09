package com.example.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.MT940Min;

@Repository
public interface MT940MinRepository extends JpaRepository<MT940Min, Long> {
}
