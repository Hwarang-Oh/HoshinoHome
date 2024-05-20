<template>
  <div class="min-h-screen bg-blue-50 py-8">
    <div class="max-w-4xl mx-auto bg-white p-8 rounded-lg shadow-lg">
      <h1 class="text-3xl font-bold text-center text-blue-600 mb-8">Dong Story</h1>
      
      <!-- 지역 선택 드롭다운 메뉴 -->
      <div class="mb-4">
        <label for="dongFilter" class="block text-lg font-semibold mb-1 ms-3 text-blue-700">지역 선택</label>
        <select id="dongFilter" v-model="selectedDong" @change="filterDongStories" class="w-1/10 p-3 border border-blue-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition duration-200">
          <option value="">전체</option>
          <option v-for="dong in dongNames" :key="dong.dong_code" :value="dong.dong_name">{{ dong.dong_name }}</option>
        </select>
      </div>

      <table class="w-full border-collapse table-fixed text-left mb-8">
        <thead>
          <tr class="bg-blue-100 text-blue-700">
            <th class="w-1/6 py-3 px-4 border-b border-blue-200 text-lg font-semibold">지역 이름</th>
            <th class="w-1/3 py-3 px-4 border-b border-blue-200 text-lg font-semibold">제목</th>
            <th class="w-1/6 py-3 px-4 border-b border-blue-200 text-lg font-semibold">작성자</th>
            <th class="w-1/6 py-3 px-4 border-b border-blue-200 text-lg font-semibold">등록일</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(story, index) in paginatedDongStories"
            :key="story.post_id"
            class="hover:bg-blue-50 cursor-pointer"
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
      <nav class="flex justify-center space-x-2 mb-8">
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

      <div class="text-right mb-8">
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
import { ref, onMounted, computed } from 'vue'
import dongStoryAPI from '@/api/dongStory.js' // DongStory API 임포트
import dongAPI from '@/api/dong.js' // Dong API 임포트
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'DongStoryView',
  setup() {
    const dongStories = ref([])
    const dongNames = ref([])
    const selectedDong = ref('')
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

    onMounted(() => {
      fetchDongStories()
      fetchDongNames()
      const dongName = route.query.dongName
      if (dongName) {
        selectedDong.value = dongName
      }
    })

    return {
      dongStories,
      dongNames,
      selectedDong,
      filteredDongStories,
      paginatedDongStories,
      currentPage,
      totalPages,
      prevPage,
      nextPage,
      goToPage,
      toDongStoryRegist,
      toDongStoryDetail
    }
  }
}
</script>

<style scoped>
th {
  text-align: left;
}

td {
  text-align: left;
  padding-left: 20px;
}
</style>
