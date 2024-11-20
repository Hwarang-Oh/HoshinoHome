<script setup>
import mapAPI from '@/api/map.js'
import naverAPI from '@/api/naver.js'
import { debounce } from 'lodash'
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserInfoStore } from '@/stores/UserInfoStore.js'
import axios from 'axios'
import DOMPurify from 'dompurify'

// 이미지 파일 이름 배열
const issueImages = [
  'apartNews.jpg',
  'issue1.jpg',
  'issue3.jpg',
  'issue5.jpg',
  'winter.jpg',
  'issue6.jpg',
  'issue7.jpg',
  'issue8.jpg',
  'issue9.png',
  'banpoXI.jpg',
  'etherno.jpg',
  'etherno2.jpg',
  'hanriver.jpg'
]

const getRandomImage = () => {
  const randomIndex = Math.floor(Math.random() * issueImages.length)
  return issueImages[randomIndex]
}

const router = useRouter()
const userInfoStore = useUserInfoStore()
const randomImage = ref(getRandomImage())
const query = ref('')
const suggestions = ref([])
const newsPosts = ref([])
const showSuggestions = ref(false)
const selectedSuggestionIndex = ref(-1)
const searchedHouse = ref(null)
const isSuggestionSelected = ref(false)
const userLocation = ref({ lat: null, lng: null })
const locationError = ref(null)

const getLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        userLocation.value = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        }
        userInfoStore.setUserLocation(userLocation.value)
        console.log('User location:', userLocation.value)
        console.log('pinia location : ', userInfoStore.userLocation)
      },
      (error) => {
        switch (error.code) {
          case error.PERMISSION_DENIED:
            locationError.value = 'User denied the request for Geolocation.'
            break
          case error.POSITION_UNAVAILABLE:
            locationError.value = 'Location information is unavailable.'
            break
          case error.TIMEOUT:
            locationError.value = 'The request to get user location timed out.'
            break
          case error.UNKNOWN_ERROR:
            locationError.value = 'An unknown error occurred.'
            break
        }
        console.error('Geolocation error:', locationError.value)
      }
    )
  } else {
    locationError.value = 'Geolocation is not supported by this browser.'
    console.error('Geolocation error:', locationError.value)
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
          console.log('해당하는 검색 결과가없습니다.')
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

const fetchBlogPosts = () => {
  naverAPI
    .searchNews()
    .then((response) => {
      newsPosts.value = response.data.items
    })
    .catch((error) => {
      console.error('뉴스 검색 실패!', error)
    })
}

const handleClickOutside = (event) => {
  if (!event.target.closest('.suggestion-container')) {
    showSuggestions.value = false
  }
}

onMounted(() => {
  fetchBlogPosts()
  getLocation()
  document.addEventListener('click', handleClickOutside)
})
</script>

<template>
  <main>
    <div>
      <!-- Hero Section -->
      <section class="relative">
        <img
          src="/skyscraper.jpg"
          alt="Hero Background"
          class="w-full object-cover opacity-80 bg"
        />

        <!-- 어두운 오버레이 추가 -->
        <div class="absolute inset-0 bg-black opacity-50"></div>

        <div
          class="absolute inset-0 flex flex-col justify-center items-center text-center text-white"
        >
        <h1 class="text-4xl font-bold mb-4 animate-fade-slide-in">
       <span class="text-pink-400">최애의 집</span>을 찾으러 갈 준비가 되셨나요?
    </h1>
          <p class="text-lg mb-4 animate-fade-slide-in">아파트, 주택, 오피스텔까지 모두 알려드려요!</p>

          <div class="relative w-96 animate-fade-slide-in">
            <div class="pt-2 relative mx-auto text-gray-600 suggestion-container">
              <input
                type="text"
                v-model="query"
                @input="fetchSuggestions"
                @keydown="handleKeydown"
                placeholder="지역/주소, 건물명으로 검색하세요"
                class="border-2 border-gray-300 bg-white h-12 px-5 pr-16 rounded-lg text-lg focus:outline-none w-full"
              />
              <button @click="fetchHouseDetailAndNavigate" class="absolute right-0 top-0 mt-2 mr-4">
                <svg
                  class="text-gray-600 h-4 w-4 fill-current mt-3.5"
                  xmlns="http://www.w3.org/2000/svg"
                  xmlns:xlink="http://www.w3.org/1999/xlink"
                  version="1.1"
                  id="Capa_1"
                  x="0px"
                  y="0px"
                  viewBox="0 0 56.966 56.966"
                  style="enable-background: new 0 0 56.966 56.966"
                  xml:space="preserve"
                  width="512px"
                  height="512px"
                >
                  <path
                    d="M55.146,51.887L41.588,37.786c3.486-4.144,5.396-9.358,5.396-14.786c0-12.682-10.318-23-23-23s-23,10.318-23,23  s10.318,23,23,23c4.761,0,9.298-1.436,13.177-4.162l13.661,14.208c0.571,0.593,1.339,0.92,2.162,0.92  c0.779,0,1.518-0.297,2.079-0.837C56.255,54.982,56.293,53.08,55.146,51.887z M23.984,6c9.374,0,17,7.626,17,17s-7.626,17-17,17  s-17-7.626-17-17S14.61,6,23.984,6z"
                  />
                </svg>
              </button>
              <ul
                v-if="showSuggestions && suggestions.length"
                class="absolute left-0 right-0 bg-white border border-gray-300 rounded mt-1 z-10"
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
            </div>
          </div>
        </div>
      </section>

      <!-- News and Detailed News Section -->
      <section
        class="flex flex-col md:flex-row justify-center space-y-6 md:space-y-0 md:space-x-6 my-8 text-lg"
      >
        <!-- News List Section -->
        <div
          class="bg-white p-6 rounded-lg shadow-lg w-full md:w-1/2 mb-6 md:mb-0 animate-fade-slide-in"
        >
          <h2 class="text-2xl font-semibold mb-4">공지/뉴스</h2>
          <ul>
            <li v-for="(post, index) in newsPosts.slice(0, 5)" :key="index" class="mb-2">
              <a
                :href="post.link"
                target="_blank"
                class="text-gray-800 hover:text-blue-700"
                v-html="DOMPurify.sanitize(post.title)"
              ></a>
              <span class="text-gray-500 ml-2">{{
                new Date(post.pubDate).toLocaleDateString()
              }}</span>
            </li>
          </ul>
        </div>

        <!-- Detailed News Section -->
        <div
          v-if="newsPosts.length > 5"
          class="bg-white p-6 rounded-lg shadow-lg w-full md:w-1/2 animate-fade-slide-in"
        >
          <h2 class="text-2xl font-semibold mb-4">오늘의 이슈</h2>
          <div class="flex flex-col md:flex-row gap-4">
            <div class="flex-shrink-0 w-full h-48 md:h-auto md:w-48">
              <img
                :src="`/issues/${randomImage}`"
                alt="News Image"
                class="w-full h-full object-cover rounded-md"
              />
            </div>
            <div class="flex flex-col flex-1">
              <a
                :href="newsPosts[5].link"
                target="_blank"
                class="text-lg font-semibold text-gray-800 hover:text-blue-700 mb-2 block"
                v-html="DOMPurify.sanitize(newsPosts[5].title)"
              ></a>
              <p
                class="text-gray-600 flex-1 mb-2"
                v-html="DOMPurify.sanitize(newsPosts[5].description)"
              ></p>
              <div class="text-gray-500 text-right">
                {{ new Date(newsPosts[5].pubDate).toLocaleDateString() }}
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </main>
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

.bg {
  height: calc(100vh - 180px);
}

@media (min-width: 768px) {
  .space-x-6 > :not(:last-child) {
    margin-right: 1.5rem;
  }
}
</style>
