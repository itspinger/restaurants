package raf.rs.reservations.mapper;


import raf.rs.reservations.domain.Appointment;
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
                appointment.getTable() != null ? TableMapper.toDTO(appointment.getTable()) : null
        );
    }


    public static Appointment toEntity(AppointmentDto appointmentDTO) {
        if (appointmentDTO == null) {
            return null;
        }
        final Appointment appointment = new Appointment();
        appointment.setId(appointmentDTO.getId());
        appointment.setDate(appointmentDTO.getDate());
        appointment.setTime(appointmentDTO.getTime());
        appointment.setTable(TableMapper.toEntity(appointmentDTO.getTablesDto()));
        return appointment;
    }
}
