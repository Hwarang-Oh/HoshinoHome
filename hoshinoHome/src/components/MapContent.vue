<script setup>
import { onMounted, inject } from 'vue'
const { initMap, changeSize, displayMarker, displayInfoWindow } = inject('service')

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
  <!-- <div>
    <div class="button-group">
      <button @click="changeSize(0)">Hide</button>
      <button @click="changeSize(400)">Show</button>
      <button @click="displayMarker(markerPositions1)">Marker Set 1</button>
      <button @click="displayMarker(markerPositions2)">Marker Set 2</button>
      <button @click="displayMarker([])">Marker Set 3 (Empty)</button>
      <button @click="displayInfoWindow">Infowindow</button>
    </div>
  </div> -->
</template>

<style scoped>
#map {
  width: 100%;
  height: 100%; /* Full height of the container */
}
</style>
