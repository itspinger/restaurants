<template>
    <div class="flex items-center justify-center bg-white dark:bg-gray-900" style="padding-top:100px">
      <div class="container mx-auto">
        <div class="max-w-md mx-auto my-10">
          <div class="text-center">
            <h1 class="my-3 text-3xl font-semibold text-gray-700 dark:text-gray-200">Reset Password</h1>
            <p class="text-gray-500 dark:text-gray-400">Enter your email to receive a reset link</p>
          </div>
  
          <div class="m-7">
            <form @submit.prevent="sendResetLink">
              <!-- Email input field -->
              <div class="mb-6">
                <label
                  for="email"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >Email</label
                >
                <input
                  v-model="email"
                  type="email"
                  id="email"
                  placeholder="Enter your email"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': emailError }"
                  @input="validateEmail"
                  required
                />
                <span v-if="emailError" class="text-sm text-red-500">{{ emailError }}</span>
              </div>
  
              <!-- Submit Button -->
              <div class="mb-6">
                <button
                  type="submit"
                  class="w-full px-3 py-4 text-white bg-indigo-500 rounded-md focus:bg-indigo-600 focus:outline-none"
                >
                  Send Reset Link
                </button>
              </div>
  
              <p class="text-sm text-center text-gray-400">
                <RouterLink to="/login" class="text-indigo-400 focus:outline-none focus:underline focus:text-indigo-500 dark:focus:border-indigo-800">
                  Back to Login
                </RouterLink>
              </p>
            </form>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { RouterLink, useRouter } from 'vue-router';
  import { ref } from 'vue';
  import { useAuthStore } from '@/stores/auth';
  
  const email = ref<string>('');
  const emailError = ref<string>('');
  
  const authStore = useAuthStore()
  const router = useRouter()
  
  const emailRules = [
    (v: string) => !/\S+@\S+\.\S+/.test(v) ? 'Invalid email' : ''
  ];
  
  const validateEmail = () => {
    for (const rule of emailRules) {
      const error = rule(email.value);
      if (error) {
        emailError.value = error;
        return;
      }
    }
    emailError.value = '';
  };
  
  const sendResetLink = async () => {
    validateEmail();
  
    if (emailError.value) {
      return;
    }
  
    const success = await authStore.requestPasswordReset(email.value)
    if (!success) {
      return;
    }
      
    router.push('/login');
  };
  </script>
  