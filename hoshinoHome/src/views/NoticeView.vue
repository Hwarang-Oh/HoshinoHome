<script>
import { ref, computed, onMounted } from 'vue'
import noticeAPI from '@/api/notice.js'
import { useRouter } from 'vue-router'
import { useNoticesStore } from '@/stores/noticesStore'

export default {
  name: 'NoticePage',
  setup() {
    const notices = ref([])

    const fetchNotices = () => {
      noticeAPI.getNoticeList(
        (response) => {
          notices.value = response.data
        },
        (error) => {
          console.error('공지사항 데이터를 불러오는 데 실패했습니다.', error)
        }
      )
    }

    onMounted(fetchNotices)

    const itemsPerPage = 5
    const currentPage = ref(1)

    // Calculate the total number of pages
    const totalPages = computed(() => Math.ceil(notices.value.length / itemsPerPage))

    // Get the notices to display for the current page
    const paginatedNotices = computed(() => {
      const startIndex = (currentPage.value - 1) * itemsPerPage
      return notices.value.slice(startIndex, startIndex + itemsPerPage)
    })

    // Go to the previous page
    const prevPage = () => {
      if (currentPage.value > 1) currentPage.value -= 1
    }

    // Go to the next page
    const nextPage = () => {
      if (currentPage.value < totalPages.value) currentPage.value += 1
    }

    // Go to a specific page
    const goToPage = (page) => {
      if (page >= 1 && page <= totalPages.value) currentPage.value = page
    }

    // -------------- 게시글 클릭 시 ----------

    const router = useRouter()
    const noticesStore = useNoticesStore()

    const toNoticeDetail = (id) => {
      const notice = notices.value.find((notice) => notice.post_id === id)
      if (notice) {
        noticesStore.setSelectedNotice(notice)
        router.push({ name: 'noticeDetail', params: { id } })
      }
    }

    const toNoticeRegist = () => {
      router.push({ name: 'noticeRegist' })
    }

    return {
      currentPage,
      totalPages,
      paginatedNotices,
      prevPage,
      nextPage,
      goToPage,
      toNoticeDetail,
      toNoticeRegist
    }
  }
}
</script>

<template>
  <div class="min-h-screen bg-gray-100 py-8">
    <!-- Main Notice Content Section -->
    <div class="max-w-4xl mx-auto bg-white p-8 rounded-lg shadow-lg">
      <h1 class="text-3xl font-bold text-center text-green-600 mb-8">공지사항</h1>

      <!-- Notice Table -->
      <table class="w-full border-collapse table-fixed text-left mb-8">
        <thead>
          <tr class="bg-green-100 text-green-700">
            <th class="w-3/4 py-2 px-4 border-b border-green-200">제목</th>
            <th class="w-1/4 py-2 px-4 border-b border-green-200">등록일</th>
          </tr>
        </thead>
        <tbody>
          <!-- Loop over notices to populate table rows -->
          <tr
            v-for="(notice, index) in paginatedNotices"
            :key="notice.post_id"
            class="hover:bg-green-50"
            @click="toNoticeDetail(notice.post_id)"
          >
            <td class="py-2 px-4 border-b border-gray-200">{{ notice.title }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ notice.date }}</td>
          </tr>
        </tbody>
      </table>

      <!-- Register Button -->
      <div class="text-right mb-8">
        <button
          @click="toNoticeRegist"
          class="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
        >
          등록하기
        </button>
      </div>

      <!-- Pagination Controls -->
      <nav class="flex justify-center space-x-2">
        <button
          @click="prevPage"
          :disabled="currentPage === 1"
          class="px-3 py-1 border rounded-full hover:bg-green-200 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          &lt;
        </button>
        <span
          v-for="page in totalPages"
          :key="page"
          @click="goToPage(page)"
          :class="
            page === currentPage
              ? 'bg-green-500 text-white'
              : 'bg-white text-green-700 hover:bg-green-200'
          "
          class="px-3 py-1 border rounded-full cursor-pointer"
        >
          {{ page }}
        </span>
        <button
          @click="nextPage"
          :disabled="currentPage === totalPages"
          class="px-3 py-1 border rounded-full hover:bg-green-200 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          &gt;
        </button>
      </nav>
    </div>
  </div>
</template>

<style scoped></style>
