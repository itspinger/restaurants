<template>
    <div class="container p-8 mx-auto xl:px-0">
      <div class="flex flex-wrap">
        <div class="flex items-center w-full lg:w-1/2">
          <div class="max-w-2xl mb-8">
            <h1 class="text-4xl font-bold leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-6xl xl:leading-tight dark:text-white">
              Feastly
            </h1>
            <p class="py-5 text-xl leading-normal text-gray-500 lg:text-lg xl:text-xl dark:text-gray-300">
              Feastly is your go-to platform for effortlessly booking dining experiences. 
              Whether you're planning an intimate dinner, a family lunch, or a gathering with friends, Feastly simplifies the process by offering a seamless way to reserve your ideal restaurant.
              Discover a variety of options, customize the number of guests, and enjoy unforgettable moments with ease.
            </p>
  
            <div class="flex flex-col items-start space-y-3 sm:space-x-4 sm:space-y-0 sm:items-center sm:flex-row">
              <RouterLink to="/restaurants"
                class="px-8 py-4 text-lg font-medium text-center text-white bg-indigo-600 rounded-md">
                Book Now
              </RouterLink>
            </div>
          </div>
        </div>
        <div class="flex items-center justify-center w-full lg:w-1/2">
          <img src="../assets/hero.png" class="object-cover" alt="Hero Illustration"/>
        </div>
      </div>
      <div>
        <div class="flex flex-col justify-center">
          <div class="text-xl text-center text-gray-700 dark:text-white p-12">
            Trusted by <span class="text-indigo-600">2000+</span>
            customers worldwide
          </div>
        </div>
      </div>
    </div>
  </template>
  
<script setup lang="ts">
  import { RouterLink } from 'vue-router';
</script>

<style scoped>
.container {
  padding-top: 100px;
}
</style>