<template>
  <Navbar />
  <RouterView />
  <!-- Za sve vrste notifikacija ovo je template -->
  <VNotifications />
</template>

<script setup lang="ts">
  import { RouterView } from 'vue-router'
  import { VNotifications } from '@f3ve/vue-notify';
  import { useAuthStore } from '@/stores/auth';
  import { onMounted } from 'vue';
  import Navbar from '@/components/Navbar.vue';

  // Koristimo local storage da bismo ucitali token (ako je korisnik ulogovan naravno)
  onMounted(async () => {
    useAuthStore().loadToken();

    // Takodje fetchujemo samog korisnika
    if (useAuthStore().isLoggedIn) {
      await useAuthStore().fetchSelfUser()
    }
  });
</script>