package com.insilicosoft.portal.svc.results.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insilicosoft.portal.svc.results.persistence.entity.Results;

/**
 * Repository for {@link Results} objects.
 * 
 * @author geoff
 */
public interface ResultsRepository extends JpaRepository<Results, Long> {

  Optional<Results> findBySubmissionId(long submissionId);

}