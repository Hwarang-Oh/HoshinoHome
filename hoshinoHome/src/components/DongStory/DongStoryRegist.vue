<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import dongStoryAPI from '@/api/dongStory.js'
import dongAPI from '@/api/dong.js'
import axios from 'axios'
import Swal from 'sweetalert2'

const dongStory = reactive({
  dong_code: '',
  dong_name: '',
  title: '',
  content: '',
  date: new Date(),
  user_id: '',
  user_name: ''
})

const router = useRouter()
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

const fetchUserInfo = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      Swal.fire({
        icon: 'error',
        title: '로그인이 필요합니다.',
        showConfirmButton: true,
        confirmButtonText: '확인'
      }).then(() => {
        router.push('/login') // 로그인 페이지로 이동
      })
      return
    }

    const response = await axios.get('http://localhost:8080/auth/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    const userData = response.data
    dongStory.user_id = userData.user_id
    dongStory.user_name = userData.user_name
  } catch (error) {
    console.error('사용자 정보를 불러오는 데 실패했습니다.', error)
  }
}

const registerDongStory = () => {
  const selectedDong = dongNames.value.find(dong => dong.dong_name === dongStory.dong_name)
  if (selectedDong) {
    dongStory.dong_code = selectedDong.dong_code
    dongStoryAPI.addDongStory(
      dongStory,
      () => {
        router.push({ name: 'dongStory' })
      },
      (error) => {
        console.error('Dong Story 데이터를 등록하는 데 실패했습니다.', error)
      }
    )
  } else {
    console.error('유효한 지역 이름을 선택하세요.')
  }
}

onMounted(() => {
  fetchDongNames()
  fetchUserInfo()
})
</script>

<template>
  <div class="min-h-screen bg-blue-50 py-8">
    <div class="max-w-2xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-lg">
      <h1 class="text-2xl font-bold text-blue-600 text-center mb-6">Dong Story 생성</h1>
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
            @click="registerDongStory()"
            class="bg-gradient-to-r from-blue-400 to-blue-600 hover:from-blue-500 hover:to-blue-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
          >
            등록하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
