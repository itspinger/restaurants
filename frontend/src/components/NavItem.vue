<template>
    <li class="item">
      <RouterLink
        :to="to" 
        class="inline-block px-4 py-2 text-lg font-normal text-gray-800 no-underline rounded-md dark:text-gray-200 hover:text-indigo-500 focus:text-indigo-500 focus:bg-indigo-100 focus:outline-none dark:focus:bg-gray-800"
      >
        <slot></slot>
      </RouterLink>
    </li>
  </template>
  
<script setup lang="ts">
import { RouterLink } from 'vue-router';
  
defineProps<{
  to: string | { path: string, query?: Record<string, string> };
}>();
</script>
  
<style scoped>
.item {
  margin-right: 1rem;
}
</style>
  