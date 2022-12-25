package com.clankalliance.backbeta.repository;


import com.clankalliance.backbeta.entity.Announcement;
import com.clankalliance.backbeta.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement,Integer> {

    @Query("from Announcement a where a.id=?1")
    Optional<Announcement> findById(long id);

}
