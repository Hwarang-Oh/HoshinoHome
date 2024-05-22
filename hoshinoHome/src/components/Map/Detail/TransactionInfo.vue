<template>
  <div class="transaction-info">
    <h2 class="section-title">거래 정보</h2>
    <div class="transaction-list space-y-2 overflow-y-auto">
      <div
        v-for="(deal, index) in detailDealList"
        :key="index"
        class="flex justify-between items-center p-2 border-b border-gray-200"
      >
        <span class="text-sm text-gray-700">{{ deal.date }}</span>
        <span class="text-sm text-gray-900">{{ deal.type }}</span>
        <span class="text-sm text-gray-900">{{ deal.price }}</span>
        <span class="text-sm text-gray-900">{{ deal.floor }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject } from 'vue'
const detailDealList = inject('detailDealList')
</script>

<style scoped>
.transaction-info {
  margin-bottom: 1rem;
  max-height: 400px; /* Set a max-height for scrollable area */
  overflow-y: auto; /* Enable vertical scrolling */
}

.section-title {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.transaction-list {
  max-height: 400px;
  overflow-y: auto;
}
</style>
