import { defineStore } from 'pinia';
import { requestFromApi } from '@/utils/api';
import axios from 'axios';
import { useNotify } from '@f3ve/vue-notify';

export const useAuthStore = defineStore('auth', {
    state: () => ({
      username: null as String | null,
      uuid: null as String | null,
      token: '' as string,
    }),

    getters: {
      isLoggedIn: (state) => {
        return state.token.length > 0 && state.uuid !== null && state.username !== null;
      },
    },

    actions: {
      async login(username: string, password: string) {
        const notify = useNotify();
        const response = await requestFromApi<{token: string, username: string, user_id: string}>(
            'post',
            'auth/login',
            {username, password},
        );

        if (!response) {
          return false;
        }

        // Izvlacimo podatke iz odgovora
        this.token = response.token;
        this.username = response.username;
        this.uuid = response.user_id;

        // Dodajemo korisnika u local storage
        localStorage.setItem('user', JSON.stringify({
          username: this.username,
          uuid: this.uuid,
          token: this.token,
        }));

        // Ovo je authorizacija koja ce da se koristi
        axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
        notify.show('Sucessfully logged in', 'success');
        return true;
      },

      async register(username: string, password: string) {
        const notify = useNotify();
        const response = await requestFromApi<{user_id: string}>(
           'post',
           'auth/register',
           {username, password}
        );

        if (!response) {
          return false;
        }

        notify.show('Sucessfully created a new account', 'success');
        return true;
      },

      logout() {
        this.username = null;
        this.token = '';
        this.uuid = null;
        localStorage.removeItem('user');
        delete axios.defaults.headers.common['Authorization'];
        useNotify().show('Successfully logged out of the account', 'success');
      },

      loadToken() {
        const user = localStorage.getItem('user');
        if (!user) {
          return;
        }

        const { username, uuid, token } = JSON.parse(user);
        this.username = username;
        this.uuid = uuid;
        this.token = token;
        axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
      },
    },
  });