import Table from "./table"

interface Restaurant {
    id: number,
    name: string,
    address: string,
    description: string,
    openTime: string,
    type: string,
    tables: Table[],
    discountAfterXReservations: number,
    freeItemEachXReservations: number,
    managerId: number
}

export default Restaurant