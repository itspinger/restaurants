<template>
    <div class="flex items-center justify-center bg-white dark:bg-gray-900" style="padding-top:100px">
      <div class="container mx-auto">
        <div class="max-w-md mx-auto my-10">
          <div class="text-center">
            <h1 class="my-3 text-3xl font-semibold text-gray-700 dark:text-gray-200">Reset Password</h1>
            <p class="text-gray-500 dark:text-gray-400">Enter your new password</p>
          </div>
  
          <div class="m-7">
            <form @submit.prevent="resetPassword">
              <!-- New Password input field -->
              <div class="mb-6">
                <label
                  for="newPassword"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >New Password</label
                >
                <input
                  v-model="newPassword"
                  type="password"
                  id="newPassword"
                  placeholder="Your New Password"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{
                    'border-red-500': newPasswordError,
                  }"
                  @input="validatePassword"
                  required
                />
                <span v-if="newPasswordError" class="text-sm text-red-500">{{ newPasswordError }}</span>
              </div>
  
              <!-- Confirm Password input field -->
              <div class="mb-6">
                <label
                  for="confirmPassword"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >Confirm Password</label
                >
                <input
                  v-model="confirmPassword"
                  type="password"
                  id="confirmPassword"
                  placeholder="Confirm Your Password"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{
                    'border-red-500': confirmPasswordError,
                  }"
                  @input="validateConfirmPassword"
                  required
                />
                <span v-if="confirmPasswordError" class="text-sm text-red-500">{{ confirmPasswordError }}</span>
              </div>
  
              <!-- Submit Button -->
              <div class="mb-6">
                <button
                  type="submit"
                  class="w-full px-3 py-4 text-white bg-indigo-500 rounded-md focus:bg-indigo-600 focus:outline-none"
                >
                  Reset Password
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
  import { RouterLink, useRouter, useRoute } from 'vue-router';
  import { ref } from 'vue';
  import { useAuthStore } from '@/stores/auth';
  
  const token = ref<string>(useRoute().query.token as string || '');
  const newPassword = ref<string>('');
  const confirmPassword = ref<string>('');
  const newPasswordError = ref<string>('');
  const confirmPasswordError = ref<string>('');
  const router = useRouter();
  
  const authStore = useAuthStore()
  
  const passwordRules = [
    (v: string) => (v.length < 8 || v.length > 128) ? 'Password must be between 8 and 128 characters' : ''
  ];
  
  const validatePassword = () => {
    for (const rule of passwordRules) {
      const error = rule(newPassword.value);
      if (error) {
        newPasswordError.value = error;
        return;
      }
    }
    newPasswordError.value = '';
  };
  
  const validateConfirmPassword = () => {
    if (newPassword.value !== confirmPassword.value) {
      confirmPasswordError.value = "Passwords do not match.";
      return;
    }
    confirmPasswordError.value = '';
  };
  
  const resetPassword = async () => {
    validatePassword();
    validateConfirmPassword();
  
    if (newPasswordError.value || confirmPasswordError.value) {
      return;
    }
  
    const success = await authStore.resetPassword(newPassword.value, token.value);
    if (!success) {
      return;
    }
  
    router.push({'name': 'login'});
  };
  </script>
  