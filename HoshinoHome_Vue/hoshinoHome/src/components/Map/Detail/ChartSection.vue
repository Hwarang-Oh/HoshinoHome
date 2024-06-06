<template>
  <div class="statistics-box p-6 rounded-lg shadow-lg bg-white">
    <h2 class="main-title mb-6">실거래 통계</h2>

    <div v-if="detailDealList.length === 0" class="empty-list h-40">
      <p>거래 내역이 없습니다.</p>
    </div>
    <div v-else>
      <div class="chart-section mb-6">
        <div class="section-header">
          <h2 class="chart-title">거래 유형</h2>
        </div>
        <div class="chart-container bg-gray-100 p-4 rounded-lg">
          <DealTypeChart :data="dealTypeCounts" />
        </div>
      </div>

      <div class="chart-section">
        <div class="section-header">
          <h2 class="chart-title">시세 추이</h2>
        </div>
        <div class="chart-container bg-gray-100 p-4 rounded-lg">
          <div class="toggle-buttons mb-4">
            <button
              @click="setChartType('dealAmount')"
              :class="{ active: chartType === 'dealAmount' }"
            >
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
          <div v-if="chartType === 'dealAmount' && dealTypeCounts.counts[0] > 0" class="h-40">
            <DealAmountChart :dealData="detailDealList" :chartType="chartType" />
          </div>
          <div
            v-else-if="chartType === 'depositAmount' && dealTypeCounts.counts[1] > 0"
            class="h-40"
          >
            <DealAmountChart :dealData="detailDealList" :chartType="chartType" />
          </div>
          <div v-else-if="chartType === 'monthlyAmount' && dealTypeCounts.counts[2] > 0" class="">
            <DealAmountChart :dealData="detailDealList" :chartType="chartType" />
          </div>
          <div v-else class="empty-list h-40">
            <p><img src="https://i.namu.wiki/i/d3Hb766-6zV7ghgjoN21EKQTiNZLccPhXus2oSEYUM__btiILfmIHv5WMzLJFrohgPNumr3oeTKotx3zwJAQfQ.webp" class="h-40"></p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, inject, computed } from 'vue'
import DealAmountChart from '../Chart/DealAmountChart.vue'
import DealTypeChart from '../Chart/DealTypeChart.vue'

const detailDealList = inject('detailDealList')
const chartType = ref('dealAmount')

function setChartType(type) {
  chartType.value = type
}

const dealTypeCounts = computed(() => {
  const counts = { 1: 0, 2: 0, 3: 0 }
  if (Array.isArray(detailDealList.value)) {
    detailDealList.value.forEach((deal) => {
      if (counts[deal.deal_type] !== undefined) counts[deal.deal_type]++
    })
  }
  return {
    labels: ['매매', '전세', '월세'],
    counts: Object.values(counts)
  }
})
</script>

<style scoped>
.statistics-box {
  max-width: 800px;
  margin: 0 auto;
  border: 1px solid #e5e7eb; /* Light gray border */
  border-radius: 8px;
  background-color: #ffffff; /* White background */
  padding: 1rem; /* Padding */
}

.main-title {
  font-size: 1.5rem;
  font-weight: bold;
  text-align: left;
  color: #111827;
}

.chart-section {
  margin-bottom: 2rem;
}

.section-header {
  border-left: 4px solid #42a5f5;
  padding-left: 10px;
  margin-bottom: 1rem;
}

.chart-title {
  font-size: 1rem;
  font-weight: 600; /* 글씨를 조금 더 진하게 */
  color: #1f2937;
}

.chart-container {
  background-color: #f3f4f6; /* Light gray background */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: var(--tw-shadow);
  border-radius: var(--tw-rounded);
  padding: 1rem;
}

.toggle-buttons {
  display: flex;
  justify-content: space-around;
  margin-bottom: 10px;
}

.toggle-buttons button {
  padding: 6px 12px;
  border: none;
  cursor: pointer;
  background-color: #ddd;
  border-radius: 5px;
  font-size: 14px;
  transition:
    background-color 0.2s,
    color 0.2s;
}

.toggle-buttons button.active {
  background-color: #42a5f5;
  color: white;
}

.empty-list {
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--tw-shadow);
  border-radius: var(--tw-rounded);
}

.h-40 {
  height: 10rem;
}

.expanded-chart {
  height: 20rem; /* 무한히 커지지 않도록 수정 */
  width: 100%;
}
</style>
