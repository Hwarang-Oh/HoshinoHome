<script setup>
import mapAPI from '@/api/map.js'
import naverAPI from '@/api/naver.js'
import { ref, onMounted } from 'vue'
import DOMPurify from 'dompurify'

const query = ref('')
const suggestions = ref([])
const newsPosts = ref([])

const fetchSuggestions = () => {
  if (query.value.length > 0) {
    mapAPI.searchHouseInfoByQuery(
      query.value,
      (response) => {
        console.log(response)
        suggestions.value = response.data
      },
      () => {
        console.error('자동완성 실패!!')
        suggestions.value = []
      }
    )
  } else {
    suggestions.value = []
  }
}

const selectSuggestion = (suggestion) => {
  query.value = `${suggestion.house_name} (${suggestion.road_address})`
  suggestions.value = []
}

const fetchBlogPosts = () => {
  naverAPI.searchNews().then(response => {
    newsPosts.value = response.data.items
  }).catch(error => {
    console.error('뉴스 검색 실패!', error)
  })
}

onMounted(() => {
  fetchBlogPosts()
})
</script>

<template>
  <main>
    <div>
      <!-- Hero Section -->
      <section class="relative">
        <img src="/skyscraper.jpg" alt="Hero Background" class="w-full object-cover opacity-80 bg" />

        <!-- 어두운 오버레이 추가 -->
        <div class="absolute inset-0 bg-black opacity-50"></div>

        <div
          class="absolute inset-0 flex flex-col justify-center items-center text-center text-white"
        >
          <h1 class="text-4xl font-bold mb-4">오화랑 이찬민 취업열차 출발!</h1>
          <p class="text-lg mb-4">어떻게든 완성해야 한다! 부릉부릉!</p>

          <div class="relative w-80">
            <input
              type="text"
              v-model="query"
              @input="fetchSuggestions"
              placeholder="지역/주소, 건물명으로 검색하세요"
              class="w-full p-2 rounded-lg text-gray-700 border border-gray-300 h-12"
            />
            <button
              class="absolute right-0 top-0 h-full bg-sky-500 text-white py-2 px-4 rounded-r-lg hover:bg-sky-700 text-lg"
            >
              검색
            </button>
            <ul
              v-if="suggestions.length"
              class="absolute left-0 right-0 bg-white border border-gray-300 rounded mt-1 z-10"
            >
              <li
                v-for="(suggestion, index) in suggestions"
                :key="index"
                @mousedown.prevent="selectSuggestion(suggestion)"
                class="p-2 cursor-pointer hover:bg-gray-200"
              >
                {{ suggestion.house_name }} ({{ suggestion.road_address }})
              </li>
            </ul>
          </div>
        </div>
      </section>

      <!-- News and Detailed News Section -->
      <section class="flex flex-wrap justify-center space-x-6 my-8 text-lg">
        <!-- News List Section -->
        <div class="bg-white p-6 rounded-lg shadow-lg w-full md:w-1/2 mb-6 md:mb-0">
          <h2 class="text-2xl font-semibold mb-4">공지/뉴스</h2>
          <ul>
            <li 
              v-for="(post, index) in newsPosts.slice(0, 5)" 
              :key="index" 
              class="mb-2"
            >
              <a 
                :href="post.link" 
                target="_blank" 
                class="text-gray-800 hover:text-blue-700"
                v-html="DOMPurify.sanitize(post.title)"
              ></a>
              <span class="text-gray-500 ml-2">{{ new Date(post.pubDate).toLocaleDateString() }}</span>
            </li>
          </ul>
        </div>

        <!-- Detailed News Section -->
        <div 
          v-if="newsPosts.length > 5" 
          class="w-full md:w-2/5 bg-white p-6 rounded-lg shadow-lg"
        >
          <a 
            :href="newsPosts[5].link" 
            target="_blank" 
            class="text-lg font-semibold mb-2 text-gray-800 hover:text-blue-700 block"
            v-html="DOMPurify.sanitize(newsPosts[5].title)"
          ></a>
          <p v-html="DOMPurify.sanitize(newsPosts[5].description)"></p>
          <div class="text-gray-500 text-right">{{ new Date(newsPosts[5].pubDate).toLocaleDateString() }}</div>
        </div>
      </section>
    </div>
  </main>
</template>

<style scoped>
.bg {
  height: calc(100vh - 180px);
}

@media (min-width: 768px) {
  .space-x-6 > :not(:last-child) {
    margin-right: 1.5rem;
  }
}
</style>
