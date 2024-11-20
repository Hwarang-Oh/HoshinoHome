<template>
  <div class="deal-info">
    <div class="real-transaction-info p-6 mb-6 rounded-lg shadow-lg bg-white">
      <div class="header-section mb-4">
        <h1 class="section-title">
          실거래 정보
          <span class="sub-title"
            >| {{ houseTypeMap[selectedDealVo.house_type] }} ({{
              dealTypeMap[selectedDealVo.deal_type]
            }})</span
          >
        </h1>
      </div>
      <div class="transaction-details flex justify-between items-center p-4 bg-gray-100 rounded-lg">
        <div>
          <p class="text-sm text-gray-700 font-semibold">최근 실거래</p>
          <p class="text-sm text-gray-900">{{ formattedDate }}</p>
        </div>
        <div class="flex items-center space-x-4">
          <p class="text-2xl font-semibold text-gray-900">{{ dealAmount }}</p>
          <p class="text-xl text-gray-700">{{ formattedFloor }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject, computed } from 'vue'
const selectedDealVo = inject('selectedDealVo')
const houseTypeMap = { 1: '아파트', 2: '연립/다세대', 3: '오피스텔' }
const dealTypeMap = { 1: '매매', 2: '전세', 3: '월세' }

const formatAmount = (amount) => {
  const num = parseInt(amount.replace(/,/g, ''))
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '억'
  }
  return amount
}

const formattedDate = computed(() => {
  if (!selectedDealVo.value) return ''
  const { deal_year, deal_month, deal_day } = selectedDealVo.value
  const formattedMonth = String(deal_month).padStart(2, '0')
  const formattedDay = String(deal_day).padStart(2, '0')
  return `${deal_year % 100}.${formattedMonth}.${formattedDay}`
})

const dealAmount = computed(() => {
  if (!selectedDealVo.value) return ''
  const { deal_type, deal_amount, deposit_amount, monthly_amount } = selectedDealVo.value
  if (deal_type === 1) return formatAmount(deal_amount)
  if (deal_type === 2) return formatAmount(deposit_amount)
  if (deal_type === 3) return `${formatAmount(deposit_amount)} / ${monthly_amount}`
  return ''
})

const formattedFloor = computed(() => {
  if (!selectedDealVo.value) return ''
  const floor = selectedDealVo.value.floor
  return floor < 0 ? `B${Math.abs(floor)}` : `(${floor}F)`
})
</script>

<style scoped>
.real-transaction-info {
  border: 1px solid #e5e7eb; /* Light gray border */
  border-radius: 8px;
  background-color: #ffffff; /* White background */
  padding: 1rem; /* Padding */
}

.section-title {
  font-size: 1.5rem; /* Increase title font size */
  font-weight: 700; /* Bold */
  color: #1f2937;
}

.sub-title {
  font-size: 1rem;
  font-weight: 600; /* Semi-bold */
  color: #1f2937;
}

.transaction-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f3f4f6; /* Light gray background */
  padding: 1rem;
  border-radius: 8px; /* Rounded corners */
}

.transaction-details p {
  margin: 0; /* Remove default margins */
}

.transaction-details .text-3xl {
  font-size: 2rem; /* Larger font size */
  font-weight: 700; /* Bold */
}

.transaction-details .text-xl {
  font-size: 1.25rem; /* Slightly larger font size for floor */
  font-weight: 600; /* Semi-bold */
}
</style>
