<template>
  <div>
    <h2 class="text-2xl font-bold text-center text-green-600 mb-6">로그인</h2>
    <form @submit.prevent="login" class="space-y-4">
      <input v-model="loginInfo.user_name" type="text" placeholder="Id" required
             class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-600">
      <input v-model="loginInfo.user_password" type="password" placeholder="Password" required
             class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-600">
      <button type="submit" class="w-full bg-green-600 text-white py-3 rounded-lg font-semibold hover:bg-green-700">
        로그인
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

const loginInfo = ref({
  user_name: '',
  user_password: ''
});

const login = async () => {
  try {
    const response = await axios.post('http://localhost:8080/auth/login', loginInfo.value);
    localStorage.setItem('token', response.data);
    alert('로그인 성공!');
    emit('close'); // 부모 컴포넌트에 close 이벤트 전송
    router.push('/').then(() => {
      window.location.reload(); // 홈 화면으로 이동 후 새로고침
    });
  } catch (error) {
    alert('로그인 실패!');
  }
};

const emit = defineEmits(['close']);
</script>
