<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <main>
    <div>
      <!-- Hero Section -->
      <section class="relative">
        <img src="/skyscraper.jpg" alt="Hero Background" class="w-full object-cover opacity-80" />

        <!-- 어두운 오버레이 추가 -->
        <div class="absolute inset-0 bg-black opacity-50"></div>

        <div
          class="absolute inset-0 flex flex-col justify-center items-center text-center text-white"
        >
          <h1 class="text-4xl font-bold mb-4">오화랑 이찬민 취업열차 출발!</h1>
          <p class="text-lg mb-4">어떻게든 완성해야 한다! 부릉부릉!</p>

          <div class="flex">
            <input
              type="text"
              placeholder="지역, 지하철, 건물명, 학교명으로 검색하세요"
              class="w-80 p-2 rounded-lg text-gray-700 border border-gray-300 h-12"
            />
            <button
              class="bg-sky-500 text-white py-2 px-4 rounded-lg hover:bg-sky-700 text-lg ml-3 h-12"
            >
              검색
            </button>
          </div>
        </div>
      </section>

      <!-- Feature Cards Section -->
      <section class="flex justify-center space-x-6 my-8 text-lg">
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
    </div>
  </main>
</template>
