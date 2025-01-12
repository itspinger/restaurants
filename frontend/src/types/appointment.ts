import Restaurant from "./restaurant";
import Table from "./table";

interface Appointment {
    id: number,
    time: string,
    table: Table
    restaurant: Restaurant
}

export default Appointment;