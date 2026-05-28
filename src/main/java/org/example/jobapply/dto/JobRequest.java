package org.example.jobapply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Minimum salary is required")
    private Double salaryMin;

    @NotNull(message = "Maximum salary is required")
    private Double salaryMax;

    @NotBlank(message = "Job type is required")
    private String jobType;
}
