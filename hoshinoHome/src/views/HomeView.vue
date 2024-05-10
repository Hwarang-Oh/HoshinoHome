<script setup></script>

<template>
  <main>
    <div>
      <!-- Header Section -->
      <header class="bg-gray-900 text-white py-4 px-6 flex justify-between items-center">
        <a href="#" class="text-xl font-bold">Logo</a>
        <nav class="space-x-4 flex">
          <a href="#" class="hover:underline">탐색지도</a>
          <a href="#" class="hover:underline">설명가이드</a>
          <a href="#" class="hover:underline">부동산현황</a>
          <a href="#" class="hover:underline">고객센터</a>
        </nav>
      </header>

      <!-- Hero Section -->
      <section class="relative">
        <img src="" alt="Hero Background" class="w-full h-96 object-cover opacity-80" />
        <div
          class="absolute inset-0 flex flex-col justify-center items-center text-center text-white"
        >
          <h1 class="text-4xl font-bold mb-4">탐색 플러스 정기구독 서비스 출시!</h1>
          <p class="mb-4">영역 지정, 초기 재개발·재건축 검토</p>
          <input
            type="text"
            placeholder="지역, 지하철, 건물명, 학교명으로 검색하세요"
            class="w-80 p-2 rounded-lg text-gray-700 border border-gray-300"
          />
          <button class="bg-blue-600 text-white py-2 px-4 mt-4 rounded-lg hover:bg-blue-700">
            검색
          </button>
        </div>
      </section>

      <!-- Feature Cards Section -->
      <section class="flex justify-center space-x-6 my-8">
        <!-- Card 1 -->
        <div class="w-1/3 bg-white p-6 rounded-lg shadow-lg">
          <h2 class="text-lg font-semibold mb-2">정기구독을 위한 특별한 혜택</h2>
          <p>부동산 투자, 개발 및 재건축 검토</p>
        </div>
        <!-- Card 2 -->
        <div class="w-1/3 bg-white p-6 rounded-lg shadow-lg">
          <h2 class="text-lg font-semibold mb-2">빌딩탐색 & 자산관리 전문관</h2>
          <p>부동산 매매부터 관리까지</p>
        </div>
        <!-- Card 3 -->
        <div class="w-1/3 bg-white p-6 rounded-lg shadow-lg">
          <h2 class="text-lg font-semibold mb-2">부동산정보 앱도 다같이!</h2>
          <p>손쉽게 찾아보는 부동산 정보</p>
        </div>
      </section>

      <!-- Footer Section -->
      <footer class="bg-gray-900 text-white py-4 px-6 mt-8">
        <div class="flex justify-between items-center">
          <p>&copy; 2024 MyCompany</p>
          <div class="space-x-4">
            <a href="#" class="hover:underline">Privacy Policy</a>
            <a href="#" class="hover:underline">Terms of Service</a>
            <a href="#" class="hover:underline">Contact</a>
          </div>
        </div>
      </footer>
    </div>
  </main>
</template>
