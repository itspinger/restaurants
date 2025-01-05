<template>
  <div v-if="dialogOpen" class="fixed inset-0 z-50 bg-black/80 data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0"></div>
  <DialogRoot v-model:open="dialogOpen">
    <DialogTrigger as-child>
        <div @click.stop>
            <slot name="trigger"></slot>
        </div>
    </DialogTrigger>
    <DialogPortal>
      <DialogContent :trap-focus="true"
        v-on:interact-outside="handleOutsideInteraction"
        class="outline outline-indigo-500 data-[state=open]:animate-contentShow fixed top-[50%] left-[50%] max-h-[85vh] w-[90vw] max-w-[450px] translate-x-[-50%] translate-y-[-50%] rounded-[6px] bg-white p-[25px] shadow-[hsl(206_22%_7%_/_35%)_0px_10px_38px_-10px,_hsl(206_22%_7%_/_20%)_0px_10px_20px_-15px] focus:outline-none z-[100]"
      >
        <DialogTitle class="text-mauve12 m-0 text-[17px] font-semibold">
          {{ title }}
        </DialogTitle>
        <DialogDescription v-if="description" class="text-mauve11 mt-[10px] mb-5 text-[15px] leading-normal">
          {{ description }}
        </DialogDescription>
        <div class="mb-[15px]">
          <slot name="content"></slot>
        </div>
        <div class="mt-[25px] flex justify-end gap-x-2">
          <DialogClose as-child>
            <button
              @click="$emit('close'); dialogOpen=false"
              class="text-sm bg-indigo-500 px-4 py-2 rounded-md hover:outline-indigo-500 text-white hover:bg-indigo-600"
            >
              Cancel
            </button>
          </DialogClose>
          <DialogClose as-child>
            <button
              @click="$emit('confirm')"
              class="text-sm bg-indigo-500 px-4 py-2 rounded-md hover:outline-indigo-500 text-white hover:bg-indigo-600"
            >
              {{ confirmLabel }}
            </button>
          </DialogClose>
        </div>
        <DialogClose
          @click="$emit('close'); dialogOpen=false"
          class="text-indigo-500 hover:bg-indigo-100 focus:shadow-indigo-500 absolute top-[10px] right-[10px] inline-flex h-[25px] w-[25px] appearance-none items-center justify-center rounded-full focus:shadow-[0_0_0_2px] focus:outline-none"
          aria-label="Close"
        >
            <v-icon style="font-size: 18px">mdi-close</v-icon>
        </DialogClose>
      </DialogContent>
    </DialogPortal>
  </DialogRoot>
</template>

<script setup lang="ts">
import { defineEmits, ref } from "vue";

const dialogOpen = ref(false)

const props = defineProps({
  title: String,
  description: String,
  confirmLabel: { type: String, default: "Confirm" },
});

import {
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogPortal,
  DialogRoot,
  DialogTitle,
  DialogTrigger,
} from 'radix-vue'

const handleOutsideInteraction = (e: Event) => {
    e.preventDefault();
}

const emit = defineEmits(["confirm", "close"]);
</script>
