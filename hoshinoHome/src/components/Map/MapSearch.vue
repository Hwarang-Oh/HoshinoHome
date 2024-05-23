<script setup>
import { ref, inject, onMounted, watchEffect } from 'vue'
import { useUserInfoStore } from '@/stores/UserInfoStore'
import { debounce } from 'lodash'
import { useRouter } from 'vue-router'
import mapAPI from '@/api/map.js'
import axios from 'axios'
const { activeButtons } = inject('res')
const { initMap } = inject('service')
const router = useRouter()
const userInfoStore = useUserInfoStore()
const dropdownVisible = ref(false)
const searchPositionStyle = ref({})
const query = ref('')
const suggestions = ref([])
const showSuggestions = ref(false)
const selectedSuggestionIndex = ref(-1)
const searchedHouse = ref(null)
const isSuggestionSelected = ref(false)

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
const fetchSuggestionsDebounced = debounce(() => {
  if (query.value.length > 0) {
    mapAPI.searchHouseInfoByQuery(
      query.value.trim(),
      (response) => {
        suggestions.value = response.data
        showSuggestions.value = true
      },
      () => {
        console.error('자동완성 결과를 가져오지 못했습니다.')
        suggestions.value = []
        showSuggestions.value = false
      }
    )
  } else {
    suggestions.value = []
    showSuggestions.value = false
  }
}, 100) // 100ms의 지연시간을 설정합니다.

const fetchSuggestions = (event) => {
  query.value = event.target.value
  isSuggestionSelected.value = false
  fetchSuggestionsDebounced()
}

const selectSuggestion = (suggestion) => {
  query.value = `${suggestion.house_name} (${suggestion.road_address})`
  isSuggestionSelected.value = true
  suggestions.value = []
  showSuggestions.value = false
}

const fetchHouseDetailAndNavigate = async () => {
  if (isSuggestionSelected.value) {
    const roadAddressMatch = query.value.match(/\(([^)]+)\)/)
    const roadAddress = roadAddressMatch ? roadAddressMatch[1] : query.value
    mapAPI.searchHouseInfoByRoadAddress(
      roadAddress,
      (response) => {
        if (response.data) {
          searchedHouse.value = response.data
          userInfoStore.setUserLocation({
            lat: searchedHouse.value.lat,
            lng: searchedHouse.value.lng
          })
          userInfoStore.setSearchedHouseInfo(searchedHouse.value)
          router.push('/map')
        } else {
          console.log('해당하는 검색 결과가 없습니다.')
        }
      },
      () => {
        console.log('실패')
      }
    )
  } else {
    const kakaoApiKey = '826ff6e6031401a7fc4aba8410ea5565' // 여기에 Kakao REST API 키를 입력하세요.
    const kakaoApiUrl = `https://dapi.kakao.com/v2/local/search/keyword.json?query=${query.value}`

    try {
      const response = await axios.get(kakaoApiUrl, {
        headers: { Authorization: `KakaoAK ${kakaoApiKey}` }
      })
      if (response.data.documents.length > 0) {
        const location = response.data.documents[0]
        console.log('Kakao 검색 결과:', location)
        userInfoStore.setUserLocation({ lat: location.y, lng: location.x })
        router.push('/map')
      } else {
        console.log('Kakao 검색 결과가 비어있습니다.')
        suggestions.value = [{ house_name: '검색 결과가 없습니다', road_address: '' }]
        showSuggestions.value = true
      }
    } catch (error) {
      console.error('Kakao 검색 실패:', error)
      suggestions.value = [{ house_name: '검색 결과가 없습니다', road_address: '' }]
      showSuggestions.value = true
    }
  }
}

const handleKeydown = async (event) => {
  if (event.key === 'Enter') {
    await fetchHouseDetailAndNavigate()
  } else if (event.key === 'ArrowDown') {
    if (selectedSuggestionIndex.value < suggestions.value.length - 1) {
      selectedSuggestionIndex.value++
    }
  } else if (event.key === 'ArrowUp') {
    if (selectedSuggestionIndex.value > 0) {
      selectedSuggestionIndex.value--
    }
  } else if (event.key === 'Tab') {
    if (
      selectedSuggestionIndex.value >= 0 &&
      selectedSuggestionIndex.value < suggestions.value.length
    ) {
      selectSuggestion(suggestions.value[selectedSuggestionIndex.value])
    }
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
        v-model="query"
        @input="fetchSuggestions"
        @keydown="handleKeydown"
        placeholder="지역/주소/건물명 검색"
        class="w-full p-2 border border-gray-300 rounded"
      />
      <svg
        @click="fetchHouseDetailAndNavigate"
        class="absolute right-2 w-4 h-4 text-gray-500"
        fill="currentColor"
        viewBox="0 0 20 20"
      >
        <path
          fill-rule="evenodd"
          d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1111.748 3.168l4.317 4.317a1 1 0 01-1.414 1.414l-4.317-4.317A6 6 0 012 8z"
          clip-rule="evenodd"
        />
      </svg>
    </div>

    <div class="relative">
      <ul
        v-if="showSuggestions && suggestions.length"
        class="absolute left-0 right-0 bg-white border border-gray-300 rounded z-10"
      >
        <li
          v-for="(suggestion, index) in suggestions"
          :key="index"
          @mousedown.prevent="selectSuggestion(suggestion)"
          class="p-2 cursor-pointer hover:bg-gray-200"
          :class="{
            'bg-blue-100': index === selectedSuggestionIndex,
            'text-red-500 font-bold': suggestion.house_name === '검색 결과가 없습니다'
          }"
        >
          {{ suggestion.house_name }}
          <span v-if="suggestion.road_address">({{ suggestion.road_address }})</span>
        </li>
      </ul>
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
@keyframes fade-slide-in {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-slide-in {
  animation: fade-slide-in 1.5s ease-out;
}

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
