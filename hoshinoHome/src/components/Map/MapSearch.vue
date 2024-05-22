<script setup>
import { ref, inject, onMounted, watchEffect } from 'vue'
import { useUserInfoStore } from '@/stores/UserInfoStore'
const { activeButtons } = inject('res')
const { initMap } = inject('service')
const userInfoStore = useUserInfoStore()
const dropdownVisible = ref(false)

const searchPositionStyle = ref({})

watchEffect(() => {
  if (userInfoStore.isHouseDetailOpen) {
    searchPositionStyle.value = {
      transform: `translateX(23vw)`,
      transition: 'transform 0.3s ease-in-out'
    }
  } else {
    searchPositionStyle.value = {
      transform: 'translateX(0)',
      transition: 'transform 0.3s ease-in-out'
    }
  }
})

const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value
  if (!dropdownVisible.value) {
    ensureMinimumSelection()
  }
}

const toggleActive = (buttonName) => {
  const propertyTypes = ['apartment', 'multiFamily', 'officetel']
  const dealTypes = ['sale', 'lease', 'monthly']

  const isPropertyType = propertyTypes.includes(buttonName)
  const isDealType = dealTypes.includes(buttonName)

  const activePropertyTypes = activeButtons.value.filter((type) => propertyTypes.includes(type))
  const activeDealTypes = activeButtons.value.filter((type) => dealTypes.includes(type))

  const index = activeButtons.value.indexOf(buttonName)
  if (index > -1) {
    if (
      (isPropertyType && activePropertyTypes.length === 1) ||
      (isDealType && activeDealTypes.length === 1)
    ) {
      return
    }
    activeButtons.value.splice(index, 1)
  } else {
    activeButtons.value.push(buttonName)
  }

  ensureMinimumSelection()
  initMap()
}

const ensureMinimumSelection = () => {
  const propertyTypes = ['apartment', 'multiFamily', 'officetel']
  const dealTypes = ['sale', 'lease', 'monthly']

  const hasPropertyType = propertyTypes.some((type) => activeButtons.value.includes(type))
  const hasDealType = dealTypes.some((type) => activeButtons.value.includes(type))

  if (!hasPropertyType) {
    activeButtons.value.push(propertyTypes[0])
  }

  if (!hasDealType) {
    activeButtons.value.push(dealTypes[0])
  }
}

onMounted(() => {
  ensureMinimumSelection()
})
</script>

<template>
  <head>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <div
    :class="['absolute z-10 p-4 top-4 left-4 bg-white bg-opacity-90 rounded shadow w-80']"
    :style="searchPositionStyle"
  >
    <div class="relative w-full flex items-center mb-2">
      <input
        type="text"
        placeholder="지역/지하철/학교명 검색"
        class="w-full p-2 border border-gray-300 rounded"
      />
      <svg class="absolute right-2 w-4 h-4 text-gray-500" fill="currentColor" viewBox="0 0 20 20">
        <path
          fill-rule="evenodd"
          d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1111.748 3.168l4.317 4.317a1 1 0 01-1.414 1.414l-4.317-4.317A6 6 0 012 8z"
          clip-rule="evenodd"
        />
      </svg>
    </div>

    <div class="relative">
      <button
        class="w-full px-3 py-2 mb-2 rounded border border-gray-300 text-gray-700 bg-white hover:bg-gray-100"
        @click="toggleDropdown"
      >
        상세조건
      </button>

      <div
        v-if="dropdownVisible"
        class="absolute left-0 w-full bg-white border border-gray-300 rounded shadow-md mt-1 p-4 z-20"
      >
        <div class="mb-4">
          <div class="font-semibold mb-2">매물 유형</div>
          <div class="flex flex-wrap justify-between -mx-1">
            <button
              class="flex-1 mx-1 mb-2 px-4 py-2 rounded border border-blue-500 text-gray-700 bg-white hover:bg-blue-500 hover:text-white"
              @click="toggleActive('apartment')"
              :class="{ active: activeButtons.includes('apartment') }"
            >
              <i class="fas fa-building mr-2"></i>아파트
            </button>
            <button
              class="flex-1 mx-1 mb-2 px-4 py-2 rounded border border-blue-500 text-gray-700 bg-white hover:bg-blue-500 hover:text-white"
              @click="toggleActive('multiFamily')"
              :class="{ active: activeButtons.includes('multiFamily') }"
            >
              <i class="fas fa-home mr-2"></i>연립/다세대
            </button>
            <button
              class="flex-1 mx-1 mb-2 px-4 py-2 rounded border border-blue-500 text-gray-700 bg-white hover:bg-blue-500 hover:text-white"
              @click="toggleActive('officetel')"
              :class="{ active: activeButtons.includes('officetel') }"
            >
              <i class="fas fa-city mr-2"></i>오피스텔
            </button>
          </div>
        </div>

        <div>
          <div class="font-semibold mb-2">거래 유형</div>
          <div class="flex flex-wrap justify-between -mx-1">
            <button
              class="flex-1 mx-1 mb-2 px-4 py-2 rounded border border-blue-500 text-gray-700 bg-white hover:bg-blue-500 hover:text-white"
              @click="toggleActive('sale')"
              :class="{ active: activeButtons.includes('sale') }"
            >
              매매
            </button>
            <button
              class="flex-1 mx-1 mb-2 px-4 py-2 rounded border border-blue-500 text-gray-700 bg-white hover:bg-blue-500 hover:text-white"
              @click="toggleActive('lease')"
              :class="{ active: activeButtons.includes('lease') }"
            >
              전세
            </button>
            <button
              class="flex-1 mx-1 mb-2 px-4 py-2 rounded border border-blue-500 text-gray-700 bg-white hover:bg-blue-500 hover:text-white"
              @click="toggleActive('monthly')"
              :class="{ active: activeButtons.includes('monthly') }"
            >
              월세
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
button {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 130px;
  height: 50px;
  font-size: 12px;
}

button.active {
  background-color: #1876d6;
  color: white;
}
</style>
