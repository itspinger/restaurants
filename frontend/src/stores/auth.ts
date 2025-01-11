import { defineStore } from 'pinia';
import { requestFromApi } from '@/utils/api';
import axios from 'axios';
import { useNotify } from '@f3ve/vue-notify';

export const useAuthStore = defineStore('auth', {
    state: () => ({
      username: null as String | null,
      uuid: null as number | null,
      token: '' as string,
      roles: [] as string[]
    }),

    getters: {
      isLoggedIn: (state) => {
        return state.token.length > 0 && state.uuid !== null && state.username !== null;
      },

      isAdmin: (state) => {
        return state.roles.includes("ROLE_ADMIN");
      },

      isManager: (state) => {
        return state.roles.includes("ROLE_MANAGER");
      },

      isClient: (state) => {
        return state.roles.includes("ROLE_CLIENT");
      }
    },

    actions: {
      async login(username: string, password: string) {
        const notify = useNotify();
        const response = await requestFromApi<{token: string, user: { id: number, username: string }}>(
            'post',
            'user-service/api/user/login',
            {username, password},
        );

        if (!response) {
          return false;
        }

        // Izvlacimo podatke iz odgovora
        this.token = response.token;
        this.username = response.user.username;
        this.uuid = response.user.id;

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

      async register(role: string, firstName: string, lastName: string, email:string, username: string, password: string, dob: string) {
        const path = role === 'client' ? 'client' : 'manager';
        const notify = useNotify();

        console.log({username, firstName, email, lastName, password, birthDate: dob})

        const response = await requestFromApi<{user_id: string}>(
           'post',
           `user-service/api/${path}/register`,
           {username, firstName, email, lastName, password, birthDate: dob }
        );

        if (!response) {
          return false;
        }

        console.log(response);
        notify.show('Sucessfully created a new account', 'success');
        return true;
      },

      async fetchSelfUser() {
        if (!this.uuid) {
          return null;
        }

        const response = await requestFromApi<{ firstName: string, lastName: string, email: string, username: string, birthDate: string, roles: string[] }>(
            'get',
            `user-service/api/user/${this.uuid}`
        );

        if (!response) {
          return null;
        } 

        this.username = response.username;
        this.roles = response.roles;
        return response;
      },

      async updateProfile(firstName: string, lastName: string, email: string, birthDate: string, password: string | null) {
        const notify = useNotify();

        console.log({ firstName, lastName, email, birthDate, password });

        const response = await requestFromApi(
          'patch',
          `user-service/api/user/${this.uuid}`,
          { firstName, lastName, email, birthDate, password }
        );

        if (!response) {
          return false;
        }

        notify.show('Successfully updated profile', 'success');
        return true;
      },

      logout() {
        this.username = null;
        this.token = '';
        this.uuid = null;
        this.roles = [];
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