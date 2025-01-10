<template>
    <div class="flex items-center justify-center bg-white dark:bg-gray-900" style="padding-top:100px">
      <div class="container mx-auto">
        <div class="max-w-md mx-auto">
          <!-- Text na vrhu -->
          <div class="text-center">
            <h1 class="text-3xl font-semibold text-gray-700 dark:text-gray-200">Sign Up</h1>
            <p class="text-gray-500 dark:text-gray-400">Create your account to get started</p>
          </div>
  
          <div class="m-7">
            <form @submit.prevent="onSubmit">
              <div class="mb-6">
                <label for="role" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Role</label>
                <select v-model="role" id="role" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500">
                  <option value="manager">Manager</option>
                  <option value="client">Client</option>
                </select>
              </div>

              <div class="mb-6">
                <label for="firstName" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">First Name</label>
                <input
                  v-model="firstName"
                  type="text"
                  id="firstName"
                  placeholder="Enter your first name"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': firstNameError }"
                  @input="validateFirstName"
                  required
                />
                <span v-if="firstNameError" class="text-sm text-red-500">{{ firstNameError }}</span>
              </div>

              <div class="mb-6">
                <label for="lastName" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Last Name</label>
                <input
                  v-model="lastName"
                  type="text"
                  id="lastName"
                  placeholder="Enter your last name"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': lastNameError }"
                  @input="validateLastName"
                  required
                />
                <span v-if="lastNameError" class="text-sm text-red-500">{{ lastNameError }}</span>
              </div>

              <div class="mb-6">
                <label for="email" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Email</label>
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

                <div class="mb-6">
                  <label for="dob" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Date of Birth</label>
                  <input
                    v-model="dob"
                    type="date"
                    id="dob"
                    placeholder="Enter your Date of Birth"
                    class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                    :class="{ 'border-red-500': dobError }"
                    @input="validateDob"
                    required
                  />
                  <span v-if="dobError" class="text-sm text-red-500">{{ dobError }}</span>
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
  
const role = ref<string>('client');
const firstName = ref<string>('');
const lastName = ref<string>('');
const email = ref<string>('');
const username = ref<string>('');
const password = ref<string>('');
const confirmPassword = ref<string>('');
const dob = ref<string>('2003-01-01');
    
const firstNameError = ref<string>('');
const lastNameError = ref<string>('');
const emailError = ref<string>('');
const usernameError = ref<string>('');
const passwordError = ref<string>('');
const confirmPasswordError = ref<string>('');
const dobError = ref<string>('');
  
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

const validateFirstName = () => {
  firstNameError.value = firstName.value ? '' : 'First Name is required';
};

const validateLastName = () => {
  lastNameError.value = lastName.value ? '' : 'Last Name is required';
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

const validateDob = () => {
  if (!dob.value) {
    dobError.value = 'Date of Birth is required';
    return;
  }

  const dobDate = new Date(dob.value);
  const age = new Date().getFullYear() - dobDate.getFullYear();

  if (age < 18 || age > 99) {
    dobError.value = 'You must be between 18 and 99 years old';
  } else {
    dobError.value = '';
  }
};

const validateEmail = () => {
  const emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  if (!email.value) {
    emailError.value = 'Email is required';
    return;
  }
  if (!emailRegex.test(email.value)) {
    emailError.value = 'Please enter a valid email address';
    return;
  }
  emailError.value = '';
};
  
// Async funkcija koja se zove kada se pritisne signup dugme
// Radi validaciju svih polja, i samo ukoliko nema nijedna greska
// Zove api call za register i redirectuje korisinka na login
const onSubmit = async () => {
  validateFirstName();
  validateLastName();
  validateUsername();
  validatePassword();
  validateConfirmPassword();
  validateDob();

  if (firstNameError.value || lastNameError.value || usernameError.value || passwordError.value || confirmPasswordError.value || dobError.value) {
    return;
  }

  const response = await authStore.register(role.value.toLowerCase(), firstName.value, lastName.value, email.value, username.value, password.value, dob.value);
  if (!response) {
    return;
  }

  router.push({ name: 'login' });
};
</script>
  