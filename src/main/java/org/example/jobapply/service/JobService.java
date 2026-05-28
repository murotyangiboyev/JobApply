package org.example.jobapply.service;

import org.example.jobapply.dto.JobRequest;
import org.example.jobapply.dto.JobResponse;
import org.example.jobapply.entity.JobListing;

import java.util.List;

public interface JobService {

    JobResponse createJob(JobRequest jobRequest, String employerEmail);
    List<JobResponse> getAllOpenJobs();
    JobResponse getJobById(Long id);
    JobResponse updateJob(Long id, JobRequest jobRequest, String employerEmail);
    void deleteJob(Long id, String employerEmail);
    JobResponse mapToResponse(JobListing job);
}
