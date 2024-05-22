<script setup>
import { ref, inject, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import mapAPI from '@/api/map.js'
import Swal from 'sweetalert2'

const { selectedHouse, dealVoList, favoritePlaces } = inject('res')
const { close, drawApts, drawMarker, handleOverlayClick, getHouseDealList, selectedOverlay, createdMarkers } = inject('service')
const router = useRouter()

const isFavorite = ref(false)

const logFavoritePlaces = (response) => {
  console.log('Current favorite places:', response.data)
}

const checkFavoriteStatus = () => {
  mapAPI.getFavoritePlaces(
    (response) => {
      isFavorite.value = response.data.includes(selectedHouse.value.house_code)
    },
    (error) => {
      console.error('Failed to check favorite status:', error)
    }
  )
}

const findAndSetOverlay = (houseCode) => {
  if (createdMarkers.has(houseCode)) {
    selectedOverlay.value = createdMarkers.get(houseCode)
  }
}

const updateOverlay = (houseCode) => {
  findAndSetOverlay(houseCode)
  if (selectedOverlay.value) {
    const overlayContent = selectedOverlay.value.getContent()
    const favoriteIcon = overlayContent.querySelector('.favorite-icon')
    if (favoriteIcon) {
      if (isFavorite.value) {
        favoriteIcon.classList.add('fas', 'fa-heart')
      } else {
        favoriteIcon.classList.remove('fas', 'fa-heart')
      }
    } else if (isFavorite.value) {
      const newIcon = document.createElement('i')
      newIcon.classList.add('fas', 'fa-heart', 'favorite-icon')
      overlayContent.querySelector('.custom-overlay-top').appendChild(newIcon)
    }
  }
}

const toggleFavorite = () => {
  console.log(selectedHouse.value.house_code + " 선택된 하우스코드")

  mapAPI.toggleFavoritePlace(
    selectedHouse.value.house_code,
    (response) => {
      isFavorite.value = !isFavorite.value
      logFavoritePlaces(response)
      favoritePlaces.value = new Set(response.data) // 업데이트된 favoritePlaces를 반영
      updateOverlay(selectedHouse.value.house_code) // 오버레이 업데이트
    },
    (error) => {
      if (error.response && error.response.data === "Unable to toggle favorite place") {
        Swal.fire({
          icon: 'warning',
          title: '등록 제한',
          text: '관심 주거지는 최대 5개까지 등록가능합니다!'
        })
      } else {
        console.error('Failed to toggle favorite place:', error)
      }
    }
  )
}

onMounted(() => {
  checkFavoriteStatus()
})

// Watch for changes in selectedHouse to update favorite status
watch(() => selectedHouse.value.house_code, () => {
  checkFavoriteStatus()
})
</script>

<template>
  <div class="header-section mb-4">
    <div style="width: 100%;">
      <div class="flex items-center mb-2">
        <button class="px-2 py-1 bg-gray-300 text-blue-600 rounded">대표</button>
        <h1 class="ml-2 text-xl font-semibold">{{ selectedHouse.road_address }}</h1>
      </div>
      <div class="flex items-center justify-between">
        <p class="text-sm text-gray-600">{{ selectedHouse.jibun_address }}</p>
        <div class="flex items-center ml-auto">
          <i
            :class="['cursor-pointer', 'text-red-500']"
            @click="toggleFavorite"
          >
            <svg v-if="isFavorite" width="24" height="24" viewBox="0 0 24 24" fill="#FF0000" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41 0.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
            </svg>
            <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M16.5 3c-1.74 0-3.41 0.81-4.5 2.09C10.91 3.81 9.24 3 7.5 3 4.42 3 2 5.42 2 8.5c0 3.78 3.4 6.86 8.55 11.54L12 21.35l1.45-1.32C18.6 15.36 22 12.28 22 8.5 22 5.42 19.58 3 16.5 3z" stroke="#FF0000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </i>
          <button class="text-gray-500 hover:text-gray-700 ml-4" @click="close">
            <svg
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              ></path>
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.mt-2 {
  margin-top: 0.5rem;
}
</style>
