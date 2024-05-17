<script setup>
import { onMounted, inject } from 'vue'
const { initMap } = inject('service')

// Initialize the map after the component is mounted
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
  <div id="map" class="absolute inset-0 z-0"></div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 100%; /* Full height of the container */
}
</style>
