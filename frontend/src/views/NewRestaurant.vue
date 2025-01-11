<template>
    <div class="flex items-center justify-center bg-white dark:bg-gray-900">
      <div class="container mx-auto py-10">
        <div v-if="!authStore.isManager" class="text-center">
            <h1 class="my-3 text-3xl font-semibold text-gray-700 dark:text-gray-200">Error</h1>
            <p class="text-gray-500 dark:text-gray-400">Failed to verify you as a manager</p>
        </div>
        <div v-else class="max-w-md mx-auto my-10">
            <div class="text-center">
                <h1 class="my-3 text-3xl font-semibold text-gray-700 dark:text-gray-200">Create New Restaurant</h1>
                <p class="text-gray-500 dark:text-gray-400">Fill in the details for your new restaurant</p>
            </div>
  
          <div class="m-7">
            <form @submit.prevent="onSubmit">
              <!-- Restaurant Name -->
              <div class="mb-6">
                <label
                  for="name"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >Restaurant Name</label
                >
                <input
                  v-model="restaurant.name"
                  type="text"
                  id="name"
                  placeholder="Enter restaurant name"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': nameError }"
                  @input="validateName"
                  required
                />
                <span v-if="nameError" class="text-sm text-red-500">{{ nameError }}</span>
              </div>

              <!-- Address -->
              <div class="mb-6">
                <label
                  for="address"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >Address</label
                >
                <input
                  v-model="restaurant.address"
                  type="text"
                  id="address"
                  placeholder="Enter restaurant address"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': addressError }"
                  @input="validateAddress"
                  required
                />
                <span v-if="addressError" class="text-sm text-red-500">{{ addressError }}</span>
              </div>

              <!-- Description -->
              <div class="mb-6">
                <label
                  for="description"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >Description</label
                >
                <textarea
                  v-model="restaurant.description"
                  id="description"
                  placeholder="Enter restaurant description"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': descriptionError }"
                  @input="validateDescription"
                  required
                ></textarea>
                <span v-if="descriptionError" class="text-sm text-red-500">{{ descriptionError }}</span>
              </div>

              <!-- Open Time -->
              <div class="mb-6">
                <label
                  for="startTime"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >Start Time</label
                >
                <input
                  v-model="restaurant.startTime"
                  type="time"
                  id="startTime"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': startTimeError }"
                  @input="validateStartTime"
                  required
                />
                <span v-if="startTimeError" class="text-sm text-red-500">{{ startTimeError }}</span>
              </div>

              <div class="mb-6">
                <label
                  for="endTime"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >End Time</label
                >
                <input
                  v-model="restaurant.endTime"
                  type="time"
                  id="endTime"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': endTimeError }"
                  @input="validateEndTime"
                  required
                />
                <span v-if="endTimeError" class="text-sm text-red-500">{{ endTimeError }}</span>
              </div>

              <!-- Cuisine Type -->
              <div class="mb-6">
                <label
                  for="type"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >Cuisine Type</label
                >
                <select
                  v-model="restaurant.type"
                  id="type"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': typeError }"
                  required
                >
                  <option disabled value="">Select a cuisine type</option>
                  <option>Chinese</option>
                  <option>Italian</option>
                  <option>Mexican</option>
                  <option>Indian</option>
                  <option>French</option>
                </select>
                <span v-if="typeError" class="text-sm text-red-500">{{ typeError }}</span>
              </div>

              <!-- Discount and Free Item -->
              <div class="mb-6">
                <label
                  for="discountAfterXReservations"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >After how many reservations should a user get a discount?</label
                >
                <input
                  v-model="restaurant.discountAfterXReservations"
                  type="number"
                  id="discountAfterXReservations"
                  placeholder="e.g., 10"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': discountError }"
                  @input="validateDiscount"
                  required
                />
                <span v-if="discountError" class="text-sm text-red-500">{{ discountError }}</span>
              </div>

              <div class="mb-6">
                <label
                  for="freeItemEachXReservations"
                  class="block mb-2 text-sm text-gray-600 dark:text-gray-400"
                  >How many reservations a user needs to get a free item?</label
                >
                <input
                  v-model="restaurant.freeItemEachXReservations"
                  type="number"
                  id="freeItemEachXReservations"
                  placeholder="e.g., 20"
                  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500"
                  :class="{ 'border-red-500': freeItemError }"
                  @input="validateFreeItem"
                  required
                />
                <span v-if="freeItemError" class="text-sm text-red-500">{{ freeItemError }}</span>
              </div>

              <!-- Submit Button -->
              <div class="mb-6">
                <button
                  type="submit"
                  class="w-full px-3 py-4 text-white bg-indigo-500 rounded-md focus:bg-indigo-600 focus:outline-none"
                >
                  Create Restaurant
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { useAuthStore } from '@/stores/auth';
  import { useRestaurantStore } from '@/stores/restaurantStore';
  import Restaurant from '@/types/restaurant';
