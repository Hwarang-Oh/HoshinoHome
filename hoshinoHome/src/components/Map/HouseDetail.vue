<script setup>
import Header from '@/components/Map/Detail/Header.vue'
import MiniMap from '@/components/Map/Detail/MiniMap.vue'
import Info_Tab from '@/components/Map/Detail/Info_Tab.vue'
import DealInfo from '@/components/Map/Detail/DealInfo.vue'
import ChartSection from '@/components/Map/Detail/ChartSection.vue'
import TransactionInfo from '@/components/Map/Detail/TransactionInfo.vue'

import mapAPI from '@/api/map.js'
import { useUserInfoStore } from '@/stores/UserInfoStore'
import { ref, onMounted, provide, watch, inject } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
const { createdMarkers } = inject('res')
const userInfoStore = useUserInfoStore()
const selectedHouse = ref({})
const detailDealList = ref([])
const selectedDealVo = ref({})
const activeTab = ref('realTransaction')

const loadHouseDetail = async (house_code) => {
  try {
    mapAPI.getHouseInfo(
      house_code,
      (response) => {
        selectedHouse.value = response.data
      },
      () => {}
    )
    mapAPI.getHouseDealList(
      house_code,
      (response) => {
        detailDealList.value = response.data
      },
      () => {}
    )
    selectedDealVo.value = userInfoStore.selectedHouseDealVo
  } catch (error) {
    console.error('Failed to load house detail:', error)
  }
}

provide('selectedHouse', selectedHouse)
provide('detailDealList', detailDealList)
provide('selectedDealVo', selectedDealVo)
provide('activeTab', activeTab)

onMounted(() => {
  const house_code = route.params.house_code
  if (house_code) {
    loadHouseDetail(house_code)
  }
})

watch(
  () => route.params.house_code,
  (newHouseCode) => {
    if (newHouseCode) {
      loadHouseDetail(newHouseCode)
    }
  },
  {
    immediate: true
  }
)
</script>

<template>
  <!-- Left Side (Property Detail) -->
  <div
    id="houseDetail"
    class="absolute top-0 left-0 h-full bg-white shadow-lg overflow-auto p-4 z-50 custom-scrollbar"
  >
    <!-- Header Section -->
    <Header />
    <hr class="contour mb-4" />
    <!-- SubMap Section -->
    <MiniMap />

    <!-- Info_Tab Section -->
    <Info_Tab />

    <!-- Conditional Rendering -->
    <div v-if="activeTab === 'realTransaction'">
      <DealInfo />
      <ChartSection />
    </div>
    <div v-else-if="activeTab === 'transactionHistory'">
      <TransactionInfo />
    </div>
  </div>
</template>

<style scoped>
#houseDetail {
  width: 23%;
}
.contour {
  border: 0;
  height: 1px;
  background: #e5e7eb; /* Gray color */
  margin-bottom: 1rem;
}

/* Custom scrollbar styles */
.custom-scrollbar::-webkit-scrollbar {
  width: 8px;
  height: 8px; /* For horizontal scrollbar if needed */
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #bebebe;
  border-radius: 10px;
  border: 2px solid #f1f1f1;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #a1a1a1;
}
</style>
