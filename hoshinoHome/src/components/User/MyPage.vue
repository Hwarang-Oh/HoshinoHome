<template>
    <div>
      <h2 class="text-2xl font-bold text-center text-green-600 mb-6">내 정보</h2>
      <div class="space-y-4">
        <div>
          <label class="block text-gray-700">아이디:</label>
          <p class="border border-gray-300 p-3 rounded-lg">{{ user.user_name }}</p>
        </div>
        <div>
          <label class="block text-gray-700">주소:</label>
          <input v-model="user.user_address" class="border border-gray-300 p-3 rounded-lg w-full">
        </div>
        <div>
          <label class="block text-gray-700">관심 지역:</label>
          <input v-model="user.user_favorite_place" class="border border-gray-300 p-3 rounded-lg w-full">
        </div>
        <div>
          <label class="block text-gray-700">관리자 코드:</label>
          <input v-model="user.user_type" class="border border-gray-300 p-3 rounded-lg w-full">
        </div>
      </div>
      <div class="flex justify-center">
        <button @click="updateUserInfo" class="mt-4 bg-green-600 text-white py-2 px-4 rounded-lg hover:bg-green-700">
          수정
        </button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  
  const user = ref({
    user_name: '',
    user_address: '',
    user_favorite_place: '',
    user_type: ''
  });
  
  onMounted(async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/auth/me', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      user.value = response.data;
    } catch (error) {
      alert('사용자 정보를 불러오지 못했습니다.');
    }
  });
  
  const updateUserInfo = async () => {
    try {
      const token = localStorage.getItem('token');
      await axios.put('http://localhost:8080/auth/me', user.value, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      alert('사용자 정보가 성공적으로 업데이트되었습니다.');
    } catch (error) {
      alert('사용자 정보 업데이트에 실패했습니다.');
    }
  };
  </script>
  
  <style scoped>
  body {
    background-color: #f3f4f6;
  }
  </style>
  