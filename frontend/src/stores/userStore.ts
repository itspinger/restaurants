import { defineStore } from 'pinia';
import { requestFromApi } from '@/utils/api';
import { useNotify } from '@f3ve/vue-notify'
import User from '@/types/user';

export const useUserStore = defineStore('users', {
    state: () => ({
        users: [] as User[],
        totalPages: 0,
        totalElements: 0
    }),
    actions: {
        async fetchUsers(page: number) {
            const query = new URLSearchParams();
            query.append('page', page.toString());
            query.append('size', '12');

            const response = await requestFromApi<{ content: User[], totalElements: number, totalPages: number }>(
                'get',
                `user-service/api/user?${query.toString()}`,
            );

            if (!response) {
                return;
            }

            this.users = response.content;
            this.totalPages = response.totalPages;
            this.totalElements = response.totalElements;
        },

        async banUser(userId: number) {
            const response = await requestFromApi<{ success: boolean}>(
                'post',
                `user-service/api/admin/ban/${userId}`
            );

            if (!response || !response.success) {
                useNotify().show('Failed to ban user, please try again later', 'error');
                return false;
            }

            useNotify().show('Successfully banned user', 'success');
            return true;
        },

        async unbanUser(userId: number) {
            const response = await requestFromApi<{ success: boolean}>(
                'post',
                `user-service/api/admin/unban/${userId}`
            );

            if (!response || !response.success) {
                useNotify().show('Failed to unban user, please try again later', 'error');
                return false;
            }

            useNotify().show('Successfully unbanned user', 'success');
            return true;
        }
    }
})