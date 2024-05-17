<script setup>
import { reactive, ref, inject, onMounted, watch } from 'vue'
const { dealVoList, detailDealList, selectedHouse } = inject('res')
const { close } = inject('service')

let miniMap = reactive({})

// Initialize the mini-map
const initMiniMap = () => {
  const container = document.getElementById('mini-map')
  const options = {
    center: new kakao.maps.LatLng(37.56813, 127.00213),
    level: 5
  }

  miniMap = new kakao.maps.Map(container, options)

  let mapTypeControl = new kakao.maps.MapTypeControl()
  let zoomControl = new kakao.maps.ZoomControl()
  miniMap.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT)
  miniMap.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT)
}

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
</script>

<template>
  <!-- Left Side (Property Detail) -->
  <div class="absolute top-0 left-0 w-1/5 h-full bg-white shadow-lg overflow-auto p-4 z-50">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-4">
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
    <div id="mini-map" class="w-full h-40 mb-4"></div>

    <!-- Info Section -->
    <div class="mb-4">
      <div class="flex items-center mb-2">
        <span class="text-xs font-medium bg-gray-200 text-gray-800 rounded-full px-2 py-1 mr-2"
          >주변</span
        >
        <span class="text-xs font-medium bg-gray-200 text-gray-800 rounded-full px-2 py-1"
          >토지</span
        >
        <span class="text-xs font-medium bg-gray-200 text-gray-800 rounded-full px-2 py-1 ml-2"
          >건물</span
        >
      </div>
      <p class="text-sm text-gray-700">분당선 압구정로데오역 도보 5분</p>
      <p class="text-sm text-gray-700">토지 52평 (172m²) · 건물 75평 (248m²)</p>
    </div>

    <!-- Tabs Section -->
    <div class="border-b border-gray-200 mb-4">
      <nav class="flex space-x-4">
        <button class="text-blue-600 border-b-2 border-blue-600 py-2" aria-current="page">
          실거래
        </button>
        <button class="text-gray-600 py-2 hover:text-blue-600 hover:border-blue-600">
          주변 정보
        </button>
        <button class="text-gray-600 py-2 hover:text-blue-600 hover:border-blue-600">
          Dong Story
        </button>
      </nav>
    </div>

    <!-- Content Section -->
    <div class="space-y-4">
      <div>
        <h2 class="text-lg font-semibold mb-2">실거래정보</h2>
        <div class="flex justify-between items-center mb-4">
          <div>
            <p class="text-sm text-gray-700">최근 실거래 12.07</p>
            <p class="text-sm font-semibold text-gray-900">47억</p>
          </div>
          <div class="flex space-x-2">
            <button class="px-3 py-1 bg-blue-500 text-white text-sm rounded">5년</button>
            <button class="px-3 py-1 bg-gray-200 text-gray-700 text-sm rounded">10년</button>
            <button class="px-3 py-1 bg-gray-200 text-gray-700 text-sm rounded">20년</button>
          </div>
        </div>
        <!-- Graph Placeholder -->
        <div class="h-40 bg-gray-100 flex items-center justify-center">
          <span class="text-gray-400">320 x 180</span>
        </div>
      </div>

      <div>
        <h2 class="text-lg font-semibold mb-2">거래 정보</h2>
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
<style scoped></style>
