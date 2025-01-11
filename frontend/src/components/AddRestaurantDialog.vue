<template>
    <Dialog
      title="Add New Restaurant"
      description="Fill in the details to create a new restaurant."
      confirm-label="Create"
      @confirm="createRestaurant"
    >
      <template #trigger>
        <button
          class="flex items-center gap-2 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
        >
          <v-icon size="20px">mdi-plus</v-icon>
          Add Restaurant
        </button>
      </template>
      <template #content>
        <form @submit.prevent="createRestaurant">
          <input
            v-model="name"
            placeholder="Restaurant Name"
            required
            class="mb-3 w-full p-2 border rounded-md"
          />
          <input
            v-model="imageUrl"
            placeholder="Image URL"
            required
            class="mb-3 w-full p-2 border rounded-md"
          />
        </form>
      </template>
    </Dialog>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import Dialog from '@/components/Dialog.vue';
  import { useRestaurantStore } from '@/stores/restaurantStore';
  
  const restaurantStore = useRestaurantStore();
  
  const name = ref('');
  const imageUrl = ref('');
  
  // Create a new restaurant
  const createRestaurant = async () => {
    if (name.value && imageUrl.value) {
      await restaurantStore.addRestaurant({ name: name.value, image: imageUrl.value });
      name.value = '';
      imageUrl.value = '';
    }
  };
  </script>
  
  <style scoped>
  /* Add styles if necessary */
  </style>
  