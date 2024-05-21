<script setup>
import { ref, onMounted, inject, defineProps } from 'vue'
import MapSearch from './MapSearch.vue'
const { initMap } = inject('service')
const props = defineProps({
  isHouseDetailOpen: Boolean
})

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
  </div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 100%; /* Full height of the container */
}
</style>
