import { defineStore } from 'pinia';
import { requestFromApi } from '@/utils/api';
import { useNotify } from '@f3ve/vue-notify'
import Appointment from '@/types/appointment';

export const useAppointmentStore = defineStore('appointments', {
    state: () => ({
        appointments: [] as Appointment[],
        totalPages: 0,
        totalElements: 0
    }),
    actions: {
        async fetchAppointments(page: number, query: URLSearchParams) {
            console.log(`reservations/api/appointments?${query.toString()}`);

            const response = await requestFromApi<{ content: Appointment[], totalElements: number, totalPages: number }>(
                'get',
                `reservations/api/appointments?${query.toString()}`,
            );

            if (!response) {
                return;
            }

            this.appointments = response.content;
            this.totalPages = response.totalPages;
            this.totalElements = response.totalElements;
        }
    }
})