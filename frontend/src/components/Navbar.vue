<template>
  <div class="w-full">
    <nav class="container flex items-center justify-between p-4 mx-auto">
      <RouterLink to="/" class="flex items-center text-2xl font-medium text-indigo-500">
        <span><img src="../assets/logo.png" alt="Logo"/></span>
      </RouterLink>

      <div class="flex flex-1 justify-center">
        <ul class="flex items-center space-x-4 list-none">
          <NavItem to="/">Home</NavItem>
          <NavItem :to="{ path: '/draw/'}">Draw</NavItem>
          <NavItem to="/gallery/1">Gallery</NavItem>
          <NavItem v-if="authStore.isLoggedIn" :to="{ path: '/gallery/1', query: { author: authStore.uuid } }">My Gallery</NavItem>
          <NavItem v-if="!authStore.isLoggedIn" to="/login">Login</NavItem>
          <NavItem v-if="!authStore.isLoggedIn" to="/register">Register</NavItem>
          <button 
              v-if="authStore.isLoggedIn"
              @click="authStore.logout()" 
              class="inline-block px-4 py-2 text-lg font-normal text-gray-800 no-underline rounded-md dark:text-gray-200 hover:text-indigo-500 focus:text-indigo-500 focus:bg-indigo-100 focus:outline-none dark:focus:bg-gray-800">
              Logout
          </button>
        </ul>
      </div>

      <div class="item ml-4">
        <RouterLink v-if="!authStore.isLoggedIn" to="/draw" class="hidden lg:flex px-6 py-2 text-white bg-indigo-600 rounded-md">Get Started</RouterLink>
        <p v-if="authStore.isLoggedIn" class="hidden lg:flex px-6 py-2 text-white bg-indigo-600 rounded-md">Welcome, {{  authStore.username }}</p>
      </div>
    </nav>
  </div>
</template>

<script setup lang="ts"> 
    import { RouterLink } from 'vue-router';
    import { useAuthStore } from '@/stores/auth';
    import NavItem from './NavItem.vue';

    const authStore = useAuthStore()
</script>
