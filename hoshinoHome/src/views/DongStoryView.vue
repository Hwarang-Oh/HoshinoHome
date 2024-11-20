<template>
  <div class="min-h-screen bg-blue-50 py-8 animate-fade-slide-in">
    <div class="max-w-4xl mx-auto bg-white p-8 rounded-lg shadow-lg animate-fade-slide-in">
      <h1 class="text-3xl font-bold text-center text-blue-600 mb-8 animate-fade-slide-in">Dong Story</h1>
      
      <!-- 지역 선택 드롭다운 및 검색창 -->
      <div class="mb-4 relative w-2/5 animate-fade-slide-in">
        <label for="dongFilter" class="block text-lg font-semibold mb-1 ms-3 text-blue-700">지역 선택</label>
        <div class="flex items-center">
          <input 
            v-if="isSearchMode"
            id="dongFilter"
            v-model="searchQuery"
            @input="filterDongNames"
            @keydown.down.prevent="moveDown"
            @keydown.up.prevent="moveUp"
            @keydown.enter.prevent="selectActiveDong"
            class="w-full p-3 border border-blue-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition duration-200"
            placeholder="지역 이름을 입력하세요..."
          />
          <select v-else id="dongFilter" v-model="selectedDong" @change="filterDongStories" class="w-full p-3 border border-blue-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition duration-200">
            <option value="">전체</option>
            <option v-for="dong in dongNames" :key="dong.dong_code" :value="dong.dong_name">{{ dong.dong_name }}</option>
          </select>
          <button @click="toggleSearchMode" class="ml-2 p-3 border border-blue-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition duration-200 bg-white hover:bg-blue-100">
            🔍
          </button>
        </div>
        <ul v-if="isSearchMode && filteredDongNames.length > 0" class="absolute z-10 w-full bg-white border border-blue-200 rounded-lg shadow-lg mt-1 max-h-60 overflow-y-auto">
          <li 
            v-for="(dong, index) in filteredDongNames" 
            :key="dong.dong_code" 
            @click="selectDong(dong.dong_name)" 
            :class="{'bg-blue-100': index === activeIndex}"
            class="px-4 py-2 cursor-pointer hover:bg-blue-50"
          >
            {{ dong.dong_name }}
          </li>
        </ul>
      </div>

      <table class="w-full border-collapse table-fixed text-left mb-8 animate-fade-slide-in">
        <thead>
          <tr class="bg-blue-100 text-blue-700">
            <th class="w-1/6 py-3 px-4 border-b border-blue-200 text-lg font-semibold">지역 이름</th>
            <th class="w-1/3 py-3 px-4 border-b border-blue-200 text-lg font-semibold">제목</th>
            <th class="w-1/6 py-3 px-4 border-b border-blue-200 text-lg font-semibold">작성자</th>
            <th class="w-1/6 py-3 px-4 border-b border-blue-200 text-lg font-semibold pdLeft">등록일</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(story, index) in paginatedDongStories"
            :key="story.post_id"
            class="hover:bg-blue-50 cursor-pointer animate-fade-slide-in"
            @click="toDongStoryDetail(story.post_id)"
          >
            <td class="py-3 px-4 border-b border-gray-200">{{ story.dong_name }}</td>
            <td class="py-3 px-6 border-b border-gray-200">{{ story.title }}</td>
            <td class="py-3 px-4 border-b border-gray-200">{{ story.user_name }}</td>
            <td class="py-3 px-4 border-b border-gray-200">{{ new Date(story.date).toLocaleDateString() }}</td>
          </tr>
        </tbody>
      </table>

      <!-- Pagination Controls -->
      <nav class="flex justify-center space-x-2 mb-8 animate-fade-slide-in">
        <button
          @click="prevPage"
          :disabled="currentPage === 1"
          class="px-3 py-1 border rounded-full hover:bg-blue-200 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          &lt;
        </button>
        <span
          v-for="page in totalPages"
          :key="page"
          @click="goToPage(page)"
          :class="page === currentPage ? 'bg-blue-500 text-white' : 'bg-white text-blue-700 hover:bg-blue-200'"
          class="px-3 py-1 border rounded-full cursor-pointer"
        >
          {{ page }}
        </span>
        <button
          @click="nextPage"
          :disabled="currentPage === totalPages"
          class="px-3 py-1 border rounded-full hover:bg-blue-200 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          &gt;
        </button>
      </nav>

      <div class="text-right mb-8 animate-fade-slide-in">
        <button
          @click="toDongStoryRegist"
          class="bg-gradient-to-r from-blue-400 to-blue-600 hover:from-blue-500 hover:to-blue-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
        >
          등록하기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue'
