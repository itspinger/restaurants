package raf.rs.reservations.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.AppointmentFilterDto;
import raf.rs.reservations.repository.AppointmentRepository;
import raf.rs.reservations.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, ModelMapper modelMapper) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<AppointmentDto> findAvailableAppointments(AppointmentFilterDto filterDto, Pageable pageable) {
        return this.appointmentRepository
            .findAvailableAppointments(filterDto, pageable)
            .map(appointment -> this.modelMapper.map(appointment, AppointmentDto.class));
    }


}
