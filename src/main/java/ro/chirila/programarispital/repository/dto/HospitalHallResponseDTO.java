package ro.chirila.programarispital.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalHallResponseDTO {
    private Long id;
    private String room;
    private List<EquipmentDTO> equipment;
    private String doctorUsername;
}
