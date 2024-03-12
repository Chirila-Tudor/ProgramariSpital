package ro.chirila.programarispital.repository.dto;

public record ChangePasswordDTO(
        String username,
        String oldPassword,
        String newPassword) {

}
