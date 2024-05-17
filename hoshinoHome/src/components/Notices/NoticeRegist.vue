<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import noticeAPI from '@/api/notice.js'

const notice = reactive({
  title: '',
  content: '',
  date: new Date().toISOString().slice(0, 10) // YYYY-MM-DD 형식으로 설정
})
const router = useRouter()

const registNotice = () => {
  noticeAPI.registerNotice(
    notice,
    () => {
      //성공시
      router.push({ name: 'notice' })
    },
    (error) => {
      //실패시
      console.error('공지사항 데이터를 등록하는 데 실패했습니다.', error)
    }
  )
}
</script>

<template>
  <div class="min-h-screen bg-gray-100 py-8">
    <div class="max-w-2xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-lg">
      <h1 class="text-2xl font-bold text-green-600 text-center mb-6">공지사항 생성</h1>
      <div class="space-y-4">
        <div>
          <label for="title" class="block text-lg font-semibold mb-1 text-green-700">제목</label>
          <input
            id="title"
            :value="notice.title"
            @input="notice.title = $event.target.value"
            type="text"
            placeholder="제목을 입력하세요"
            class="w-full p-3 border border-green-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-600 focus:border-transparent transition duration-200"
          />
        </div>
        <div>
          <label for="content" class="block text-lg font-semibold mb-1 text-green-700">내용</label>
          <textarea
            id="content"
            :value="notice.content"
            @input="notice.content = $event.target.value"
            rows="6"
            placeholder="내용을 입력하세요"
            class="w-full p-3 border border-green-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-600 focus:border-transparent transition duration-200"
          ></textarea>
        </div>
        <div class="flex justify-end mt-6">
          <!-- Submit Button -->
          <button
            @click="registNotice()"
            class="bg-gradient-to-r from-green-400 to-green-600 hover:from-green-500 hover:to-green-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
          >
            등록하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
