<template>
  <div class="flex items-center justify-center bg-white dark:bg-gray-900" style="padding-top:100px">
    <div class="container mx-auto">
      <div class="max-w-md mx-auto my-10">
        <div class="text-center">
          <h1 class="my-3 text-3xl font-semibold text-gray-700 dark:text-gray-200">Sign in</h1>
          <p class="text-gray-500 dark:text-gray-400">Sign in to access your account</p>
        </div>

        <div class="m-7">
          <form @submit.prevent="onSubmit">
            <!-- Polje za username korisnika -->
            <div class="mb-6">
              <label
                for="username"
                class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                >Username</label
              >
              <input
                v-model="username"
                type="text"
                id="username"
                placeholder="Enter your username"
                class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                :class="{ 'border-red-500': usernameError }"
                @input="validateUsername"
                required
              />
              <span v-if="usernameError" class="text-sm text-red-500">{{ usernameError }}</span>
            </div>

            <!-- Password input field -->
            <div class="mb-6">
              <label
                for="password"
                class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                >Password</label
              >
              <input
                v-model="password"
                type="password"
                id="password"
                placeholder="Your Password"
                class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                :class="{
                  'border-red-500': passwordError,
                }"
                @input="validatePassword"
                required
              />
              <span v-if="passwordError" class="text-sm text-red-500">{{ passwordError }}</span>
            </div>

            <!-- Submit Button -->
            <div class="mb-6">
              <button
                type="submit"
                class="w-full px-3 py-4 text-white bg-indigo-500 rounded-md focus:bg-indigo-600 focus:outline-none"
              >
                Sign in
              </button>
            </div>

            <p class="text-sm text-center text-gray-400">
              Don’t have an account yet?
              <RouterLink to="/register" 
                class="text-indigo-400 focus:outline-none focus:underline focus:text-indigo-500 dark:focus:border-indigo-800">
                Sign up
              </RouterLink>
            </p>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { RouterLink, useRouter } from 'vue-router';
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const username = ref<string>('');
const password = ref<string>('');
const usernameError = ref<string>('');
const passwordError = ref<string>('');

const authStore = useAuthStore()
const router = useRouter()

const usernameRules = [
  (v: String) => (v.length < 2 || v.length > 32) ? 'Name must be between 2 and 32 characters' : ''
];

const passwordRules = [
  (v: string) => (v.length < 8 || v.length > 128) ? 'Password must be between 8 and 128 characters' : ''
];

const validateUsername = () => {
  for (const rule of usernameRules) {
    const error = rule(username.value);
    if (error) {
      usernameError.value = error;
      return;
    }
  }
  usernameError.value = '';
};

const validatePassword = () => {
  for (const rule of passwordRules) {
    const error = rule(password.value);
    if (error) {
      passwordError.value = error;
      return;
    }
  }
  passwordError.value = '';
};

// Async funkcija koja se zove kada se pritisne login dugme
// Radi validaciju svih polja, i samo ukoliko nema nijedna greska
// Zove api call za register i redirectuje korisinka na login
const onSubmit = async () => {
  validateUsername();
  validatePassword();

  if (usernameError.value || passwordError.value) {
    return;
  }

  const success = await authStore.login(username.value, password.value)
  if (!success) {
    return;
  }
    
  router.push({'name': 'draw'});
};
</script>
