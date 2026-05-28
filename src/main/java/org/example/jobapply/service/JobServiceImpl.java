package org.example.jobapply.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.jobapply.dto.JobRequest;
import org.example.jobapply.dto.JobResponse;
import org.example.jobapply.entity.JobListing;
import org.example.jobapply.entity.Users;
import org.example.jobapply.enums.JobStatus;
import org.example.jobapply.enums.Role;
import org.example.jobapply.repository.JobRepository;
import org.example.jobapply.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public JobResponse createJob(JobRequest jobRequest, String employerEmail) {
        // 1. Find the user by email
        Users user = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + employerEmail));

        // 2. Verify that the user has EMPLOYER or ADMIN role
        if (user.getRole() != Role.EMPLOYER && user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only EMPLOYER or ADMIN can create jobs");
        }

        // 3. Validate salary range
        if (jobRequest.getSalaryMin() > jobRequest.getSalaryMax()) {
            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
        }

        // 4. Build and save the job listing
        JobListing jobListing = JobListing.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .location(jobRequest.getLocation())
                .salaryMin(jobRequest.getSalaryMin())
                .salaryMax(jobRequest.getSalaryMax())
                .jobType(jobRequest.getJobType())
                .employer(user)
                .build();

        JobListing savedJob = jobRepository.save(jobListing);

        // 5. Convert to response DTO and return
        return mapToResponse(savedJob);
    }

    @Override
    public List<JobResponse> getAllOpenJobs() {
        // Get all jobs with OPEN status, ordered by creation date (newest first)
        List<JobListing> openJobs = jobRepository.findByStatus(JobStatus.OPEN);
        
        return openJobs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JobResponse getJobById(Long id) {
        // Find job by ID
        JobListing job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));
        
        return mapToResponse(job);
    }

    @Override
    public JobResponse updateJob(Long id, JobRequest jobRequest, String employerEmail) {
        // 1. Find the job by ID
        JobListing job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));

        // 2. Find the user making the request
        Users user = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + employerEmail));

        // 3. Authorization check:
        // - ADMIN can update any job
        // - EMPLOYER can only update their own jobs
        if (user.getRole() == Role.ADMIN) {
            // Admin can update any job - proceed
        } else if (user.getRole() == Role.EMPLOYER) {
            // Employer can only update their own jobs
            if (!job.getEmployer().getId().equals(user.getId())) {
                throw new IllegalArgumentException("You can only update your own jobs");
            }
        } else {
            throw new IllegalArgumentException("Only EMPLOYER or ADMIN can update jobs");
        }

        // 4. Validate salary range
        if (jobRequest.getSalaryMin() > jobRequest.getSalaryMax()) {
            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
        }

        // 5. Update job fields
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setLocation(jobRequest.getLocation());
        job.setSalaryMin(jobRequest.getSalaryMin());
        job.setSalaryMax(jobRequest.getSalaryMax());
        job.setJobType(jobRequest.getJobType());

        // 6. Save and return
        JobListing updatedJob = jobRepository.save(job);
        return mapToResponse(updatedJob);
    }

    @Override
    public void deleteJob(Long id, String employerEmail) {
        // 1. Find the job by ID
        JobListing job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));

        // 2. Find the user making the request
        Users user = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + employerEmail));

        // 3. Authorization check:
        // - ADMIN can delete any job
        // - EMPLOYER can only delete their own jobs
        if (user.getRole() == Role.ADMIN) {
            // Admin can delete any job - proceed
        } else if (user.getRole() == Role.EMPLOYER) {
            // Employer can only delete their own jobs
            if (!job.getEmployer().getId().equals(user.getId())) {
                throw new IllegalArgumentException("You can only delete your own jobs");
            }
        } else {
            throw new IllegalArgumentException("Only EMPLOYER or ADMIN can delete jobs");
        }

        // 4. Delete the job
        jobRepository.delete(job);
    }

    @Override
    public JobResponse mapToResponse(JobListing job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .jobType(job.getJobType())
                .status(job.getStatus().name())
                .employerName(job.getEmployer().getUsername())
                .employerEmail(job.getEmployer().getEmail())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