import dongStoryAPI from '@/api/dongStory.js' // DongStory API 임포트
import dongAPI from '@/api/dong.js' // Dong API 임포트
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'DongStoryView',
  setup() {
    const dongStories = ref([])
    const dongNames = ref([])
    const selectedDong = ref('')
    const searchQuery = ref('')
    const filteredDongNames = ref([])
    const activeIndex = ref(-1)
    const isSearchMode = ref(false)
    const router = useRouter()
    const route = useRoute()

    const currentPage = ref(1)
    const itemsPerPage = 10

    const fetchDongStories = () => {
      dongStoryAPI.getDongStoryList(
        (response) => {
          if (Array.isArray(response.data)) {
            dongStories.value = response.data.sort((a, b) => new Date(b.date) - new Date(a.date))
          } else {
            console.error('Dong Story 데이터를 불러오는 데 실패했습니다. 데이터 형식이 올바르지 않습니다.')
          }
        },
        (error) => {
          console.error('Dong Story 데이터를 불러오는 데 실패했습니다.', error)
        }
      )
    }

    const fetchDongNames = () => {
      dongAPI.getDongList(
        (response) => {
          dongNames.value = response.data.filter(dong => dong.dong_name !== null)
        },
        (error) => {
          console.error('Dong 데이터를 불러오는 데 실패했습니다.', error)
        }
      )
    }

    const toDongStoryRegist = () => {
      router.push({ name: 'dongStoryRegist' })
    }

    const toDongStoryDetail = (id) => {
      router.push({ name: 'dongStoryDetail', params: { id } })
    }

    const filterDongStories = () => {
      if (selectedDong.value) {
        return dongStories.value.filter(story => story.dong_name === selectedDong.value)
      }
      return dongStories.value
    }

    const filterDongNames = () => {
      if (searchQuery.value) {
        filteredDongNames.value = dongNames.value.filter(dong =>
          dong.dong_name.toLowerCase().includes(searchQuery.value.toLowerCase())
        )
        activeIndex.value = -1 // 검색어가 변경될 때마다 activeIndex를 초기화
      } else {
        filteredDongNames.value = []
      }
    }

    const filteredDongStories = computed(() => filterDongStories())

    const paginatedDongStories = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage
      return filteredDongStories.value.slice(start, start + itemsPerPage)
    })

    const totalPages = computed(() => Math.ceil(filteredDongStories.value.length / itemsPerPage))

    const prevPage = () => {
      if (currentPage.value > 1) currentPage.value -= 1
    }

    const nextPage = () => {
      if (currentPage.value < totalPages.value) currentPage.value += 1
    }

    const goToPage = (page) => {
      if (page >= 1 && page <= totalPages.value) currentPage.value = page
    }

    const toggleSearchMode = () => {
      isSearchMode.value = !isSearchMode.value
      if (!isSearchMode.value) {
        searchQuery.value = ''
        filteredDongNames.value = []
      }
    }

    const selectDong = (dongName) => {
      selectedDong.value = dongName
      searchQuery.value = dongName
      filteredDongNames.value = []
      isSearchMode.value = false
    }

    const moveDown = () => {
      if (activeIndex.value < filteredDongNames.value.length - 1) {
        activeIndex.value++
      }
    }

    const moveUp = () => {
      if (activeIndex.value > 0) {
        activeIndex.value--
      }
    }

    const selectActiveDong = () => {
      if (activeIndex.value >= 0 && activeIndex.value < filteredDongNames.value.length) {
        selectDong(filteredDongNames.value[activeIndex.value].dong_name)
      }
    }

    onMounted(() => {
      fetchDongStories()
      fetchDongNames()
      const dongName = route.query.dongName
      if (dongName) {
        selectedDong.value = dongName
      }
    })

    watch(searchQuery, filterDongNames)

    return {
      dongStories,
      dongNames,
      selectedDong,
      searchQuery,
      filteredDongNames,
      filteredDongStories,
      paginatedDongStories,
      currentPage,
      totalPages,
      prevPage,
      nextPage,
      goToPage,
      toDongStoryRegist,
      toDongStoryDetail,
      isSearchMode,
      toggleSearchMode,
      selectDong,
      activeIndex,
      moveDown,
      moveUp,
      selectActiveDong
    }
  }
}
</script>

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
  animation: fade-slide-in 0.5s ease-out;
}

th {
  text-align: left;
}

td {
  text-align: left;
  padding-left: 20px;
}

.pdLeft {
  padding-left: 20px;
}

.bg-blue-100 {
  background-color: #ebf8ff;
}
</style>
