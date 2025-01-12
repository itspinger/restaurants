<template>
    <div v-if="restaurant" class="container mx-auto outline outline-3 outline-indigo-500 rounded-lg border bg-card basis-1/5 hover:shadow-xl cursor-pointer">
      <!-- Restaurant Details -->
      <div class="border rounded-lg bg-card text-card-foreground p-6 shadow-md">
        <h1 class="text-2xl font-semibold text-center mb-4">{{ restaurant.name }}</h1>
  
        <!-- Address -->
        <div class="flex items-center gap-2 text-gray-500 mb-2 hover:text-blue-500">
          <v-icon>mdi-map-marker-outline</v-icon>
          <p>{{ restaurant.address }}</p>
        </div>
  
        <!-- Description -->
        <div class="flex items-center gap-2 text-gray-500 mb-2 hover:text-blue-500">
          <v-icon>mdi-text-box</v-icon>
          <p>{{ restaurant.description }}</p>
        </div>
  
        <!-- Open Time -->
        <div class="flex items-center gap-2 text-gray-500 mb-2 hover:text-blue-500">
          <v-icon>mdi-clock-time-four-outline</v-icon>
          <p>{{ restaurant.openTime }}</p>
        </div>
  
        <!-- Type -->
        <div class="flex items-center gap-2 text-gray-500 mb-4 hover:text-blue-500">
          <v-icon>mdi-food</v-icon>
          <p>{{ restaurant.type }}</p>
        </div>
  
        <!-- Tables Section -->
        <div>
          <p class="text-gray-500 font-medium mb-2">Tables:</p>
          <div class="grid grid-cols-5 gap-4">
            <div 
              v-for="table in restaurant.tables" 
              :key="table.id" 
              @disabled="processing"
              @click="viewAppointments(table)" 
              class="flex flex-col border border-gray-300 rounded-lg p-4 bg-white hover:shadow-lg cursor-pointer outline outline-1">
              <p><v-icon>mdi-table-picnic</v-icon> {{ table.id }}</p>
              <p><v-icon>mdi-smoking-off</v-icon> {{ table.zone }}</p>
              <p><v-icon>mdi-sofa-single</v-icon> {{ table.capacity }}</p>
            </div>
          </div>
        </div>
  
        <!-- Add Table Button for Managers -->
        <div v-if="isOwner()" class="mt-3">
          <Dialog @confirm="addTable" title="Create Table" description="Fill out the form in order to create the table">
            <template #trigger>
                <button class="px-4 py-2 bg-green-500 text-white rounded-lg">Add Table</button>
            </template>
            <template #content>
              <div class="flex flex-col gap-4">
                <select v-model="newTableZone" class="border border-gray-300 rounded-lg p-2">
                    <option value="smoking">Smoking</option>
                    <option value="no-smoking">No Smoking</option>
                </select>
                <input type="number" v-model="newTableCapacity" placeholder="Enter table capacity" class="border border-gray-300 rounded-lg p-2">
              </div>
            </template>
          </Dialog>
        </div>
      </div>
    </div>

    <!-- Display Information for each appointment -->
    <div v-if="selectedTable" class="container mx-auto outline outline-3 outline-indigo-500 rounded-lg border bg-card basis-1/5 hover:shadow-xl cursor-pointer mt-10">
        <div class="border rounded-lg bg-card text-card-foreground p-6 shadow-md">
            <h1 class="text-2xl font-semibold text-center mb-4">Appointments</h1>
            <div class="mt-6">
                <div class="flex items-center gap-2 text-gray-500 mb-4 justify-center">
                    <label for="dateFilter" class="text-gray-500">Filter by Date: </label>
                    <input 
                        id="dateFilter" 
                        type="date" 
                        v-model="date" 
                        class="border border-gray-300 rounded-lg p-2"
                    />
                    <button :disabled="processing" @click="fetchAppointments" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Apply Filters</button>
                </div>
                <ul v-if="appointments && appointments.length > 0" class="divide-y divide-gray-200">
                    <li v-for="appointment in appointments" :key="appointment.id" class="py-2 flex justify-between items-center">
                        <p>
                            {{ dayjs(new Date(appointment.time)).format('HH:mm') }} 
                            ({{ dayjs(new Date(appointment.time)).fromNow() }})
                        </p>
                        <button 
                        :disabled="processing"
                        v-if="authStore.isClient"
                        @click="bookAppointment(appointment.id)" 
                        class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
                        >
                        Book
                        </button>
                    </li>
                </ul>
                <div v-if="isOwner()" class="mt-3">
                    <Dialog @confirm="addAppointment" title="Create Appointment" description="Select a date and time for the appointment">
                        <template #trigger>
                            <button class="px-4 py-2 bg-green-500 text-white rounded-lg">Add Appointment</button>
                        </template>
                        <template #content>
                            <div class="flex flex-col gap-4">
                            <input type="datetime-local" v-model="newAppointmentTime" placeholder="Select date and time" class="border border-gray-300 rounded-lg p-2"
                            >
                            </div>
                        </template>
                    </Dialog>
                </div>
            </div>
        </div>
    </div>
    <div v-if="isOwner() || authStore.isClient" class="container mx-auto outline outline-3 outline-indigo-500 rounded-lg border bg-card basis-1/5 hover:shadow-xl cursor-pointer mt-10">
    <div class="border rounded-lg bg-card text-card-foreground p-6 shadow-md">
      <h1 class="text-2xl font-semibold text-center mb-4">Available Reservations</h1>
      <ul v-if="reservationStore.reservations && reservationStore.reservations.length > 0" class="flex flex-wrap gap-4 justify-center">
        <li v-for="reservation in reservationStore.reservations" :key="reservation.id" class="outline outline-indigo-500 rounded-lg border bg-card text-card-foreground min-w-[300px] transition basis-1/5 hover:shadow-xl cursor-pointer">
          <!-- Appointment Details -->
          <div class="flex flex-col gap-y-1.5 p-6">
                <!-- Address -->
                <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-map-marker-outline</v-icon>
                <p>{{ reservation.restaurant.address }}</p>
            </button>
            <!-- Capacity -->
            <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-sofa-single</v-icon>
                <p>{{ reservation.appointment.table.capacity }}</p>
            </button>
            <!-- Open Time -->
            <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-clock-time-four-outline</v-icon>
                <p>
                {{ dayjs(new Date(reservation.appointment.time)).format('HH:mm') }} 
                ({{ dayjs(new Date(reservation.appointment.time)).fromNow() }})
                </p>
            </button>
            <!-- Cuisine -->
            <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-food</v-icon>
                <p>{{ reservation.restaurant.type }}</p>
            </button>
            <!-- Cancel Reservation Button -->
            <button 
                :disabled="processing"
                @click="cancelReservation(reservation.id)"
                class="bg-red-500 text-white px-4 py-2 mt-4 rounded-lg hover:bg-red-700"
            >
                Cancel Appointment
            </button>
          </div>
        </li>
      </ul>

      <Pagination
        v-slot="{ page }"
        :total="reservationStore.totalElements"
        :sibling-count="1"
        show-edges
        :default-page="currentPage"
        :items-per-page="8"
        class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm justify-center py-5"
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
    </div>
  </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  import { useAuthStore } from '@/stores/auth';
  import { useRestaurantStore } from '@/stores/restaurantStore';
  import { useReservationStore } from '@/stores/reservationStore';
  import Reservation from '@/types/reservation';
  import Restaurant from '@/types/restaurant';
  import Table from '@/types/table';
  import Dialog from '@/components/Dialog.vue'
  import dayjs from 'dayjs'
  import relativeTime from 'dayjs/plugin/relativeTime'
  import { useNotify } from '@f3ve/vue-notify'

  import { Button } from '@/components/ui/button'

  import {
    Pagination,
    PaginationEllipsis,
    PaginationFirst,
    PaginationList,
    PaginationListItem,
    PaginationNext,
    PaginationPrev,
  } from '@/components/ui/pagination'

  dayjs.extend(relativeTime)
  
  const router = useRouter();
  const route = useRoute();

  const authStore = useAuthStore();
  const restaurantStore = useRestaurantStore();
  const reservationStore = useReservationStore();

  const restaurant = ref<Restaurant | null>(null);
  const selectedTable = ref<Table | null>(null);
  const appointments = ref<{ id: number, time: string }[] | null>([]);
  const reservations = ref<Reservation[] | null>([]);  // List of appointments for the selected table
  const date = ref(new Date().toISOString().split('T')[0]);
  const processing = ref(false);
  
  // Fields for new table
  const newTableZone = ref('smoking');
  const newTableCapacity = ref(2);
  const newAppointmentTime = ref(new Date().toISOString().slice(0, 16));

  const currentPage = ref(1);
  
  onMounted(async () => {
    if (!route.params.id) {
      return;
    }
  
    restaurant.value = await restaurantStore.fetchRestaurantsById(route.params.id);
    if (!restaurant.value) {
        return;
    }

    await fetchReservations();
  });

  const fetchReservations = async() => {
    if (!restaurant.value) {
        return;
    }

    const params = new URLSearchParams();
    params.append('page', (currentPage.value - 1).toString())
    params.append('restaurantId', restaurant.value.id.toString());
    params.append('available', 'true');
    if (authStore.isClient && authStore.uuid) {
        params.append('userId', authStore.uuid.toString());
    }

    await reservationStore.fetchReservations(currentPage.value, params);
  }
  
  const isOwner = () => {
    return authStore.uuid !== null && authStore.uuid === restaurant.value?.managerId;
  };
  
  const viewAppointments = async (table: Table) => {
    selectedTable.value = table;
    await fetchAppointments();
  };

  const fetchAppointments = async() => {
    if (!selectedTable.value?.id) {
        return;
    }
    appointments.value = await restaurantStore.fetchAppointmentsByTableId(selectedTable.value?.id, date.value);
  }
  
  const bookAppointment = async (appointmentId: number) => {
      if (!authStore.uuid) {
        return;
      }

      processing.value = true;
      await restaurantStore.bookAppointment(appointmentId, authStore.uuid);
      processing.value = false;

      await fetchAppointments();
  }

  const cancelReservation = async (reservationId: number) => {
     processing.value = true;
     await reservationStore.cancelReservation(reservationId);
     await fetchReservations();
     processing.value = false;
  }

  const addTable = async () => {
    if (newTableZone.value && newTableCapacity.value >= 1) {
      processing.value = true;
      const newTable = await restaurantStore.addTable(restaurant.value!.id, newTableZone.value, newTableCapacity.value);
      processing.value = false;
      if (newTable) {
        restaurant.value!.tables.push(newTable);
      }
    } else {
      useNotify().show('Capacity must be at least 1', 'error');
    }
  };
  
  const addAppointment = async () => {
    if (newAppointmentTime.value && selectedTable.value) {
      processing.value = true;
      const appointment = await restaurantStore.addAppointment(selectedTable.value.id, newAppointmentTime.value);
      processing.value = false;
      if (appointment) {
        appointments.value?.push(appointment);
      }
    }
  };

    // Handle pagination click
const onPageClick = async (page: number) => {
    if (currentPage.value === page) {
        return;
    }

    currentPage.value = page;
    await fetchReservations();
}
</script>
  