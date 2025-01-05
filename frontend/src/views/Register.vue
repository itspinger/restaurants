<template>
    <div class="flex items-center justify-center bg-white dark:bg-gray-900" style="padding-top:100px">
      <div class="container mx-auto">
        <div class="max-w-md mx-auto my-10">
          <!-- Text na vrhu -->
          <div class="text-center">
            <h1 class="my-3 text-3xl font-semibold text-gray-700 dark:text-gray-200">Sign Up</h1>
            <p class="text-gray-500 dark:text-gray-400">Create your account to get started</p>
          </div>
  
          <div class="m-7">
            <form @submit.prevent="onSubmit">
              <!-- Username Field -->
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

              <!-- Prikazi gresku za username ako postoji -->
              <span v-if="usernameError" class="text-sm text-red-500">{{ usernameError }}</span>
            </div>
  
              <!-- Password Field -->
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
              
              <!-- Prikazi gresku za password ako postoji -->
              <span v-if="passwordError" class="text-sm text-red-500">{{ passwordError }}</span>
            </div>
  
              <!-- Confirm Password Field -->
              <div class="mb-6">
                <label for="confirmPassword" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Confirm Password</label>
                <input
                  v-model="confirmPassword"
                  type="password"
                  id="confirmPassword"
                  placeholder="Confirm your password"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': confirmPasswordError }"
                  @input="validateConfirmPassword"
                  required
                />

                <!-- Prikazi gresku za confirm password ako postoji -->
                <span v-if="confirmPasswordError" class="text-sm text-red-500">{{ confirmPasswordError }}</span>
              </div>
  
              <!-- Submit Button -->
              <div class="mb-6">
                <button
                  type="submit"
                  class="w-full px-3 py-4 text-white bg-indigo-500 rounded-md focus:bg-indigo-600 focus:outline-none"
                >
                  Sign Up
                </button>
              </div>
  
              <!-- Ako korisnik vec ima acc, moze da se redirectuje na login stranicu -->
              <p class="text-sm text-center text-gray-400">
                Already have an account?
                <RouterLink to="/login" 
                  class="text-indigo-400 focus:outline-none focus:underline focus:text-indigo-500 dark:focus:border-indigo-800">
                  Sign in
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
import { useAuthStore } from '@/stores/auth'
  

const username = ref<string>('');
const password = ref<string>('');
const confirmPassword = ref<string>('');
    
const usernameError = ref<string>('');
const passwordError = ref<string>('');
const confirmPasswordError = ref<string>('');
  
const authStore = useAuthStore()
const router = useRouter()

const usernameRules = [
  (v: string) => (v.length < 2 || v.length > 32) ? 'Username must be between 2 and 32 characters' : ''
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
  
const validateConfirmPassword = () => {
  for (const rule of passwordRules) {
    const error = rule(confirmPassword.value);
    if (error) {
      confirmPasswordError.value = error;
      return;
    }
  }

  if (confirmPassword.value !== password.value) {
    confirmPasswordError.value = 'Passwords do not match';
    return;
  }

  confirmPasswordError.value = "";
};
  
// Async funkcija koja se zove kada se pritisne signup dugme
// Radi validaciju svih polja, i samo ukoliko nema nijedna greska
// Zove api call za register i redirectuje korisinka na login
const onSubmit = async () => {
  validateUsername();
  validatePassword();
  validateConfirmPassword();
  
  if (usernameError.value || passwordError.value || confirmPasswordError.value) {
    return;
  }

  const response = await authStore.register(username.value, password.value);
  if (!response) {
    return;
  }

  router.push({'name': 'login'});
};
</script>
  