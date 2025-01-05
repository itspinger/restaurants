<template>
    <div class="w-full max-w-2xl p-2 mx-auto rounded-2xl">
      <div class="flex w-full flex-col mt-4 items-center justify-center text-center">
        <div class="text-sm font-bold tracking-wider text-indigo-600 uppercase">
            FAQ
        </div>
        <h2 class="max-w-2xl mt-3 mb-4 text-3xl font-bold leading-snug tracking-tight text-gray-800 lg:leading-tight lg:text-4xl dark:text-white">
            Frequently Asked Questions
        </h2>
      </div>

      <div v-for="item in faq" :key="item.question" class="mb-5">
        <Disclosure v-slot="{ open }">
          <DisclosureButton
            class="flex items-center justify-between w-full px-4 py-4 text-lg text-left text-gray-800 rounded-lg bg-gray-50 hover:bg-gray-100 focus:outline-none focus-visible:ring focus-visible:ring-indigo-100 focus-visible:ring-opacity-75 dark:bg-trueGray-800 dark:text-gray-200"
            >
            <span>{{ item.question }}</span>
            <ChevronRightIcon :class="{ 'transform rotate-180': open }" class="w-5 h-5 text-indigo-500" />
          </DisclosureButton>
          <DisclosurePanel class="px-4 pt-4 pb-2 text-gray-500 dark:text-gray-300">
            {{ item.answer }}
          </DisclosurePanel>
        </Disclosure>
      </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ChevronRightIcon } from '@heroicons/vue/24/solid'
import { Disclosure, DisclosureButton, DisclosurePanel} from '@headlessui/vue'

const faq = ref([
  {
    question: "Is this software completely free to use?",
    answer: "Yes, this template is completely free to use.",
  },
  {
    question: "Do you offer technical support?",
    answer: "No, we don't offer technical support for free downloads. Please purchase a support plan to get 6 months of support.",
  }
]);
</script>