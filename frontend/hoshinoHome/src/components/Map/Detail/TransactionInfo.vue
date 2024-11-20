<template>
  <div class="transaction-info">
    <h2 class="section-title">거래 정보</h2>
    <div class="transaction-list space-y-2 overflow-y-auto custom-scrollbar">
      <!-- 표의 첫 행에 제목 추가 -->
      <div class="p-2 bg-gray-200 rounded-lg shadow-sm flex justify-between items-center font-bold text-gray-700">
        <div class="flex-1 text-center">거래일</div>
        <div class="flex-1 text-center">거래유형</div>
        <div class="flex-1 text-center">거래금액</div>
        <div class="flex-1 text-center">층수</div>
      </div>
      <!-- 데이터 항목들 -->
      <div
        v-for="(deal, index) in formattedDealList"
        :key="index"
        class="flex justify-between items-center p-2 border-b border-gray-200"
        :class="{
          'bg-blue-50': index % 2 === 0,
          'bg-white': index % 2 !== 0
        }"
      >
        <span class="text-sm text-gray-700 flex-1 text-center">{{ deal.date }}</span>
        <span
          class="text-sm font-semibold flex-1 text-center"
          :class="{
            'text-red-500': deal.type === '전세',
            'text-blue-500': deal.type === '월세',
            'text-green-500': deal.type === '매매'
          }"
        >
          {{ deal.type }}
        </span>
        <span class="text-sm text-gray-900 flex-1 text-center">{{ deal.price }}</span>
        <span class="text-sm text-gray-900 flex-1 text-center">{{ deal.floor }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject, computed } from 'vue'

const detailDealList = inject('detailDealList')

const formatPrice = (amount) => {
  const num = parseInt(amount.replace(/,/g, ''), 10)
  return num >= 10000 ? `${(num / 10000).toFixed(1)}억` : `${num}`
}

const formattedDealList = computed(() => {
  return detailDealList.value.map(deal => {
    const date = `${deal.deal_year % 100}.${String(deal.deal_month).padStart(2, '0')}.${String(deal.deal_day).padStart(2, '0')}`
    const type = deal.deal_type === 1 ? '매매' : deal.deal_type === 2 ? '전세' : '월세'
    const price = type === '매매'
      ? formatPrice(deal.deal_amount)
      : type === '전세'
        ? formatPrice(deal.deposit_amount)
        : `${formatPrice(deal.deposit_amount)} / ${formatPrice(deal.monthly_amount)}`
    const floor = deal.floor < 0 ? `B${Math.abs(deal.floor)}층` : `${deal.floor}층`

    return {
      date,
      type,
      price,
      floor
    }
  })
})
</script>

<style scoped>
.transaction-info {
  margin-bottom: 1rem;
  max-height: 400px; /* Set a max-height for scrollable area */
  overflow-y: auto; /* Enable vertical scrolling */
  background-color: #f9fafb; /* Light gray background */
  padding: 1rem;
  border-radius: 8px;
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

.bg-blue-50 {
  background-color: #ebf8ff;
}

.bg-white {
  background-color: #ffffff;
}

/* Custom scrollbar styles */
.custom-scrollbar::-webkit-scrollbar {
  width: 8px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #888;
  border-radius: 10px;
  border: 2px solid #f1f1f1;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #555;
}
</style>
