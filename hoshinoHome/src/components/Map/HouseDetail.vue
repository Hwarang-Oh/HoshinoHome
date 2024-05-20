<script setup>
import { ref, reactive, computed, onMounted, inject, watch } from 'vue'
import { useRouter } from 'vue-router'
import DealTypeChart from './DealTypeChart.vue'
import DealAmountChart from './DealAmountChart.vue'
const { detailDealList, selectedHouse } = inject('res')
const { close } = inject('service')

let miniMap = reactive({}) // miniMap을 ref로 선언하여 null로 초기화
const marker = ref(null)
let roadView = reactive({})
const isRoadViewVisible = ref(false)
// Function to initialize the mini-map
const initMiniMap = () => {
  const container = document.getElementById('mini-map')
  const options = {
    center: new kakao.maps.LatLng(selectedHouse.value.lat, selectedHouse.value.lng),
    level: 5
  }

  miniMap = new kakao.maps.Map(container, options)
  addMarker(options.center)
}

const addMarker = (position) => {
  if (marker.value) {
    marker.value.setMap(null)
  }

  marker.value = new kakao.maps.Marker({
    position: position,
    map: miniMap
  })
}

watch(
  () => selectedHouse.value,
  (newHouse) => {
    if (newHouse && newHouse.lat && newHouse.lng) {
      initMiniMap()
    }
  },
  { immediate: true }
)

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMiniMap()
  } else {
    const script = document.createElement('script')
    script.onload = () => kakao.maps.load(initMiniMap)
    script.src =
      '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4aa94d500c252274b8dc18944a4026f5'
    document.head.appendChild(script)
  }
})

// 2. Compute Chart Data for dealTypeCount
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

const chartType = ref('dealAmount')

function setChartType(type) {
  chartType.value = type
}

const router = useRouter()
const goToDongStory = () => {
  if (selectedHouse.value.dong_name) {
    router.push({ name: 'dongStory', query: { dongName: selectedHouse.value.dong_name } })
  } else {
    console.error('dong_name is not defined')
  }
}
</script>

<template>
  <!-- Left Side (Property Detail) -->
  <div class="absolute top-0 left-0 w-1/5 h-full bg-white shadow-lg overflow-auto p-4 z-50">
    <!-- Header Section -->
    <div class="header-section mb-4">
      <div>
        <h1 class="text-xl font-semibold">{{ selectedHouse.road_address }}</h1>
        <p class="text-sm text-gray-600">{{ selectedHouse.jibun_address }}</p>
      </div>
      <button class="text-gray-500 hover:text-gray-700" @click="close">
        <svg
          class="w-6 h-6"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M6 18L18 6M6 6l12 12"
          ></path>
        </svg>
      </button>
    </div>

    <!-- SubMap Section -->
    <div id="mini-map" class="submap-section relative w-full h-40 mb-4"></div>

    <!-- Info Section -->
    <div class="info-section mb-4">
      <div class="flex items-center mb-2">
        <span class="info-tag">주변</span>
        <span class="info-tag">토지</span>
        <span class="info-tag ml-2">건물</span>
      </div>
      <p class="text-sm text-gray-700">분당선 압구정로데오역 도보 5분</p>
      <p class="text-sm text-gray-700">토지 52평 (172m²) · 건물 75평 (248m²)</p>
    </div>

    <!-- Tabs Section -->
    <div class="tabs-section border-b border-gray-200 mb-4">
      <nav class="flex space-x-4">
        <button class="tab-button active" aria-current="page">실거래</button>
        <button class="tab-button">주변 정보</button>
        <button class="tab-button" @click="goToDongStory">Dong Story</button>
      </nav>
    </div>

    <!-- Content Section -->
    <div class="content-section space-y-4">
      <div class="deal-info">
        <h2 class="section-title">실거래정보</h2>
        <div class="flex justify-between items-center mb-4">
          <div>
            <p class="text-sm text-gray-700">최근 실거래 12.07</p>
            <p class="text-sm font-semibold text-gray-900">47억</p>
          </div>
          <div class="flex space-x-2">
            <button class="year-button active">5년</button>
            <button class="year-button">10년</button>
            <button class="year-button">20년</button>
          </div>
        </div>
        <!-- Graph Placeholder -->
        <div class="h-40 bg-gray-100 flex items-center justify-center">
          <DealTypeChart v-if="detailDealList.length" :data="dealTypeCounts" />
        </div>
        <div class="toggle-buttons">
          <button
            @click="setChartType('dealAmount')"
            :class="{ active: chartType === 'dealAmount' }"
          >
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

      <!-- DealList Placeholder -->
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
    </div>
  </div>
</template>

<style scoped>
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.submap-section {
  position: relative;
  width: 100%;
  height: 10rem;
  margin-bottom: 1rem;
}

.info-section {
  margin-bottom: 1rem;
}

.tabs-section {
  border-bottom: 1px solid #e5e7eb;
  margin-bottom: 1rem;
}

.tab-button {
  padding: 0.5rem 1rem;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: border-color 0.2s;
}

.tab-button.active {
  color: #1d4ed8;
  border-color: #1d4ed8;
}

.content-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.deal-info {
  margin-bottom: 1rem;
}

.transaction-info {
  margin-bottom: 1rem;
}

.section-title {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
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

.year-button {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  border-radius: 0.375rem;
  cursor: pointer;
  transition:
    background-color 0.2s,
    color 0.2s;
}

.year-button.active {
  background-color: #3b82f6;
  color: white;
}

.year-button:hover {
  background-color: #2563eb;
  color: white;
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
