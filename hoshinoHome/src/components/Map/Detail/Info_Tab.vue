<template>
  <div class="info-section mb-4">
    <div class="flex items-center mb-2">
      <span class="info-tag">토지</span>
      <p class="text-sm text-gray-700 ml-2">{{ selectedDealVo.area }}</p>
    </div>
    <div class="flex items-center mb-2">
      <span class="info-tag">건물</span>
      <p class="text-sm text-gray-700 ml-2">
        {{ selectedHouse.house_name + ' (' + selectedHouse.build_year + ')' }}
      </p>
    </div>
  </div>
  <div class="tabs-section border-b border-gray-200 mb-4">
    <nav class="flex space-x-4">
      <button class="tab-button active" aria-current="page">실거래</button>
      <button class="tab-button">주변 정보</button>
      <button class="tab-button" @click="goToDongStory">Dong Story</button>
    </nav>
  </div>
</template>

<script setup>
import { inject } from 'vue'
import { useRouter } from 'vue-router'
const { selectedHouse, selectedDealVo } = inject('res')
const router = useRouter()

const goToDongStory = () => {
  if (selectedHouse.value.dong_name) {
    router.push({ name: 'dongStory', query: { dongName: selectedHouse.value.dong_name } })
  } else {
    console.error('dong_name is not defined')
  }
}
</script>

<style scoped>
.info-section {
  margin-bottom: 1rem;
}

.info-tag {
  font-size: 0.75rem;
  font-weight: 500;
  background-color: #e5e7eb;
  color: #374151;
  border-radius: 9999px;
  padding: 0.25rem 0.5rem;
  margin-right: 0.5rem;
}
.tabs-section {
  border-bottom: 1px solid #e5e7eb;
  margin-bottom: 1rem;
}

.tab-button {
  padding: 0.5rem 1rem;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: border-color 0.2s;
}

.tab-button.active {
  color: #1d4ed8;
  border-color: #1d4ed8;
}
</style>
