import { defineStore } from 'pinia';
import { requestFromApi } from '@/utils/api';
import { useNotify } from '@f3ve/vue-notify'
import Reservation from '@/types/reservation';

export const useReservationStore = defineStore('reservations', {
    state: () => ({
        reservations: [] as Reservation[],
        totalPages: 0,
        totalElements: 0
    }),
    actions: {
        async fetchReservations(page: number, query: URLSearchParams) {
            console.log(`reservations/api/reservation?${query.toString()}`);

            const response = await requestFromApi<{ content: Reservation[], totalElements: number, totalPages: number }>(
                'get',
                `reservations/api/reservation?${query.toString()}`,
            );

            if (!response) {
                return;
            }

            this.reservations = response.content;
            this.totalPages = response.totalPages;
            this.totalElements = response.totalElements;
        },

        async cancelReservation(id: number) {
            const notify = useNotify();
            notify.show('Preparing to cancel the reservation...', 'success');

            const response = await requestFromApi<{ success: boolean }>(
                'post',
                `reservations/api/reservation/cancel/${id}`
            );

            if (!response || !response.success) {
                notify.show('Failed to cancel, please refresh and try again', 'error')
                return;
            }

            notify.show('Successfully cancelled this thing for you', 'success');
            this.reservations = [];
        }
    }
})