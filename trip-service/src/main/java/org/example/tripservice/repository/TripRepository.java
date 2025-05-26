package org.example.tripservice.repository;

import org.example.tripservice.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip,String> {
    
}
