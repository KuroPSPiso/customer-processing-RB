package com.example.demo.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.MT940Min;

@Repository
public interface MT940MinRepository extends JpaRepository<MT940Min, Long> {
}
