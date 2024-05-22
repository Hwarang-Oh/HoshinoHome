<template>
  <div class="content-section space-y-4">
    <div
      v-if="detailDealList.length === 0"
      class="h-40 bg-white flex items-center justify-center shadow rounded"
    >
      <p>거래 내역이 없습니다.</p>
    </div>
    <div v-else>
      <div class="h-40 bg-white flex items-center justify-center shadow rounded">
        <DealTypeChart :data="dealTypeCounts" />
      </div>
      <div class="toggle-buttons">
        <button @click="setChartType('dealAmount')" :class="{ active: chartType === 'dealAmount' }">
          매매 계약
        </button>
        <button
          @click="setChartType('depositAmount')"
          :class="{ active: chartType === 'depositAmount' }"
        >
          전세 계약
        </button>
        <button
          @click="setChartType('monthlyAmount')"
          :class="{ active: chartType === 'monthlyAmount' }"
        >
          월세 계약
        </button>
      </div>
      <div
        v-if="chartType === 'dealAmount' && dealTypeCounts.counts[0] > 0"
        class="h-40 bg-white flex items-center justify-center shadow rounded"
      >
        <DealAmountChart :dealData="detailDealList" :chartType="chartType" />
      </div>
      <div
        v-else-if="chartType === 'depositAmount' && dealTypeCounts.counts[1] > 0"
        class="h-40 bg-white flex items-center justify-center shadow rounded"
      >
        <DealAmountChart :dealData="detailDealList" :chartType="chartType" />
      </div>
      <div
        v-else-if="chartType === 'monthlyAmount' && dealTypeCounts.counts[2] > 0"
        class="h-80 flex flex-col space-y-4"
      >
        <DealAmountChart :dealData="detailDealList" :chartType="chartType" />
      </div>
      <div v-else class="h-40 bg-white flex items-center justify-center shadow rounded">
        <p>해당 거래 유형의 내역이 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, inject, computed, reactive } from 'vue'
import DealAmountChart from '../Chart/DealAmountChart.vue'
import DealTypeChart from '../Chart/DealTypeChart.vue'

const detailDealList = inject('detailDealList')

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
    labels: ['매매', '전세', '월세'],
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
  padding: 6px 12px; /* 버튼 크기 조절 */
  border: none;
  cursor: pointer;
  background-color: #ddd;
  border-radius: 5px;
  font-size: 14px;
  transition:
    background-color 0.2s,
    color 0.2s; /* 배경색과 글자색 전환 효과 */
}

.toggle-buttons button.active {
  background-color: #42a5f5;
  color: white;
}

.h-40 {
  height: 10rem;
}

.h-80 {
  height: 20rem;
}
</style>