import router from '@/router';

  const restaurant = ref({
    name: '',
    address: '',
    description: '',
    startTime: '09:00',
    endTime: '17:00',
    type: '',
    discountAfterXReservations: 0,
    freeItemEachXReservations: 0
  });

  const nameError = ref<string | null>(null);
  const addressError = ref<string | null>(null);
  const descriptionError = ref<string | null>(null);
  const startTimeError = ref<string | null>(null);
  const endTimeError = ref<string | null>(null);
  const typeError = ref<string | null>(null);
  const discountError = ref<string | null>(null);
  const freeItemError = ref<string | null>(null);

  const authStore = useAuthStore();
  const restaurantStore = useRestaurantStore()

  const validateName = () => {
    if (!restaurant.value.name) {
      nameError.value = 'Name is required';
    } else {
      nameError.value = null;
    }
  };

  const validateAddress = () => {
    if (!restaurant.value.address) {
      addressError.value = 'Address is required';
    } else {
      addressError.value = null;
    }
  };

  const validateDescription = () => {
    if (!restaurant.value.description) {
      descriptionError.value = 'Description is required';
    } else {
      descriptionError.value = null;
    }
  };

  const validateStartTime = () => {
    if (!restaurant.value.startTime) {
      startTimeError.value = 'Start time is required';
    } else {
      startTimeError.value = null;
    }
  };

  const validateEndTime = () => {
    if (!restaurant.value.endTime) {
      endTimeError.value = 'End time is required';
    } else {
      endTimeError.value = null;
    }
  };

  const validateDiscount = () => {
    if (restaurant.value.discountAfterXReservations <= 0) {
      discountError.value = 'Discount must be a positive number';
    } else {
      discountError.value = null;
    }
  };

  const validateFreeItem = () => {
    if (restaurant.value.freeItemEachXReservations <= 0) {
      freeItemError.value = 'Free item count must be a positive number';
    } else {
      freeItemError.value = null;
    }
  };

  const onSubmit = async () => {
    validateName();
    validateAddress();
    validateDescription();
    validateStartTime();
    validateEndTime();
    validateDiscount();
    validateFreeItem();

    if (!nameError.value && !addressError.value && !descriptionError.value && !startTimeError.value && !endTimeError.value && !discountError.value && !freeItemError.value) {
        if (!authStore.uuid) {
            return;
        }

        const restaurantEntity : Partial<Restaurant> = {
            name: restaurant.value.name,
            address: restaurant.value.address,
            description: restaurant.value.description,
            openTime: `${restaurant.value.startTime}-${restaurant.value.endTime}`,
            type: restaurant.value.type,
            discountAfterXReservations: restaurant.value.discountAfterXReservations,
            freeItemEachXReservations: restaurant.value.freeItemEachXReservations,
            managerId: authStore.uuid
        }

        const res = await restaurantStore.createRestaurant(restaurantEntity)
        if (res) {
            router.push({ name: 'restaurants' });
        }
    }
  };
</script>

  