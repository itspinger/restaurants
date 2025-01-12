<template>
    <div class="w-full py-8 px-4">
      <!-- Loading and Error States -->
      <div v-if="error" class="text-red-600 bg-red-100 border-l-4 border-red-500 p-4 mb-4">
        {{ error }}
      </div>

      <h2 class="text-3xl font-extrabold text-gray-900 mb-6 text-center">Notifications</h2>
      <div v-if="isLoading" class="text-center text-gray-500">Loading notifications...</div>

      <div class="flex flex-wrap gap-4 justify-center items-center">
        <!-- Notification Type Filter -->
        <div>
          <label for="notificationType" class="text-gray-500">Notification Type: </label>
          <select
            id="notificationType"
            v-model="filters.type"
            class="border border-gray-300 rounded-lg p-2"
          >
            <option value="">All</option>
            <option
              v-for="type in notificationTypes"
              :key="type.id"
              :value="type.id"
            >
              {{ type.category }}
            </option>
          </select>
        </div>

        <!-- Email Filter -->
        <div>
          <label for="email" class="text-gray-500">Email: </label>
          <input
            id="email"
            type="email"
            v-model="filters.email"
            class="border border-gray-300 rounded-lg p-2"
            placeholder="Email"
          />
        </div>

        <!-- Start Date Filter -->
        <div>
          <label for="startDate" class="text-gray-500">Start Date: </label>
          <input
            id="startDate"
            type="date"
            v-model="filters.startDate"
            class="border border-gray-300 rounded-lg p-2"
          />
        </div>

        <!-- End Date Filter -->
        <div>
          <label for="endDate" class="text-gray-500">End Date: </label>
          <input
            id="endDate"
            type="date"
            v-model="filters.endDate"
            class="border border-gray-300 rounded-lg p-2"
          />
        </div>

        <!-- Apply Filters Button -->
        <button
          @click="applyFilters"
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Apply Filters
        </button>
      </div>
    </div>
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
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

    <Pagination v-slot="{ page }" :total="totalElements" :sibling-count="1" show-edges :default-page="currentPage" :items-per-page="pageSize" class="py-6">
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

    <div v-if="authStore.isAdmin" class="container mx-auto outline outline-2 outline-indigo-500 rounded-lg border bg-card hover:shadow-xl cursor-pointer">
      <h3 class="text-xl font-bold text-gray-700 mb-4 text-center">Manage Notification Types</h3>
      <div class="grid grid-cols-2 justify-between items-center gap-5 py-5 m-5">
        <div
          v-for="type in notificationTypes"
          :key="type.id"
          @click="navigateToEdit(type.id)"
          class="p-4 bg-white border rounded-lg shadow cursor-pointer hover:shadow-md transition-shadow duration-300"
        >
          <h4 class="text-lg font-semibold text-gray-800">{{ type.category }}</h4>
          <p class="text-sm text-gray-600">{{ type.name }}</p>
        </div>
      </div>
    </div>
</template>


<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useAuthStore } from "@/stores/auth";
import { requestFromApi } from "@/utils/api";
import { Button } from '@/components/ui/button'
import { useRouter } from "vue-router";

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
  category: string;
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

const router = useRouter();

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

const navigateToEdit = (typeId: number) => {
  router.push(`/notifications/edit/${typeId}`);
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