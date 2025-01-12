<template>
    <div v-if="authStore.isAdmin" class="w-full max-w-2xl mx-auto mt-8 p-6 bg-white rounded-lg shadow">
      <h2 class="text-2xl font-bold text-gray-800 mb-4">Edit Notification Type</h2>
      <div v-if="error" class="text-red-600 mb-4">{{ error }}</div>
      <div v-if="isLoading" class="text-gray-500">Loading...</div>
  
      <div v-else>
        <label class="block text-gray-700 font-medium mb-2">Name</label>
        <input
          v-model="notificationType.name"
          type="text"
          class="w-full mb-4 p-2 border rounded-lg"
        />
  
        <label class="block text-gray-700 font-medium mb-2">Message</label>
        <textarea
          v-model="notificationType.text"
          class="w-full mb-4 p-2 border rounded-lg"
          rows="4"
        ></textarea>
  
        <button
          @click="updateNotificationType"
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Update
        </button>
      </div>
    </div>
    <div v-else>
        <h2 class="text-2xl font-bold text-gray-800 mb-4">You're not an admin, how did you get here?</h2>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { requestFromApi } from '@/utils/api';
  import { useAuthStore } from '@/stores/auth'
  
  const route = useRoute();
  const router = useRouter();
  const notificationTypeId = route.params.id as string;
  const notificationType = ref({ name: '', text: '' });
  const isLoading = ref(false);
  const authStore = useAuthStore();
  const error = ref<string | null>(null);
  
  onMounted(async () => {
    try {
      console.log(`notification/api/notification/types/${notificationTypeId}`)
      isLoading.value = true;
      const response = await requestFromApi<{ name: string, text: string }>('get', `notification/api/notification/types/${notificationTypeId}`);
      if (response) {
        notificationType.value = response;
      }
    } catch (err: any) {
      error.value = 'Failed to load notification type.';
    } finally {
      isLoading.value = false;
    }
  });
  
  const updateNotificationType = async () => {
    try {
      await requestFromApi('patch', `notification/api/notification/type/${notificationTypeId}`, notificationType.value);
      router.push('/notifications');
    } catch (err: any) {
      error.value = 'Failed to update notification type.';
    }
  };
  </script>
  