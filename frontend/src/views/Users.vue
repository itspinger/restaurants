<template>
    <div v-if="!authStore.isAdmin" class="text-center py-5">
        <h1 class="my-3 text-3xl font-semibold text-gray-700 dark:text-gray-200">Error</h1>
        <p class="text-gray-500 dark:text-gray-400">Failed to verify you as an admin</p>
    </div>
    <div class="flex items-center justify-center gap-5 py-5 flex-col">
      <div v-if="error" class="text-red-600 bg-red-100 border-l-4 border-red-500 p-4 mb-4">
        {{ error }}
      </div>

    <h1 class="text-2xl font-bold">Users</h1>

         <!-- Pagination Component -->
    <Pagination
        v-slot="{ page }"
        :total="userStore.totalElements"
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
  
        <div v-if="userStore.users.length > 0" class="flex flex-wrap gap-4 justify-center">
          <div
            v-for="user in userStore.users"
            :key="user.id"
            class="outline outline-indigo-500 rounded-lg border bg-card text-card-foreground min-w-[300px] transition basis-1/5 hover:shadow-xl cursor-pointer"
          >
            <!-- Appointment Details -->
            <div class="flex flex-col gap-y-1.5 p-6">
              <div class="flex justify-between items-center">
                <p class="text-xl font-semibold text-center">{{ user.username }}</p>
              </div>
              <!-- Address -->
              <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-rename</v-icon>
                <p>{{ user.firstName }} {{ user.lastName }}</p>
              </button>
              <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-calendar-range</v-icon>
                <p>{{ user.birthDate }}</p>
              </button>
              <!-- Cuisine -->
              <button class="flex gap-2 text-gray-500 hover:text-blue-500 text-sm">
                <v-icon>mdi-email-outline</v-icon>
                <p>{{ user.email }}</p>
              </button>
              <!-- Book Appointment Button -->
              <div class="flex justify-around mt-4">
                    <button
                    v-if="!user.banned"
                    :disabled="loading"
                    @click="banUser(user.id)"
                    class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-700"
                    >
                    Ban
                    </button>
                    <button
                    v-if="user.banned"
                    :disabled="loading"
                    @click="unbanUser(user.id)"
                    class="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-700"
                    >
                    Unban
                    </button>
            </div>
            </div>
          </div>
        </div>
  
        <div v-else class="text-center text-gray-500">
          No users.
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
  import { useUserStore } from '@/stores/userStore'

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

  const authStore = useAuthStore();
  const userStore = useUserStore();
  
  const currentPage = ref(1);
  const loading = ref(false);
  const error = ref('');

  const fetchUsers = async() => {
     await userStore.fetchUsers(currentPage.value - 1);
  }

  const onPageClick = async (page: number) => {
    if (currentPage.value === page) {
        return;
    }

    currentPage.value = page;
    await fetchUsers();
  }

const banUser = async (userId: number) => {
  if (authStore.uuid === userId) {
    return;
  }

  loading.value = true;
  try {
    await userStore.banUser(userId);
    await fetchUsers();
  } catch (err) {
    error.value = 'Failed to ban user';
  } finally {
    loading.value = false;
  }
};

const unbanUser = async (userId: number) => {
  loading.value = true;
  try {
    await userStore.unbanUser(userId);
    await fetchUsers();
  } catch (err) {
    error.value = 'Failed to unban user';
  } finally {
    loading.value = false;
  }
};
  
  onMounted(() => {
    fetchUsers();
  });
  </script>
  