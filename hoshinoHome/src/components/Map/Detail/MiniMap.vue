<template>
  <div id="mini-map" class="submap-section relative w-full h-40 mb-4"></div>
</template>

<script setup>
import { ref, reactive, watch, onMounted, inject } from 'vue'
const { selectedHouse } = inject('res')
let miniMap = reactive({})
const marker = ref(null)

const initMiniMap = () => {
  const container = document.getElementById('mini-map')
  const options = {
    center: new kakao.maps.LatLng(selectedHouse.value.lat, selectedHouse.value.lng),
    level: 5
  }
  miniMap = new kakao.maps.Map(container, options)
  addMarker(options.center)
}

const addMarker = (position) => {
  if (marker.value) {
    marker.value.setMap(null)
  }
  marker.value = new kakao.maps.Marker({
    position: position,
    map: miniMap
  })
}

watch(
  () => selectedHouse.value,
  (newHouse) => {
    if (newHouse && newHouse.lat && newHouse.lng) {
      initMiniMap()
    }
  },
  { immediate: true }
)

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMiniMap()
  } else {
    const script = document.createElement('script')
    script.onload = () => kakao.maps.load(initMiniMap)
    script.src =
      '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4aa94d500c252274b8dc18944a4026f5'
    document.head.appendChild(script)
  }
})
</script>

<style scoped>
.submap-section {
  position: relative;
  width: 100%;
  height: 10rem;
  margin-bottom: 1rem;
}
</style>
