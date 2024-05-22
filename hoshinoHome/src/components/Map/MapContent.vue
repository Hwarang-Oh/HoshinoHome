<script setup>
import { ref, onMounted, inject, defineProps, watch } from 'vue'
import MapSearch from './MapSearch.vue'
import ChatBot from '@/views/ChatView.vue'
const { initMap } = inject('service')

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
    <MapSearch />

    <button class="chatbot-button" @click="toggleChatBot">
      <img src="/chatboticon2.png" alt="ChatBot" class="w-full h-full object-cover" />
    </button>

    <!-- ChatBot Modal -->
    <div v-if="isChatBotOpen" class="chatbot-modal">
      <div class="chatbot-header">
        <span>ChatBot</span>
        <button class="close-button" @click="toggleChatBot">
          <i class="mdi mdi-close"></i>
        </button>
      </div>
      <div class="chatbot-content">
        <ChatBot />
      </div>
    </div>
  </div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 100%;
}
.map-content {
  position: absolute;
  height: 100%;
}

/* ChatBot Button 스타일 */
.chatbot-button {
  position: fixed;
  bottom: 40px;
  right: 40px;
  border: 1px solid #979797;
  background-color: #ffffff;
  border-radius: 50%;
  width: 60px;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s;
  z-index: 1000;
}

.chatbot-button:hover {
  background-color: #e7e7e7;
}

/* ChatBot Modal 스타일 */
.chatbot-modal {
  position: fixed;
  bottom: 80px;
  right: 20px;
  width: 350px;
  height: 500px;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.chatbot-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ccc;
  font-size: 20px;
  font-weight: bold;
}

.close-button {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.chatbot-content {
  padding: 10px;
  flex: 1;
  overflow-y: auto;
}
</style>

<style>
@import url('https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/css/materialdesignicons.min.css');
@import url('https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css');
</style>
