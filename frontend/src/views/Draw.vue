<template>
    <div class="flex items-center justify-center gap-5 py-10 flex-col" v-if="loading">
        <!-- Loader za slucaj da se ucitava slika i dalje -->
        <pulse-loader color="#2596be" size="12px"></pulse-loader>
    </div>

    <!-- Draw panel sa svim tool-ovima i sa displejem -->
    <DrawPanel v-if="!loading" :grid="transpose(pictureData)" :name="pictureName" :uuid="pictureId" />
</template>

<script setup lang = "ts">
import { ref, watch, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { usePictureStore } from '@/stores/pictureStore'
import Picture from '@/types/picture'
import DrawPanel from '@/components/DrawPanel.vue';

const route = useRoute()

const loading = ref(false)
const picture = ref<Picture | null>(null)
const pictureStore = usePictureStore()

const pictureData = computed(() => {
    if (picture.value) {
        return picture.value.picture_data;
    }

    const defaultSize = 16;
    const grid: string[][] = []

    // Inicijalizujemo grid sa belom kojom koja je default
    for (let x = 0; x < defaultSize; x++) {
        const row: string[] = [];
        for (let y = 0; y < defaultSize; y++) {
            row.push('#FFFFFF');
        }

        grid.push(row);
    }

    return grid;
})

// Pratimo promene u route-u, i pozivamo fetchPicture opet
// Kako bi se promene updejtovale u DrawPanel komponenti
watch(
    () => route.params.id,
    async () => {
        picture.value = null;
        await fetchPicture();
    }
)

// Vraca transponovanu matricu od prvobitne
function transpose(matrix: string[][]) {
  if (!matrix || !matrix[0]) {
     return matrix;
  }

  return matrix[0].map((_, c) => matrix.map((_, r) => matrix[r][c]));
}

const pictureName = computed(() => picture.value?.name || '')
const pictureId = computed(() => route.params.id as string || '')

const fetchPicture = async () => {
    if (!route.params.id) {
        return;
    }

    loading.value = true;
    const fetchedPicture = await pictureStore.fetchPictureById(route.params.id)

    if (!fetchedPicture) {
        loading.value = false;
        return;
    }

    picture.value = fetchedPicture;
    loading.value = false;
}

onMounted(fetchPicture)
</script>
