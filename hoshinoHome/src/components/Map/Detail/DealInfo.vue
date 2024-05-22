<template>
  <div class="deal-info">
    <div class="real-transaction-info p-4 mb-4 rounded-lg shadow-lg bg-white">
      <div class="header-section mb-4">
        <h2 class="section-title">
          실거래정보
          <span class="sub-title">| {{ houseTypeMap[selectedDealVo.house_type] }}</span>
        </h2>
      </div>
      <div class="transaction-details flex justify-between items-center p-4 bg-gray-100 rounded-lg">
        <div>
          <p class="text-sm text-gray-700">최근 실거래 {{ formattedDate }}</p>
          <p class="text-3xl font-semibold text-gray-900">
            {{ dealAmount }}
            <span class="text-lg"> ({{ selectedDealVo ? selectedDealVo.floor : '' }}F)</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject, computed } from 'vue'
const selectedDealVo = inject('selectedDealVo')
// const { selectedDealVo } = inject('res')

const houseTypeMap = { 1: '아파트', 2: '연립/다세대', 3: '오피스텔' }
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
</script>

<style scoped>
.real-transaction-info {
  border: 1px solid #e5e7eb; /* Light gray border */
  border-radius: 8px;
  background-color: #ffffff; /* White background */
  padding: 1rem; /* Padding */
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
}

.transaction-details .text-lg {
  font-size: 1rem; /* Smaller font size */
}
</style>
