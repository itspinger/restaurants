import Appointment from "./appointment";
import Restaurant from "./restaurant";

interface Reservation {
    id: number,
    userId: number,
    appointment: Appointment,
    restaurant: Restaurant
}

export default Reservation;