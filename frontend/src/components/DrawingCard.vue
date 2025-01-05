<template>
 <div 
    v-if="picture" 
    @click="openPicture"
    class="outline outline-indigo-500 rounded-lg border bg-card text-card-foreground min-w-[300px] transition basis-1/5 hover:shadow-xl cursor-pointer"
    >
    <!-- Ovde je grid, tj slika koja se prikazuje u umanjenoj velicini na kartici -->
    <div class="flex flex-col gap-y-1.5 p-6">
      <div class="aspect-square grid" :style="gridStyle">
        <div
            v-for="(pixel, index) in pictureGrid"
            :key="index"
            :style="{ backgroundColor: pixel }"
            class="pixel">
        </div>
      </div>

      <!-- Opcije za editovanje i brisanje slike ukoliko je trenutni korisnik ujedno i author-->
      <div class="flex gap-2 justify-center items-center pt-4 text-xl font-semibold text-center">
        <p>{{ picture.name }}</p>
        <Dialog v-if="isAuthor()" title="Edit Drawing Name" description="After you've finished typing, please click the confirm button." confirm-label="Save" @confirm="saveDrawingName">
          <template #trigger>
            <button v-if="isAuthor()" @click="handlePictureNameEdit" class="hover:text-green-500">
              <v-icon size="20px">mdi-pencil-outline</v-icon>
            </button>
          </template>
          <template #content>
            <input
              id="name"
              v-model="pictureNameField"
              placeholder="Enter a new name"
              class="text-gray shadow-gray focus:shadow-black inline-flex h-[35px] w-full flex-1 items-center justify-center rounded-[4px] px-[10px] text-[15px] leading-none shadow-[0_0_0_1px] outline-none focus:shadow-[0_0_0_2px]"
            >
          </template>
        </Dialog>

        <Dialog v-if="isAuthor()" title="Are you sure you want to delete this picture?", description="This item will be deleted immediately. You cannot undo this action." @confirm="$emit('delete')">
          <template #trigger>
            <button v-if="isAuthor()" class="hover:text-red-500">
              <v-icon size="20px">mdi-delete-outline</v-icon>
            </button>
          </template>
        </Dialog>
      </div>

      <!-- Dugme koje kad se klikne preusmerava na stranicu autora -->
      <button 
        class="flex gap-2 justify-center items-center text-gray-500 hover:text-blue-500 text-sm"
        @click.stop="pushToAuthorPage"
        >
        <v-icon>mdi-account-circle-outline</v-icon>
        <p>{{ picture.author.username }}</p>
      </button> 

      <!-- Vreme od kada je kreirana slika -->
      <button class="flex gap-2 justify-center items-center text-gray-500 text-xs" >
        <v-icon>mdi-calendar-blank-outline</v-icon>
        <p>{{ dayjs(picture.created_at).fromNow()}}</p>
      </button> 

      <!-- Vreme od kada je poslednji put azurirana slika -->
      <button class="flex gap-2 justify-center items-center text-gray-500 text-xs">
        <v-icon>mdi-calendar-edit-outline</v-icon>
        <p>{{ dayjs(picture.updated_at).fromNow()}}</p>
      </button> 
    </div>
  </div>
</template>
  
<script setup lang="ts">
import { ref, onMounted, computed, defineEmits } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { usePictureStore } from '@/stores/pictureStore'
import Picture from '@/types/picture'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import Dialog from '@/components/Dialog.vue'

// Propovi koji su prosledjeni iz Gallery komponente
const props = defineProps({
  uuid: {
    type: String,
    required: true
  }
})

const emit = defineEmits(["delete"]);
const router = useRouter()

dayjs.extend(relativeTime);

const pictureStore = usePictureStore()
const authStore = useAuthStore()

const picture = ref<Picture | null>(null)

// Computed property for picture grid
const pictureGrid = computed(() => {
  return transpose(picture.value?.picture_data!!).flat() || []  // Fallback to an empty array
})

// Vraca transponovanu matricu u odnosu na prvobitnu
function transpose(matrix: string[][]) {
  return matrix[0].map((col, c) => matrix.map((row, r) => matrix[r][c]));
}

// Izracunata velicina grid-a
const gridSize = computed(() => {
  return Math.sqrt(pictureGrid.value.length) || 0
})

// Stil za grid
const gridStyle = computed(() => ({
  'grid-template-columns': `repeat(${gridSize.value}, 1fr)`,
  'grid-template-rows': `repeat(${gridSize.value}, 1fr)`
}))

const pictureNameField = ref('');

// Posto koristimo v-model, moramo svaki put da azuriramo picture name field
// Na pocetnu vrednost, tj na vrednost imena slike
const handlePictureNameEdit = () => {
  pictureNameField.value = picture.value?.name || ''
}

// Fetchuje podatke slike
onMounted(async () => {
   picture.value = await pictureStore.fetchPictureById(props.uuid)
})

// Redirektuje na stranicu autora
const pushToAuthorPage = () => {
  router.push({ path: '/gallery/1', query: { author: picture.value?.author.user_id }})
}

// Proverava da li je trenutni korisnik
// Autor prosledjene slike
const isAuthor = () => {
    return authStore.uuid !== null && authStore.uuid === picture.value?.author.user_id;
}

// Cuva novo ime slike
const saveDrawingName = async () => {
  await pictureStore.savePictureName(picture.value?.picture_id!, pictureNameField.value)
}

// Redirektuje na sliku
const openPicture = () => {
  router.push({ name: 'draw', params: { id: picture.value?.picture_id }})
}
</script>
  
<style scoped>
.pixel {
  outline: 1px solid gray;
}
</style>
  