<template>
  <div
    class="bg-white hover:bg-green-500 transition duration-300 shadow p-4 flex justify-between items-center"
  >
    <!-- Navigation Bar Content -->
    <div class="flex items-center">
      <img src="" alt="Logo" class="h-8" />
      <nav class="ml-8 space-x-4 text-gray-700">
        <a href="#" class="hover:underline text-white">심각리가격조회</a>
        <a href="#" class="hover:underline text-white">매물</a>
        <a href="#" class="hover:underline text-white">기업 매물</a>
        <a href="#" class="hover:underline text-white">인구/기업정보</a>
        <a href="#" class="hover:underline text-white">인사이드</a>
      </nav>
    </div>
    <div class="space-x-4 text-sm">
      <a href="#" class="hover:underline text-white">로그인</a>
      <a href="#" class="hover:underline text-white">회원가입</a>
      <a href="#" class="hover:underline text-white">고객센터</a>
      <a href="#" class="bg-blue-500 text-white py-1 px-3 rounded-full hover:bg-blue-600"
        >이용료 구매</a
      >
    </div>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
