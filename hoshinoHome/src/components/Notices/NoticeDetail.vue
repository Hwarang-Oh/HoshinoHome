<!-- NoticeDetail.vue -->

<script setup>
import { watch, ref } from "vue";
import { useRouter } from 'vue-router'
import noticeAPI from "@/api/notice.js";
const props = defineProps(['id'])
const notice = ref({})
// const router = useRouter()

const getNotice = () => {
    noticeAPI.getNoticeDetail(
    props.id,
        ({ data }) => {
            (notice.value = data) 
        },
    () => console.log('공지 조회를 실패했습니다.')
  )
}


watch(
  () => props.id,
  () => {
    getNotice()
  },
  { immediate: true }
)


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
          <span>{{ notice.value.title }}</span>  
          <span>{{ notice.value.date }}</span>
        </div>
      </div>
      <div class="mt-4">
        <p class="text-gray-700">{{ notice.value.content }}</p>
      </div>
      <div class="mt-6 flex justify-end space-x-4">
        <!-- Modify Button -->
        <button
          class="bg-gradient-to-r from-orange-400 to-orange-600 hover:from-orange-500 hover:to-orange-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
        >
          수정하기
        </button>
        <!-- Delete Button -->
        <button
          class="bg-gradient-to-r from-orange-600 to-red-600 hover:from-red-500 hover:to-orange-800 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
        >
          삭제하기
        </button>
      </div>
    </div>
</div>
  </template>
  
  
 
  
  <style scoped></style>