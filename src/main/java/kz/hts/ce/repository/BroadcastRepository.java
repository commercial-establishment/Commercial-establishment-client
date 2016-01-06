package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Broadcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast, UUID> {

    @Query("SELECT b FROM Broadcast b WHERE b.date <= ?1")
    List<Broadcast> findByNearestDate(Date date);
}


