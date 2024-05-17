<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useNoticesStore } from '@/stores/noticesStore'
import noticeAPI from '@/api/notice.js'
import axios from 'axios'

const noticesStore = useNoticesStore()
const selectedNotice = ref(noticesStore.selectedNotice)
const isAdmin = ref(false)

const router = useRouter()

const toNoticeModify = (id) => {
  router.push({ name: 'noticeModify', params: { id } })
}

const removeNotice = (id) => {
  noticeAPI.removeNotice(
    id,
    () => {
      // 성공시
      router.push({ name: 'notice' })
    },
    (error) => {
      // 실패시
      console.error('공지사항 데이터를 삭제하는 데 실패했습니다.', error)
    }
  )
}

const toNoticeList = () => {
  router.push({ name: 'notice' })
}

const checkAdmin = async () => {
  try {
    const token = localStorage.getItem('token')
    const response = await axios.get('http://localhost:8080/auth/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    isAdmin.value = response.data.user_type === '1234'
  } catch (error) {
    console.error('사용자 정보를 불러오지 못했습니다.', error)
  }
}

onMounted(() => {
  checkAdmin()
})
</script>

<template>
  <div class="min-h-screen bg-gray-100 py-8">
    <div class="max-w-2xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-lg">
      <h1 class="text-2xl font-bold text-green-600 text-center mb-6">공지사항 상세</h1>
      <div class="bg-green-100 text-green-700 py-2 px-4 rounded-md mb-4">
        <div class="flex justify-between">
          <span>제목</span>
          <span>등록일</span>
        </div>
        <div class="flex justify-between mt-2">
          <span>{{ selectedNotice.title }}</span>
          <span>{{ selectedNotice.date }}</span>
        </div>
      </div>
      <div class="mt-4">
        <p class="text-gray-700">{{ selectedNotice.content }}</p>
      </div>
      <div class="mt-6 flex justify-end space-x-4">
        <!-- List Button -->
        <button
          @click="toNoticeList"
          class="bg-gradient-to-r from-green-400 to-green-600 hover:from-green-500 hover:to-green-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
        >
          목록
        </button>
        <template v-if="isAdmin">
          <!-- Modify Button -->
          <button
            @click="toNoticeModify(selectedNotice.post_id)"
            class="bg-gradient-to-r from-orange-400 to-orange-600 hover:from-orange-500 hover:to-orange-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
          >
            수정하기
          </button>
          <!-- Delete Button -->
          <button
            @click="removeNotice(selectedNotice.post_id)"
            class="bg-gradient-to-r from-orange-600 to-red-600 hover:from-red-500 hover:to-orange-800 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
          >
            삭제하기
          </button>
        </template>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
