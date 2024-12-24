package raf.rs.reservations.mapper;


import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Tables;
import raf.rs.reservations.dto.AppointmentDto;

public class AppointmentMapper {

    public static AppointmentDto toDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        return new AppointmentDto(
                appointment.getId(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getTable() != null ? appointment.getTable().getId() : null
        );
    }


    public static Appointment toEntity(AppointmentDto appointmentDTO, Tables table) {
        if (appointmentDTO == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(appointmentDTO.getId());
        appointment.setDate(appointmentDTO.getDate());
        appointment.setTime(appointmentDTO.getTime());
        appointment.setTable(table);
        return appointment;
    }
}
