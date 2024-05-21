<script setup>
import { inject } from 'vue'
const { selectedHouse } = inject('res')
const { close } = inject('service')
</script>

<template>
  <div class="header-section mb-4">
    <div>
      <div class="flex items-center mb-2">
        <button class="px-2 py-1 bg-gray-300 text-blue-600 rounded">대표</button>
        <h1 class="ml-2 text-xl font-semibold">{{ selectedHouse.road_address }}</h1>
      </div>
      <p class="text-sm text-gray-600">{{ selectedHouse.jibun_address }}</p>
    </div>
    <button class="text-gray-500 hover:text-gray-700" @click="close">
      <svg
        class="w-6 h-6"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M6 18L18 6M6 6l12 12"
        ></path>
      </svg>
    </button>
  </div>
</template>

<style scoped>
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
</style>
