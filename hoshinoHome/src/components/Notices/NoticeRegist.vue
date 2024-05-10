
<script>
export default {
  name: 'NoticeCreate',
  data() {
    return {
      title: '',
      content: ''
    };
  },
  methods: {
    submitNotice() {
      // Handle the notice creation logic
      console.log('Submitted Title:', this.title);
      console.log('Submitted Content:', this.content);
    }
  }
};
</script>


<template>
    <div class="max-w-2xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-lg">
      <h1 class="text-2xl font-bold text-green-600 text-center mb-6">공지사항 생성</h1>
      <div class="space-y-4">
        <div>
          <label for="title" class="block text-lg font-semibold mb-1 text-green-700">제목</label>
          <input
            id="title"
            v-model="title"
            type="text"
            placeholder="제목을 입력하세요"
            class="w-full p-3 border border-green-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-600 focus:border-transparent transition duration-200"
          />
        </div>
        <div>
          <label for="content" class="block text-lg font-semibold mb-1 text-green-700">내용</label>
          <textarea
            id="content"
            v-model="content"
            rows="6"
            placeholder="내용을 입력하세요"
            class="w-full p-3 border border-green-200 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-600 focus:border-transparent transition duration-200"
  
      ></textarea>
    </div>
    <div class="flex justify-end mt-6">
      <!-- Submit Button -->
      <button
        class="bg-gradient-to-r from-blue-500 to-purple-500 hover:from-blue-600 hover:to-purple-600 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
      >
        제출하기
      </button>
    </div>
  </div>
    </div>
  </template>
  

  
  
  <style scoped></style>