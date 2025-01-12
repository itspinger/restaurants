package raf.rs.reservations.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.domain.Table;
import raf.rs.reservations.domain.Table.Zone;
import raf.rs.reservations.repository.AppointmentRepository;
import raf.rs.reservations.repository.RestaurantRepository;
import raf.rs.reservations.repository.TableRepository;

import java.time.LocalDateTime;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final TableRepository tableRepository;
    private final AppointmentRepository appointmentRepository;

    public TestDataRunner(RestaurantRepository restaurantRepository,
                          TableRepository tableRepository,
                          AppointmentRepository appointmentRepository) {
        this.restaurantRepository = restaurantRepository;
        this.tableRepository = tableRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        //// Add Restaurants
        //addRestaurant("123 Main St, Springfield", "Family-friendly restaurant", 5, 10, "The Food Hub", "08:00-22:00", "American", 602L);
        //addRestaurant("45 Elm St, Springfield", "Italian cuisine with authentic recipes", 3, 7, "Mama Mia Pizzeria", "10:00-23:00", "Italian", 602L);
        //addRestaurant("78 Oak Ave, Springfield", "Asian fusion dining experience", 4, 8, "Dragon Feast", "11:00-21:00", "Asian", 602L);
        //addRestaurant("22 Pine Rd, Springfield", "Fresh seafood delights", 2, 5, "Ocean Breeze", "12:00-22:00", "Seafood", 602L);
        //addRestaurant("91 Maple Blvd, Springfield", "Quick bites and coffee", 6, 12, "Coffee & Co.", "07:00-20:00", "Cafe", 602L);
        //addRestaurant("56 Birch Ln, Springfield", "Authentic Mexican dishes", 3, 6, "Taco Fiesta", "11:00-23:00", "Mexican", 602L);
        //addRestaurant("89 Walnut St, Springfield", "Steakhouse with premium cuts", 5, 9, "Prime Cuts", "17:00-23:00", "Steakhouse", 602L);
        //addRestaurant("33 Cedar Ct, Springfield", "Vegan and vegetarian options", 2, 4, "Green Delights", "08:00-22:00", "Vegan", 602L);
        //addRestaurant("71 Willow Way, Springfield", "Desserts and pastries", 4, 7, "Sweet Treats", "09:00-21:00", "Bakery", 602L);
        //addRestaurant("15 Chestnut Rd, Springfield", "Fine dining experience", 3, 6, "La Belle Vie", "18:00-23:00", "French", 602L);
        //addRestaurant("44 Aspen Dr, Springfield", "Casual dining with variety", 6, 10, "Urban Eats", "09:00-21:00", "Fusion", 602L);
        //addRestaurant("66 Cypress Ln, Springfield", "Authentic Greek cuisine", 4, 7, "Greek Taverna", "12:00-22:00", "Greek", 602L);
        //addRestaurant("92 Hemlock St, Springfield", "Bar and grill", 3, 8, "Grill & Chill", "11:00-23:00", "Bar & Grill", 602L);
        //addRestaurant("27 Spruce Ave, Springfield", "Korean BBQ specialties", 5, 10, "Seoul Garden", "12:00-23:00", "Korean", 602L);
        //addRestaurant("50 Alder Ct, Springfield", "Casual pizza and pasta", 2, 5, "Pizza Plus", "11:00-22:00", "Italian", 602L);
        //
        //// Add Tables
        //addTable(4, Zone.SMOKING, "The Food Hub");
        //addTable(6, Zone.SMOKING, "The Food Hub");
        //addTable(2, Zone.SMOKING, "Mama Mia Pizzeria");
        //addTable(8, Zone.SMOKING, "Mama Mia Pizzeria");
        //addTable(4, Zone.SMOKING, "Dragon Feast");
        //addTable(6, Zone.SMOKING, "Dragon Feast");
        //addTable(10, Zone.NO_SMOKING, "Ocean Breeze");
        //addTable(4, Zone.NO_SMOKING, "Coffee & Co.");
        //addTable(2, Zone.NO_SMOKING, "Taco Fiesta");
        //addTable(4, Zone.NO_SMOKING, "Taco Fiesta");
        //addTable(8, Zone.NO_SMOKING, "Prime Cuts");
        //addTable(10, Zone.NO_SMOKING, "Prime Cuts");
        //addTable(4, Zone.NO_SMOKING, "Green Delights");
        //addTable(6, Zone.NO_SMOKING, "Green Delights");
        //addTable(4, Zone.NO_SMOKING, "Sweet Treats");
        //addTable(6, Zone.NO_SMOKING, "La Belle Vie");
        //addTable(8, Zone.NO_SMOKING, "Urban Eats");
        //addTable(10, Zone.NO_SMOKING, "Greek Taverna");
        //addTable(4, Zone.NO_SMOKING, "Grill & Chill");
        //addTable(6, Zone.NO_SMOKING, "Seoul Garden");
        //
        //// Add Appointments
        //addAppointment(LocalDateTime.parse("2025-01-12T12:30:00"), "The Food Hub");
        //addAppointment(LocalDateTime.parse("2025-01-12T13:00:00"), "The Food Hub");
        //addAppointment(LocalDateTime.parse("2025-01-12T13:30:00"), "Mama Mia Pizzeria");
        //addAppointment(LocalDateTime.parse("2025-01-12T14:00:00"), "Mama Mia Pizzeria");
        //addAppointment(LocalDateTime.parse("2025-01-12T14:30:00"), "Dragon Feast");
        //addAppointment(LocalDateTime.parse("2025-01-12T15:00:00"), "Dragon Feast");
        //addAppointment(LocalDateTime.parse("2025-01-12T15:30:00"), "Ocean Breeze");
        //addAppointment(LocalDateTime.parse("2025-01-12T16:00:00"), "Coffee & Co.");
        //addAppointment(LocalDateTime.parse("2025-01-12T16:30:00"), "Taco Fiesta");
        //addAppointment(LocalDateTime.parse("2025-01-12T17:00:00"), "Prime Cuts");
    }

    private void addRestaurant(String address, String description, int discountAfterReservations, int freeItemsAfterReservations, String name, String openTime, String type, Long managerId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setDescription(description);
        restaurant.setDiscountAfterXReservations(discountAfterReservations);
        restaurant.setFreeItemEachXReservations(freeItemsAfterReservations);
        restaurant.setName(name);
        restaurant.setOpenTime(openTime);
        restaurant.setType(type);
        restaurant.setManagerId(managerId);
        restaurantRepository.save(restaurant);
    }

    private void addTable(int capacity, Zone zone, String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName);
        if (restaurant != null) {
            Table table = new Table();
            table.setCapacity(capacity);
            table.setZone(zone);
            table.setRestaurant(restaurant);
            tableRepository.save(table);
        }
    }

    private void addAppointment(LocalDateTime time, String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName);
        if (restaurant != null) {
            Table table = tableRepository.findFirstByRestaurant(restaurant);
            if (table != null) {
                Appointment appointment = new Appointment();
                appointment.setTime(time);
                appointment.setTable(table);
                appointmentRepository.save(appointment);
            }
        }
    }
}