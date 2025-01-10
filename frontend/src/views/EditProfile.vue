<template>
    <div class="flex items-center justify-center bg-white dark:bg-gray-900" style="padding-top:100px">
      <div class="container mx-auto">
        <div class="max-w-md mx-auto">
          <!-- Header -->
          <div class="text-center">
            <h1 class="text-3xl font-semibold text-gray-700 dark:text-gray-200">Edit Profile</h1>
            <p class="text-gray-500 dark:text-gray-400">Update your account details</p>
          </div>
  
          <div class="m-7">
            <form @submit.prevent="onSubmit">
              <!-- Profile fields similar to the register view -->
              <div class="mb-6">
                <label for="firstName" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">First Name</label>
                <input
                  v-model="firstName"
                  type="text"
                  id="firstName"
                  placeholder="Enter your first name"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                />
              </div>
  
              <div class="mb-6">
                <label for="lastName" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Last Name</label>
                <input
                  v-model="lastName"
                  type="text"
                  id="lastName"
                  placeholder="Enter your last name"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                />
              </div>
  
              <div class="mb-6">
                <label for="email" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Email</label>
                <input
                  v-model="email"
                  type="email"
                  id="email"
                  placeholder="Enter your email"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                />
              </div>
  
              <div class="mb-6">
                <label for="password" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">New Password</label>
                <input
                  v-model="password"
                  type="password"
                  id="password"
                  placeholder="Enter a new password"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                />
              </div>

              <div class="mb-6">
                <label for="dob" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Date of Birth</label>
                <input
                    v-model="dob"
                    type="date"
                    id="dob"
                    class="w-full px-3 py-2 border rounded-md dark:bg-gray-700 dark:text-white"
                />
              </div>
  
              <!-- Save Button -->
              <div class="mb-6">
                <button
                  type="submit"
                  class="w-full px-3 py-4 text-white bg-indigo-500 rounded-md focus:bg-indigo-600 focus:outline-none"
                >
                  Save Changes
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
  
      <!-- Password confirmation dialog -->
      <div v-if="showPasswordDialog" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center">
        <div class="bg-white rounded-lg p-6 w-96">
          <h2 class="text-lg font-semibold text-gray-700">Confirm Password Change</h2>
          <p class="mt-2 text-sm text-gray-600">Please confirm your new password to proceed.</p>
          <input
            v-model="passwordConfirmation"
            type="password"
            placeholder="Confirm your password"
            class="w-full px-3 py-2 mt-4 border border-gray-300 rounded-md focus:outline-none"
          />
          <div class="flex justify-end mt-4 space-x-2">
            <button @click="confirmPasswordChange" class="px-4 py-2 bg-indigo-500 text-white rounded-md">Confirm</button>
            <button @click="cancelPasswordChange" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md">Cancel</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const firstName = ref('');
const lastName = ref('');
const email = ref('');
const password = ref('');
const passwordConfirmation = ref('');
const dob = ref('');
const showPasswordDialog = ref(false);

onMounted(async () => {
    const profile = await authStore.fetchSelfUser();
    if (!profile) {
        return;
    }

    firstName.value = profile.firstName;
    lastName.value = profile.lastName;
    email.value = profile.email;
    dob.value = profile.birthDate;
});

const onSubmit = () => {
  if (password.value) {
    showPasswordDialog.value = true;
  } else {
    saveProfile();
  }
};

const confirmPasswordChange = () => {
  if (password.value === passwordConfirmation.value) {
    saveProfile();
  } else {
    alert('Passwords do not match!');
  }
  showPasswordDialog.value = false;
};

const cancelPasswordChange = () => {
  showPasswordDialog.value = false;
};

const saveProfile = async () => {
  await authStore.updateProfile(
    firstName.value,
    lastName.value,
    email.value,
    dob.value,
    password.value || null,
  );
};
</script>
