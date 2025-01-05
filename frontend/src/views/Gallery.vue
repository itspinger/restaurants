<template>
  <div class= "flex items-center justify-center gap-5 py-10 flex-col">
    <!-- Na vrhu imamo paginaciju gde prikazujemo po 8 slika -->
    <Pagination v-slot="{ page }" :total="pictureStore.total" :sibling-count="1" show-edges :default-page="currentPage" :items-per-page="8">
        <PaginationList v-slot="{ items }" class="flex items-center justify-center gap-1">
        <PaginationFirst @click="onPageClick(1)"/>
        <PaginationPrev @click="onPageClick(currentPage - 1)"/>

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

    <!-- U slucaju da se jos nismo ucitali sve slike koristimo pulse loader kao indikator da se i dalje ucitava-->
    <pulse-loader :loading="loading" color="#2596be" size="12px"></pulse-loader>
    <h2 v-if="!loading && pictureStore.pictures.length === 0" class="text-lg text-gray-500">There are no drawings</h2> <!-- Ako nismo nasli nijednu sliku vracamo gresku -->

    <!-- Ovde prikazujemo te kartice za slike -->
    <div class="flex flex-wrap gap-10 justify-center mb-20" v-if="!loading" style="position:relative">
      <DrawingCard v-for="picture in pictureStore.pictures" :key="picture.picture_id" :uuid="picture.picture_id" @delete="onItemDeleted(picture)" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePictureStore } from '@/stores/pictureStore'
import { Button } from '@/components/ui/button'
import Picture from '@/types/picture'
import DrawingCard from '@/components/DrawingCard.vue'

import {
  Pagination,
  PaginationEllipsis,
  PaginationFirst,
  PaginationList,
  PaginationListItem,
  PaginationNext,
  PaginationPrev,
} from '@/components/ui/pagination'

const route = useRoute()
const router = useRouter()

const pictureStore = usePictureStore();
const currentPage = ref(1); // Trenutna strnaica
const loading = ref(true) // Da li i dalje ucitavamo slike

// Funkcija koja fetchuje slike u zavisnosit od stranice koja je proslednjena kao param 
// I author id koji je prosledjen kao query
const fetchPictures = async () => {
  const page = Number(route.params.page) || 1 // Stranica koja je prosledjena
  const authorId = route.query.author as string | null // Author id koji je prosledjen
  currentPage.value = page
  loading.value = true
  await pictureStore.fetchPictures(page, authorId) // Funkcija iz store-a koja fetchuje slike
  loading.value = false
}

// Funkcija koja ce biti pozvana nakon sto DrawingCard (dete) pozove event delete
// Odavde dobijamo signal da je korisnik potvrdio da hoce da izbrise sliku
// I onda brisemo iz store-a i zovemo delete u api-u
const onItemDeleted = async (picture: Picture) => {
  const deleted = await pictureStore.deletePicture(picture.picture_id);
  if (!deleted) {
    return;
  }

  await fetchPictures()
}

onMounted(fetchPictures)

// Detektujemo bilo kakve promene u route-u
watch(
  () => [route.params.page, route.query.author],
  async () => {
    await fetchPictures()
  }
)

// Metoda koja handluje click na stranice iz paginacije
// Ceo query ce da prekopira, tj autora, i redirektovace na novi broj
function onPageClick(page: number) {
  const query: Record<string, string> = {}
  
  if (route.query.author) {
    query.author = route.query.author as string
  }

  router.push({
    path: `/gallery/${page}`,
    query: query
  })
}
</script>
