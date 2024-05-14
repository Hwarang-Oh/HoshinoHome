<template>
  <div>
    <h2 class="text-2xl font-bold text-center text-green-600 mb-6">회원가입</h2>
    <form @submit.prevent="register" class="space-y-4">
      <input v-model="user.user_name" type="text" placeholder="Id" required
             class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-600">
      <input v-model="user.user_password" type="password" placeholder="Password" required
             class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-600">
      <input v-model="user.user_address" type="text" placeholder="주소"
             class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-600">
      <input v-model="user.user_favorite_place" type="text" placeholder="관심 지역"
             class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-600">
      <input v-model="user.user_type" type="text" placeholder="관리자 코드"
             class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-600">
      <button type="submit" class="w-full bg-green-600 text-white py-3 rounded-lg font-semibold hover:bg-green-700">
        회원가입
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

const user = ref({
  user_name: '',
  user_password: '',
  user_address: '',
  user_favorite_place: '',
  user_type: ''
});

const register = async () => {
  try {
    const response = await axios.post('http://localhost:8080/auth/register', user.value);
    alert('회원가입 성공!');
    router.push('/'); // 홈 화면으로 이동
  } catch (error) {
    alert('회원가입 실패!');
  }
};
</script>

<style scoped>
body {
  background-color: #f3f4f6;
}
</style>
