package org.example.jobapply.repository;

import java.util.List;

import org.example.jobapply.entity.JobListing;
import org.example.jobapply.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobListing, Long> {

    List<JobListing> findByEmployerId(Long employerId);
    List<JobListing> findByStatus(JobStatus status);
    List<JobListing> findAllByOrderByCreatedAtDesc();

}
