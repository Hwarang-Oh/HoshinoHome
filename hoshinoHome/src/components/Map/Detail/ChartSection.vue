<template>
  <div class="content-section space-y-4">
    <div class="h-40 bg-gray-100 flex items-center justify-center">
      <DealTypeChart v-if="detailDealList.length" :data="dealTypeCounts" />
    </div>
    <div class="toggle-buttons">
      <button @click="setChartType('dealAmount')" :class="{ active: chartType === 'dealAmount' }">
        Deal Amount
      </button>
      <button
        @click="setChartType('depositAmount')"
        :class="{ active: chartType === 'depositAmount' }"
      >
        Deposit Amount
      </button>
      <button
        @click="setChartType('monthlyAmount')"
        :class="{ active: chartType === 'monthlyAmount' }"
      >
        Deposit + Monthly Amount
      </button>
    </div>
    <div class="h-40 bg-gray-100 flex items-center justify-center">
      <DealAmountChart :dealData="detailDealList" :chartType="chartType" />
    </div>
  </div>
</template>

<script setup>
import { ref, inject, computed } from 'vue'
import DealAmountChart from '../Chart/DealAmountChart.vue'
import DealTypeChart from '../Chart/DealTypeChart.vue'

const { detailDealList } = inject('res')

const chartType = ref('dealAmount')

function setChartType(type) {
  chartType.value = type
}
const dealTypeCounts = computed(() => {
  const counts = { 1: 0, 2: 0, 3: 0 }
  detailDealList.value.forEach((deal) => {
    if (counts[deal.deal_type] !== undefined) counts[deal.deal_type]++
  })
  return {
    labels: Object.keys(counts).map((type) => `Type ${type}`),
    counts: Object.values(counts)
  }
})
</script>

<style scoped>
.content-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.toggle-buttons {
  display: flex;
  justify-content: space-around;
  margin-bottom: 10px;
}

.toggle-buttons button {
  padding: 10px;
  border: none;
  cursor: pointer;
  background-color: #ddd;
  border-radius: 5px;
}

.toggle-buttons button.active {
  background-color: #42a5f5;
  color: white;
}
</style>
