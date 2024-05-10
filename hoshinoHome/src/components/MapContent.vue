<!--
  * 가독성을 위해 Template Style Script로 Code 순서를 변경함!
-->

<template>
  <div>
    <div id="map"></div>
    <div class="button-group">
      <button @click="changeSize(0)">Hide</button>
      <button @click="changeSize(400)">Show</button>
      <button @click="displayMarker(markerPositions1)">Marker Set 1</button>
      <button @click="displayMarker(markerPositions2)">Marker Set 2</button>
      <button @click="displayMarker([])">Marker Set 3 (Empty)</button>
      <button @click="displayInfoWindow">Infowindow</button>
    </div>
  </div>
</template>

<style scoped>
#map {
  width: 70%;
  height: 1000px;
}

.button-group {
  margin: 10px 0px;
}

button {
  margin: 0 3px;
}
</style>

<script setup>
import { ref, onMounted } from 'vue'

// Define data as `refs`
const markerPositions1 = [
  [33.452278, 126.567803],
  [33.452671, 126.574792],
  [33.451744, 126.572441]
]
const markerPositions2 = [
  [37.499590490909185, 127.0263723554437],
  [37.499427948430814, 127.02794423197847],
  [37.498553760499505, 127.02882598822454],
  [37.497625593121384, 127.02935713582038],
  [37.49629291770947, 127.02587362608637],
  [37.49754540521486, 127.02546694890695],
  [37.49646391248451, 127.02675574250912]
]

const markers = ref([])
const infowindow = ref(null)
const map = ref(null)

// 1. 지도를 생성하는 Code
const initMap = () => {
  const container = document.getElementById('map')
  const options = {
    center: new kakao.maps.LatLng(37.56813, 127.00213),
    level: 5
  }
  // 지도 타입 변경 Control 요소 정의
  let mapTypeControl = new kakao.maps.MapTypeControl()
  let zoomControl = new kakao.maps.ZoomControl()

  // Initialize the map
  map.value = new kakao.maps.Map(container, options)
  map.value.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT)
  map.value.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT)
}

// 2. 지도의 크기를 변경하는 Code => Hide / Show 기능에 사용
const changeSize = (size) => {
  const container = document.getElementById('map')
  container.style.width = `${size}px`
  container.style.height = `${size}px`
  map.value.relayout()
}

const displayMarker = (markerPositions) => {
  // Clear existing markers
  if (markers.value.length > 0) {
    markers.value.forEach((marker) => marker.setMap(null))
  }

  const positions = markerPositions.map((position) => new kakao.maps.LatLng(...position))

  if (positions.length > 0) {
    markers.value = positions.map(
      (position) =>
        new kakao.maps.Marker({
          map: map.value,
          position
        })
    )

    const bounds = positions.reduce(
      (bounds, latlng) => bounds.extend(latlng),
      new kakao.maps.LatLngBounds()
    )

    map.value.setBounds(bounds)
  }
}

const displayInfoWindow = () => {
  if (infowindow.value && infowindow.value.getMap()) {
    // Move the map to the position of the infowindow
    map.value.setCenter(infowindow.value.getPosition())
    return
  }

  const iwContent = '<div style="padding:5px;">Hello World!</div>' // Content for the infowindow
  const iwPosition = new kakao.maps.LatLng(33.450701, 126.570667) // Position of the infowindow
  const iwRemovable = true // Allow closing the infowindow

  infowindow.value = new kakao.maps.InfoWindow({
    map: map.value, // Associate the infowindow with the map
    position: iwPosition,
    content: iwContent,
    removable: iwRemovable
  })

  map.value.setCenter(iwPosition)
}

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
