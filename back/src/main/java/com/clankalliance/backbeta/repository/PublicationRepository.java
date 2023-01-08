package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.publications.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository  extends JpaRepository<Publication,Integer> {

    @Query("from Publication p where p.id=?1")
    Optional<Publication> findById(String id);

    @Query("from Publication p where ?1='' or p.heading like %?1%")
    List<Publication> findByHeading(String heading);

}
