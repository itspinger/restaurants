import { defineStore } from 'pinia';
import { requestFromApi } from '@/utils/api';
import Restaurant from '@/types/restaurant';

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
        }
    }
})