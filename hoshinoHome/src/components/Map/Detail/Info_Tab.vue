<template>
  <div class="info-section mb-4">
    <div class="flex items-center mb-2">
      <span class="info-tag">토지</span>
      <p class="text-sm text-gray-700 ml-2">
        {{ selectedDealVo.area }} m² ({{ convertToPyeong(selectedDealVo.area) }} 평)
      </p>
    </div>
    <div class="flex items-center mb-2">
      <span class="info-tag">건물</span>
      <p class="text-sm text-gray-700 ml-2">
        {{ selectedHouse.house_name + ' (' + selectedHouse.build_year + '년도 건설)' }}
      </p>
    </div>
  </div>
  <div class="tabs-section border-b border-gray-200 mb-4">
    <nav class="flex justify-between">
      <button
        class="tab-button"
        :class="{ active: activeTab === 'realTransaction' }"
        @click="setActiveTab('realTransaction')"
      >
        실거래
      </button>
      <button
        class="tab-button"
        :class="{ active: activeTab === 'transactionHistory' }"
        @click="setActiveTab('transactionHistory')"
      >
        거래 내역
      </button>
      <button class="tab-button" @click="goToDongStory">Dong Story</button>
    </nav>
  </div>
</template>

<script setup>
import { ref, inject, watch } from 'vue'
import { useRouter } from 'vue-router'

const selectedHouse = inject('selectedHouse')
const selectedDealVo = inject('selectedDealVo')
const activeTab = inject('activeTab') // activeTab을 inject로 받아옵니다.
const router = useRouter()

const setActiveTab = (tab) => {
  activeTab.value = tab
}

const goToDongStory = () => {
  if (selectedHouse.value.dong_name) {
    router.push({ name: 'dongStory', query: { dongName: selectedHouse.value.dong_name } })
  } else {
    console.error('dong_name is not defined')
  }
}

const convertToPyeong = (squareMeters) => {
  return (squareMeters / 3.3058).toFixed(2) // 1평 = 3.3058 제곱미터
}

// Watch for changes in selectedHouse to perform any required action
watch(
  () => selectedHouse.value,
  () => {
    // Perform any required action when selectedHouse changes
  }
)
</script>

<style scoped>
.info-section {
  margin-bottom: 1rem;
}

.info-tag {
  font-size: 0.75rem;
  font-weight: 500;
  background-color: #e5e7eb;
  color: #374151;
  border-radius: 9999px;
  padding: 0.25rem 0.5rem;
  margin-right: 0.5rem;
}

.tabs-section {
  border-bottom: 1px solid #d1d5db;
  margin-bottom: 1rem;
}

.tab-button {
  padding: 0.75rem 1.5rem;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition:
    border-color 0.2s,
    color 0.2s,
    background-color 0.2s;
  font-size: 0.875rem;
  font-weight: 600;
  color: #6b7280;
  flex: 1; /* Flex property to ensure equal width */
  text-align: center; /* Center text */
}

.tab-button.active {
  color: #1d4ed8;
  border-color: #1d4ed8;
  background-color: #e0f2fe;
}
</style>
