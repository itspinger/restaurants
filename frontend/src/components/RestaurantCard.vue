<template>
    <div 
      v-if="restaurant" 
      @click="openRestaurant" 
      class="outline outline-indigo-500 rounded-lg border bg-card text-card-foreground min-w-[300px] transition basis-1/5 hover:shadow-xl cursor-pointer">
  
      <!-- Restaurant Image -->
      <div class="aspect-square overflow-hidden rounded-t-lg">
        <img src="../assets/restaurant.jpg" alt="Restaurant Image" class="w-full h-full object-cover">
      </div>
  
      <!-- Restaurant Details -->
      <div class="flex flex-col gap-y-1.5 p-6">
        <div class="flex justify-between items-center">
          <p class="text-xl font-semibold text-center">{{ restaurant.name }}</p>
          
          <!-- Edit and Delete Options -->
          <div class="flex gap-2">
            <Dialog title="Delete Restaurant" description="Are you sure you want to delete this restaurant? This action cannot be undone." @confirm="$emit('delete')">
              <template #trigger>
                <button class="hover:text-red-500">
                  <v-icon size="20px">mdi-delete-outline</v-icon>
                </button>
              </template>
            </Dialog>
          </div>
        </div>

        <!-- Dugme koje kad se klikne preusmerava na stranicu autora -->
        <button 
            class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm"
            @click.stop="pushToAuthorPage"
            >
            <v-icon>mdi-map-marker-outline</v-icon>
            <p>{{ restaurant.address }}</p>
        </button> 

        <button 
            class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm
            ">
            <v-icon>mdi-text-box</v-icon>
            <p>{{ restaurant.description }}</p>
        </button> 

        <button 
            class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm
            ">
            <v-icon>mdi-clock-time-four-outline</v-icon>
            <p>{{ restaurant.openTime }}</p>
        </button> 

        <button 
            class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm
            ">
            <v-icon>mdi-food</v-icon>
            <p>{{ restaurant.type }}</p>
        </button> 

        <button 
            class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm
            ">
            <v-icon>mdi-table-picnic</v-icon>
            <p>{{ restaurant.tables.length }} tables</p>
        </button> 
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, defineEmits } from 'vue';
  import { useRouter } from 'vue-router';
  import { useAuthStore } from '@/stores/auth';
  import { useRestaurantStore } from '@/stores/restaurantStore';
  import dayjs from 'dayjs';
  import relativeTime from 'dayjs/plugin/relativeTime';
  import Dialog from '@/components/Dialog.vue';
import Restaurant from '@/types/restaurant';
  
  dayjs.extend(relativeTime);
  
  const props = defineProps({
    id: {
        type: Number,
        required: true,
    },
  });

  
  const emit = defineEmits(['delete']);
  const router = useRouter();
  
  const authStore = useAuthStore();
  const restaurantStore = useRestaurantStore();
  const restaurant = ref<Restaurant | null>(null);
  const restaurantNameField = ref('');
  
  // Fetch restaurant details on mount
  onMounted(async () => {
    restaurant.value = await restaurantStore.fetchRestaurantsById(props.id);
  });
  
  // Check if the current user is the owner of the restaurant
  const isOwner = () => {
    return authStore.uuid !== null && authStore.uuid === restaurant.value?.managerId;
  };
  
  // Handle restaurant name editing
  const handleRestaurantNameEdit = () => {
    restaurantNameField.value = restaurant.value?.name || '';
  };
  
  // Save new restaurant name
  const saveRestaurantName = async () => {
    await restaurantStore.saveRestaurantName(restaurant.value?.restaurant_id!, restaurantNameField.value);
  };
  
  // Navigate to owner page
  const pushToOwnerPage = () => {
    router.push({ path: '/restaurants', query: { owner: restaurant.value?.owner.user_id } });
  };
  
  // Navigate to restaurant page
  const openRestaurant = () => {
    router.push({ name: 'restaurant', params: { id: restaurant.value?.restaurant_id } });
  };
  </script>
  
  <style scoped>
  /* Styles specific to this component */
  </style>
  