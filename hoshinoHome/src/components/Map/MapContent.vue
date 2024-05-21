<script setup>
import { ref, onMounted, inject, defineProps } from 'vue'
import MapSearch from './MapSearch.vue'
import ChatBot from '@/views/ChatView.vue' // ChatBot 컴포넌트를 임포트합니다
const { initMap } = inject('service')
const props = defineProps({
  isHouseDetailOpen: Boolean
})

const isChatBotOpen = ref(false)

const toggleChatBot = () => {
  isChatBotOpen.value = !isChatBotOpen.value
}

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap()
  } else {
    const script = document.createElement('script')
    script.onload = () => kakao.maps.load(initMap)
    script.src =
      '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4aa94d500c252274b8dc18944a4026f5'
    document.head.appendChild(script)
  }
})
</script>

<template>
  <div id="map" class="relative inset-0 z-0">
    <MapSearch :isHouseDetailOpen="isHouseDetailOpen" />

    <button class="chatbot-button" @click="toggleChatBot">
      <i class="fas fa-comments"></i> ChatBot
    </button>

    <!-- ChatBot Modal -->
    <div v-if="isChatBotOpen" class="chatbot-modal">
      <button class="close-button" @click="toggleChatBot">X</button>
      <div class="chatbot-content">
        <ChatBot />
      </div>
    </div>
  </div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 100%; /* Full height of the container */
}
.map-content {
  position: relative;
  height: 100%;
}

/* ChatBot Button 스타일 */
.chatbot-button {
  position: fixed; /* fixed로 변경하여 항상 화면에 고정 */
  bottom: 40px;
  right: 40px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s;
  z-index: 1000; /* 다른 요소 위에 표시되도록 z-index 추가 */
}

.chatbot-button:hover {
  background-color: #0056b3;
}

/* ChatBot Modal 스타일 */
.chatbot-modal {
  position: fixed; /* fixed로 변경하여 항상 화면에 고정 */
  bottom: 80px;
  right: 20px;
  width: 350px;
  height: 500px;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000; /* 다른 요소 위에 표시되도록 z-index 추가 */
  display: flex;
  flex-direction: column;
}

.close-button {
  align-self: flex-end;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin: 5px;
}

.chatbot-content {
  padding: 10px;
  flex: 1;
  overflow-y: auto;
}
</style>
