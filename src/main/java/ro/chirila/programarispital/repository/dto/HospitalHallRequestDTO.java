package ro.chirila.programarispital.repository.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalHallRequestDTO {
    @NotBlank(message = "Please enter a valid room.")
    private String room;

    @NotBlank(message = "Please enter a valid equipment.")
    private List<EquipmentDTO> equipment;

    private String doctorUsername;
}
