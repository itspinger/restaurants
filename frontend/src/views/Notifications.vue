<template>
    <div class="max-w-4xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <!-- Loading and Error States -->
      <div v-if="error" class="text-red-600 bg-red-100 border-l-4 border-red-500 p-4 mb-4">
        {{ error }}
      </div>

      <h2 class="text-3xl font-extrabold text-gray-900 mb-6 text-center">Notifications</h2>
      <div v-if="isLoading" class="text-center text-gray-500">Loading notifications...</div>
  
      <!-- Filters -->
      <div v-if="!isLoading" class="mb-6 flex items-center space-x-4">
        <div class="w-1/4 px-4">
          <label for="notificationType" class="block text-sm font-medium text-gray-700">Notification Type</label>
          <select v-model="filters.type" id="notificationType" class="mt-1 block w-full border-gray-300 shadow-sm sm:text-sm">
            <option value="">All</option>
            <option v-for="type in notificationTypes" :key="type.id" :value="type.id">
              {{ type.name }}
            </option>
          </select>
        </div>
  
        <div class="w-1/4 px-4">
          <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
          <input type="email" v-model="filters.email" id="email" class="mt-1 block w-full border-gray-300 shadow-sm sm:text-sm" placeholder="Email" />
        </div>
  
        <div class="w-1/4 px-4">
          <label for="startDate" class="block text-sm font-medium text-gray-700">Start Date</label>
          <input type="date" v-model="filters.startDate" id="startDate" class="mt-1 block w-full border-gray-300 shadow-sm sm:text-sm" />
        </div>
  
        <div class="w-1/4 px-4">
          <label for="endDate" class="block text-sm font-medium text-gray-700">End Date</label>
          <input type="date" v-model="filters.endDate" id="endDate" class="mt-1 block w-full border-gray-300 shadow-sm sm:text-sm" />
        </div>

        <button @click="applyFilters" class="text-xs bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg transition-all">Filter</button>
      </div>
  
      <!-- Notifications List -->
      <ul v-if="!isLoading && notifications && notifications.length > 0" class="space-y-6">
        <li
          v-for="notification in notifications"
          :key="notification.id"
          class="p-6 bg-white border border-gray-200 rounded-lg shadow hover:shadow-md transition-shadow duration-300"
        >
          <div class="flex items-center justify-between mb-2">
            <h3 class="text-xl font-semibold text-blue-600">{{ notification.type.name }}</h3>
            <span class="text-xs text-gray-500">{{ formatTimestamp(notification.timestamp) }}</span>
          </div>
  
          <!-- Uncomment if we really need this -->
          <!-- <p class="text-gray-700 mb-4 break-words">{{ notification.text }}</p>  -->
  
          <p class="text-sm text-gray-600">
            <span class="font-medium">Email:</span> {{ notification.email }}
          </p>
        </li>
      </ul>
  
      <!-- Empty State -->
      <p v-else class="text-center text-gray-500">No notifications to show.</p>
    </div>

    <Pagination v-slot="{ page }" :total="totalElements" :sibling-count="1" show-edges :default-page="currentPage" :items-per-page="pageSize">
      <PaginationList v-slot="{ items }" class="flex items-center justify-center gap-1">
        <PaginationFirst @click="onPageClick(1)" />
        <PaginationPrev @click="onPageClick(currentPage - 1)" />

        <template v-for="(item, index) in items">
          <PaginationListItem v-if="item.type === 'page'" :key="index" :value="item.value" as-child>
            <Button class="w-10 h-10 p-0" :variant="item.value === currentPage ? 'default' : 'outline'" @click="onPageClick(item.value)">
              {{ item.value }}
            </Button>
          </PaginationListItem>
          <PaginationEllipsis v-else :key="item.type" :index="index" />
        </template>

        <PaginationNext @click="onPageClick(currentPage + 1)" />
      </PaginationList>
    </Pagination>
</template>


<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useAuthStore } from "@/stores/auth";
import { requestFromApi } from "@/utils/api";
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

interface NotificationTypeDto {
  id: number;
  name: string;
  text: string;
}

interface Notification {
  id: number;
  text: string;
  email: string;
  type: NotificationTypeDto;
  timestamp: string;
}

interface Filters {
  type?: number;
  email?: string;
  startDate?: string;
  endDate?: string;
}

const authStore = useAuthStore();
const userId = computed(() => authStore.uuid); // Replace with actual store field
const notifications = ref<Notification[]>([]);
const isLoading = ref(false);
const error = ref<string | null>(null);
const filters = ref<Filters>({});
const notificationTypes = ref<NotificationTypeDto[]>([]);
const currentPage = ref(1);
const pageSize = ref(20);  // Adjust this as needed
const totalElements = ref(0);  // Use from API response

const fetchNotifications = async (filters: Filters) => {
  isLoading.value = true;
  error.value = null;
  notifications.value = [];

  try {
    let searchParams = new URLSearchParams();
    searchParams.append("page", (currentPage.value - 1).toString());

    if (filters.type) searchParams.append("type", filters.type.toString());
    if (filters.email) searchParams.append("email", filters.email);
    if (filters.startDate) searchParams.append("startDate", filters.startDate);
    if (filters.endDate) searchParams.append("endDate", filters.endDate);

    searchParams.append('sort', "timestamp,desc");

    console.log(searchParams.toString())

    const fetchFrom = authStore.isAdmin ? '' : `/${userId.value}`; 
    let queryParams = `notification/api/notification${fetchFrom}?${searchParams.toString()}`;

    console.log(queryParams);

    const response = await requestFromApi<{ content: Notification[], totalElements: number } >("get", queryParams);

    if (!response) {
      error.value = "Failed to load notifications.";
      return;
    }

    console.log(response.totalElements)
    notifications.value = response.content;
    totalElements.value = response.totalElements;
  } catch (err: any) {
    error.value = err.message || "An error occurred while fetching notifications.";
  } finally {
    isLoading.value = false;
  }
};

const applyFilters = () => {
  console.log('called');
  fetchNotifications(filters.value);
};

const formatTimestamp = (timestamp: string): string => {
  const date = new Date(timestamp);
  return date.toLocaleString();
};

onMounted(() => {
  fetchNotifications({});
});

onMounted(() => {
  // We want to fetch all available notification types from the rest api
  // So we can display to the user for filtering...
  const fetchNotificationTypes = async () => {
    try {
      const response = await requestFromApi<NotificationTypeDto[]>(
        'get',
        'notification/api/notification/types'
      );

      if (response) {
        notificationTypes.value = response;
      }
    } catch (err: any) {
      error.value = 'Failed to load notification types';
      console.error("Failed to load notification types:", err.message);
    }
  };

  fetchNotificationTypes();
});

const onPageClick = async (page: number) => {
    if (currentPage.value === page) {
        return;
    }

    currentPage.value = page;
    fetchNotifications(filters.value);
}
</script>