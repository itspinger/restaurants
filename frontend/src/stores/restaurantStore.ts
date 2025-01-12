import { defineStore } from 'pinia';
import { requestFromApi } from '@/utils/api';
import Restaurant from '@/types/restaurant';
import { useNotify } from '@f3ve/vue-notify'
import Table from '@/types/table';

export const useRestaurantStore = defineStore('restaurant', {
    state: () => ({
        restaurants: [] as Restaurant[],
        totalPages: 0,
        totalElements: 0
    }),
    actions: {
        async fetchRestaurants(page: number) {
            const queryParams = new URLSearchParams()
            queryParams.append('page', page.toString());
            queryParams.append('size', '8');
            queryParams.append('sort', 'id,desc');

            const response = await requestFromApi<{ content: Restaurant[], totalPages: number, totalElements: number }>(
                'get',
                `reservations/api/restaurants?${queryParams.toString()}`
            );

            console.log(`reservations/api/restaurants?${queryParams.toString()}`)
            console.log(response);

            if (!response) {
                return;
            }

            this.restaurants = response.content;
            this.totalPages = response.totalPages; 
            this.totalElements = response.totalElements;
        },
        
        async fetchRestaurantsById(restaurantId: number) {
            const restaurant = this.restaurants.find(res => res.id === restaurantId) || null
            if (restaurant) {
                return restaurant;
            }

            console.log(`reservations/api/restaurants/${restaurantId}`);

            const response = await requestFromApi<Restaurant>(
                'get',
                `reservations/api/restaurants/${restaurantId}`
            )

            if (!response) {
                return null;
            }

            return response;
        },

        async createRestaurant(restaurant: Partial<Restaurant>) {
            const notify = useNotify();
            if (restaurant.id) {
                notify.show('This restaurant is already created', 'error');
                return null;
            }

            console.log(restaurant)

            const response = await requestFromApi<Restaurant>(
                'post',
                `reservations/api/restaurants`,
                restaurant
            );

            if (!response) {
                return null;
            }

            notify.show('Successfully saved the restaurant', 'success');
            return response;
        },

        async updateRestaurant(restaurant: Partial<Restaurant>) {
            const notify = useNotify();
            console.log(restaurant);
            if (!restaurant.id) {
                notify.show('This restaurant does not exist', 'error');
                return null;
            }

            console.log(restaurant);

            const response = await requestFromApi<Restaurant>(
                'patch',
                `reservations/api/restaurants/${restaurant.id}`,
                restaurant
            );

            if (!response) {
                return null;
            }

            notify.show('Successfully updated the restaurant', 'success');
            return response;
        },

        async fetchAppointmentsByTableId(tableId: number, date: string) {
            const queryParams = new URLSearchParams()
            queryParams.append('size', '24');
            queryParams.append('tableId', tableId.toString());
            queryParams.append('date', date);
            queryParams.append('available', 'true');

            const notify = useNotify();

            console.log(`reservations/api/appointments?${queryParams.toString()}`);

            const response = await requestFromApi<{ content: { id: number, time: string }[] }>(
                'get',
                `reservations/api/appointments?${queryParams.toString()}`,
            );

            if (!response) {
                return null;
            }

            return response.content;
        },

        async bookAppointment(appointmentId: number, userId: number) {
            const notify = useNotify();
            notify.show('Preparing the reservation...', 'success');
            const response = await requestFromApi<{ success: boolean }>(
                'post',
                `reservations/api/reservation/create`,
                { appointmentId, userId }
            );

            if (!response || !response.success) {
                notify.show('Failed to book, please refresh and try again', 'error')
                return;
            }

            notify.show('Successfully booked this restaurant for you', 'success');
        },

        async addTable(restaurantId: number, zone: string, capacity: number) {
            const correctZone = zone === 'smoking' ? 'SMOKING' : 'NO_SMOKING';
            console.log(`reservations/api/restaurants/tables`)
            console.log({restaurantId, zone: correctZone, capacity })

            const response = await requestFromApi<Table>(
                'post',
                `reservations/api/restaurants/tables`,
                { restaurantId, zone: correctZone, capacity }
            );

            if (!response) {
                return null;
            }

            useNotify().show('Successfully created a new table', 'success');
            return response;
        },

        async addAppointment(tableId: number, time: string) {
            const response = await requestFromApi<{ id: number, time: string }>(
                'post',
                `reservations/api/appointments`,
                { tableId, time }
            );

            if (!response) {
                return null;
            }

            useNotify().show('Successfully created an appointment', 'success');
            return response;
        }
    }
})