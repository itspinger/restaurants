package raf.rs.reservations.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.AppointmentFilterDto;

public interface AppointmentService {

    Page<AppointmentDto> findAvailableAppointments(AppointmentFilterDto filterDto, Pageable pageable);

}
