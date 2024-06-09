package ro.chirila.programarispital.repository.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfServiceRequestDTO {
    @NotBlank(message = "Please enter a valid service.")
    private String service;

    @NotBlank(message = "Please enter a valid doctor.")
    private List<String> doctorsWhoCanPerformService;
}
