<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import dongStoryAPI from '@/api/dongStory.js'
import dongAPI from '@/api/dong.js'
import { useDongStoriesStore } from '@/stores/dongStoriesStore'

const router = useRouter()
const route = useRoute()
const dongStoriesStore = useDongStoriesStore()
const selectedDongStory = ref(dongStoriesStore.selectedDongStory)

const dongStory = reactive({ ...selectedDongStory.value })
const dongNames = ref([])

const fetchDongNames = () => {
  dongAPI.getDongList(
    (response) => {
      dongNames.value = response.data
    },
    (error) => {
      console.error('Dong 데이터를 불러오는 데 실패했습니다.', error)
    }
  )
}

const modifyDongStory = () => {
  const selectedDong = dongNames.value.find(dong => dong.dong_name === dongStory.dong_name)
  if (selectedDong) {
    dongStory.dong_code = selectedDong.dong_code
    dongStoryAPI.modifyDongStory(
      dongStory,
      () => {
        router.push({ name: 'dongStory' })
      },
      (error) => {
        console.error('Dong Story 데이터를 수정하는 데 실패했습니다.', error)
      }
    )
  } else {
    console.error('유효한 지역 이름을 선택하세요.')
  }
}

onMounted(() => {
  fetchDongNames()
  const dongStoryId = route.params.id
  dongStory.post_id = parseInt(dongStoryId) // URL 파라미터에서 post_id 설정
  if (!selectedDongStory.value) {
    dongStoryAPI.getDongStoryDetail(dongStoryId, 
      (response) => {
        Object.assign(dongStory, response.data)
      },
      (error) => {
        console.error('Dong Story 데이터를 불러오는 데 실패했습니다.', error)
      })
  }
})
</script>

<template>
  <div class="min-h-screen bg-blue-50 py-8">
    <div class="max-w-2xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-lg">
      <h1 class="text-2xl font-bold text-blue-600 text-center mb-6">Dong Story 수정</h1>
      <div class="space-y-4">
        <div>
          <label for="dong_name" class="block text-lg font-semibold mb-1 text-blue-700">지역 이름</label>
          <input
            id="dong_name"
            v-model="dongStory.dong_name"
            list="dongNames"
            placeholder="지역 이름을 입력하세요"
            class="w-full p-3 border border-blue-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition duration-200"
          />
          <datalist id="dongNames">
            <option v-for="dong in dongNames" :key="dong.dong_code" :value="dong.dong_name"></option>
          </datalist>
        </div>
        <div>
          <label for="title" class="block text-lg font-semibold mb-1 text-blue-700">제목</label>
          <input
            id="title"
            v-model="dongStory.title"
            type="text"
            placeholder="제목을 입력하세요"
            class="w-full p-3 border border-blue-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition duration-200"
          />
        </div>
        <div>
          <label for="content" class="block text-lg font-semibold mb-1 text-blue-700">내용</label>
          <textarea
            id="content"
            v-model="dongStory.content"
            rows="6"
            placeholder="내용을 입력하세요"
            class="w-full p-3 border border-blue-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition duration-200"
          ></textarea>
        </div>
        <div class="flex justify-end mt-6">
          <!-- Submit Button -->
          <button
            @click="modifyDongStory"
            class="bg-gradient-to-r from-blue-500 to-purple-500 hover:from-blue-600 hover:to-purple-600 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
          >
            수정하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
