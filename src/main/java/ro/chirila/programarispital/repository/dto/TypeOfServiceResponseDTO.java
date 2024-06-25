package ro.chirila.programarispital.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfServiceResponseDTO {
    private Long id;
    private String service;
    private List<String> doctorsWhoCanPerformService;
    private Integer duration;
}
