<template>
  <div class="transaction-info">
    <h2 class="section-title">거래 정보</h2>
    <div class="space-y-2">
      <div class="flex justify-between items-center">
        <span class="text-sm text-gray-700">거래일</span>
        <span class="text-sm text-gray-900">2023.01.01</span>
      </div>
      <div class="flex justify-between items-center">
        <span class="text-sm text-gray-700">거래 금액</span>
        <span class="text-sm text-gray-900">47억</span>
      </div>
      <div class="flex justify-between items-center">
        <span class="text-sm text-gray-700">면적</span>
        <span class="text-sm text-gray-900">75평</span>
      </div>
      <div class="flex justify-between items-center">
        <span class="text-sm text-gray-700">층수</span>
        <span class="text-sm text-gray-900">2층</span>
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
}

.section-title {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
}
</style>
