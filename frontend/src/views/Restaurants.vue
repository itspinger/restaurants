<template>
   <div class= "flex items-center justify-center gap-5 py-5 flex-col">
      <h1 class="text-2xl font-bold">Restaurants</h1>
      <RouterLink to="/restaurants/new">
        <button
            v-if="authStore.isManager"
            class="flex items-center gap-2 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
        >
            <v-icon size="20px">mdi-plus</v-icon>
            Add Restaurant
        </button>
      </RouterLink>

      <!-- Pagination Component -->
      <Pagination
        v-slot="{ page }"
        :total="restaurantStore.totalElements"
        :sibling-count="1"
        show-edges
        :default-page="currentPage"
        :items-per-page="8"
      >
        <PaginationList v-slot="{ items }" class="flex items-center justify-center gap-1">
          <PaginationFirst @click="onPageClick(1)" />
          <PaginationPrev @click="onPageClick(currentPage - 1)" />
  
          <template v-for="(item, index) in items">
            <PaginationListItem
              v-if="item.type === 'page'"
              :key="index"
              :value="item.value"
              as-child
            >
              <Button
                class="w-10 h-10 p-0"
                :variant="item.value === currentPage ? 'default' : 'outline'"
                @click="onPageClick(item.value)"
              >
                {{ item.value }}
              </Button>
            </PaginationListItem>
            <PaginationEllipsis v-else :key="item.type" :index="index" />
          </template>
  
          <PaginationNext @click="onPageClick(currentPage + 1)" />
        </PaginationList>
      </Pagination>
  
      <!-- Loader -->
      <pulse-loader :loading="loading" color="#2596be" size="12px" />
  
      <!-- No Restaurants Found -->
      <h2
        v-if="!loading && restaurantStore.restaurants.length === 0"
        class="text-lg text-gray-500"
      >
        No restaurants found
      </h2>
  
      <!-- Restaurant Cards -->
      <div
        class="flex flex-wrap gap-10 justify-center mb-20 py-3"
        v-if="!loading"
        style="position:relative"
      >
        <RestaurantCard
          v-for="restaurant in restaurantStore.restaurants"
          :key="restaurant.id"
          :id="restaurant.id"
          :restaurant="restaurant"
        />
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, watch } from 'vue'
  import { useRestaurantStore } from '@/stores/restaurantStore'
  import { useAuthStore } from '@/stores/auth'
  import { Button } from '@/components/ui/button'
  import RestaurantCard from '@/components/RestaurantCard.vue'

  import {
    Pagination,
    PaginationEllipsis,
    PaginationFirst,
    PaginationList,
    PaginationListItem,
    PaginationNext,
    PaginationPrev,
  } from '@/components/ui/pagination'
  
  const restaurantStore = useRestaurantStore()
  const authStore = useAuthStore()

  const currentPage = ref(1)
  const loading = ref(true)
  
  // Fetch restaurants
  const fetchRestaurants = async () => {
    loading.value = true
    await restaurantStore.fetchRestaurants(currentPage.value - 1)
    loading.value = false
  }
  
  // Watch for route changes
  onMounted(fetchRestaurants)

  // Handle pagination click
  const onPageClick = async (page: number) => {
    if (currentPage.value === page) {
        return;
    }

    currentPage.value = page;
    fetchRestaurants();
  }
  </script>
  