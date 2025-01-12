<template>
    <div class="flex items-center justify-center gap-5 py-5 flex-col">
      <div v-if="error" class="text-red-600 bg-red-100 border-l-4 border-red-500 p-4 mb-4">
        {{ error }}
      </div>

      <h1 class="text-2xl font-bold">Appointments</h1>

         <!-- Pagination Component -->
    <Pagination
        v-slot="{ page }"
        :total="appointmentStore.totalElements"
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
  
        <div class="flex flex-wrap gap-4 justify-center items-center mb-6">
          <!-- Filter Fields -->
          <div>
            <label for="dateFilter" class="text-gray-500">Date: </label>
            <input
            id="dateFilter"
            type="date"
            v-model="filters.date"
            class="border border-gray-300 rounded-lg p-2"
            />
        </div>
        <div>
            <label for="timeFilter" class="text-gray-500">Time: </label>
            <input
            id="timeFilter"
            type="time"
            v-model="filters.time"
            class="border border-gray-300 rounded-lg p-2"
            />
        </div>
          <div>
            <label for="cuisineTypeFilter" class="text-gray-500">Cuisine: </label>
            <input 
                id="cuisineTypeFilter" 
                type="text" 
                v-model="filters.cuisineType" 
                class="border border-gray-300 rounded-lg p-2" 
                placeholder="Enter cuisine type"
            />
          </div>
          <div>
            <label for="minCapacityFilter" class="text-gray-500">Min Capacity: </label>
            <input
              id="minCapacityFilter"
              type="number"
              v-model="filters.capacity"
              class="border border-gray-300 rounded-lg p-2"
              placeholder="Enter min capacity"
            />
          </div>
          <div>
            <label for="locationFilter" class="text-gray-500">Location: </label>
            <input 
                id="locationFilter" 
                type="text" 
                v-model="filters.location" 
                class="border border-gray-300 rounded-lg p-2" 
                placeholder="Enter location"
            />
            </div>
          <button
            :disabled="loading"
            @click="fetchAppointments(filters)"
            class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
            Apply Filters
          </button>
        </div>
  
        <div v-if="appointmentStore.appointments.length" class="flex flex-wrap gap-4 justify-center">
          <div
            v-for="appointment in appointmentStore.appointments"
            :key="appointment.id"
            class="outline outline-indigo-500 rounded-lg border bg-card text-card-foreground min-w-[300px] transition basis-1/5 hover:shadow-xl cursor-pointer"
          >
            <!-- Appointment Restaurant Image -->
            <div class="aspect-square overflow-hidden rounded-t-lg">
              <img src='../assets/restaurant.jpg'
                alt="Restaurant Image"
                class="w-full h-full object-cover"
              />
            </div>
  
            <!-- Appointment Details -->
            <div class="flex flex-col gap-y-1.5 p-6">
              <div class="flex justify-between items-center">
                <p class="text-xl font-semibold text-center">{{ appointment.restaurant.name }}</p>
              </div>
              <!-- Address -->
              <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-map-marker-outline</v-icon>
                <p>{{ appointment.restaurant.address }}</p>
              </button>
              <!-- Description -->
              <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-text-box</v-icon>
                <p>{{ appointment.restaurant.description }}</p>
              </button>
              <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-sofa-single</v-icon>
                <p>{{ appointment.table.capacity }}</p>
              </button>
              <!-- Open Time -->
              <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-clock-time-four-outline</v-icon>
                <p>
                  {{ dayjs(new Date(appointment.time)).format('HH:mm') }} 
                  ({{ dayjs(new Date(appointment.time)).fromNow() }})
                </p>
              </button>
              <!-- Cuisine -->
              <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-food</v-icon>
                <p>{{ appointment.restaurant.type }}</p>
              </button>
              <!-- Book Appointment Button -->
              <button
                v-if="authStore.isClient"
                :disabled="loading"
                @click="bookAppointment(appointment.id)"
                class="bg-green-500 text-white px-4 py-2 mt-4 rounded-lg hover:bg-green-700"
              >
                Book Appointment
              </button>
            </div>
          </div>
        </div>
  
        <div v-else class="text-center text-gray-500">
          No appointments available. Try adjusting the filters.
        </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRestaurantStore } from '@/stores/restaurantStore'
  import { useAuthStore } from '@/stores/auth';
  import { useAppointmentStore } from '@/stores/appointmentsStore';
import { Button } from '@/components/ui/button'
  import dayjs from 'dayjs';
  import relativeTime from 'dayjs/plugin/relativeTime';

  import {
    Pagination,
    PaginationEllipsis,
    PaginationFirst,
    PaginationList,
    PaginationListItem,
    PaginationNext,
    PaginationPrev,
  } from '@/components/ui/pagination'
  
  dayjs.extend(relativeTime);
  
  interface Filters {
    capacity?: number;
    location?: string;
    date?: string;
    time?: string
    cuisineType?: string;
  }
  
  const restaurantStore = useRestaurantStore();
  const appointmentStore = useAppointmentStore();
  const authStore = useAuthStore();
  
  const filters = ref<Filters>({});
  const currentPage = ref(1);
  const loading = ref(false);
  const error = ref('');
  
  const fetchAppointments = async (filters: Filters) => {
    loading.value = true;
    try {
      const searchParams = new URLSearchParams();
      searchParams.append('page', (currentPage.value - 1).toString());
  
      if (filters.capacity) searchParams.append('capacity', filters.capacity.toString());
      if (filters.location) searchParams.append('location', filters.location);
      if (filters.date) searchParams.append('date', filters.date);
      if (filters.time) searchParams.append('time', filters.time);

      console.log(filters.date);
      console.log(filters.time);
  
      if (filters.cuisineType) searchParams.append('cuisineType', filters.cuisineType);
      searchParams.append('available', 'true');
      searchParams.append('size', '8');
  
      await appointmentStore.fetchAppointments(currentPage.value - 1, searchParams);
      console.log(appointmentStore.appointments);
    } catch (err: any) {
      error.value = err.message || 'An error occurred while fetching appointments.';
    } finally {
      loading.value = false;
    }
  };

  const bookAppointment = async (appointmentId: number) => {
      if (!authStore.uuid) {
        return;
      }

      loading.value = true;
      await restaurantStore.bookAppointment(appointmentId, authStore.uuid);
      loading.value = false;

      await fetchAppointments(filters.value);
  }

  const onPageClick = async (page: number) => {
    if (currentPage.value === page) {
        return;
    }

    currentPage.value = page;
    await fetchAppointments(filters.value);
  }
  
  onMounted(() => {
    fetchAppointments(filters.value);
  });
  </script>
  