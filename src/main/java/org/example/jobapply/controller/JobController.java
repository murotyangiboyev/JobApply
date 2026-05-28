package org.example.jobapply.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.jobapply.dto.JobRequest;
import org.example.jobapply.dto.JobResponse;
import org.example.jobapply.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("all-jobs")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobs =  new ArrayList<>();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        JobResponse job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @PostMapping("/create")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest jobRequest,
                                                 Authentication authentication) {
        String email = authentication.getName();
        JobResponse job = jobService.createJob(jobRequest, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest request,
                                                 Authentication authentication){
        String email = authentication.getName();
        JobResponse job = jobService.updateJob(id, request, email);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id, Authentication authentication){
        String email = authentication.getName();
        jobService.deleteJob(id, email);
        return ResponseEntity.noContent().build();
    }

}
