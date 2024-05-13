<template>
  <div class="relative h-screen">
    <MapContent />
    <MapList />
  </div>
</template>

<style scoped>
body,
html {
  height: 100%;
  margin: 0;
}
</style>

<script setup>
import MapContent from '@/components/MapContent.vue'
import { ref, reactive, provide, readonly, watch } from 'vue'
import mapAPI from '@/api/map.js'
import MapList from '@/components/MapList.vue'

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

// resources in Map View
let map = reactive({})
let markers = ref([])
let infowindow = ref(null)
const dealList = ref([])

// 1. 지도를 생성하는 Code
const initMap = () => {
  const container = document.getElementById('map')
  const options = {
    // center Location
    center: new kakao.maps.LatLng(37.56813, 127.00213),
    level: 5
  }
  // 지도 타입 변경 Control 요소 정의
  let mapTypeControl = new kakao.maps.MapTypeControl()
  let zoomControl = new kakao.maps.ZoomControl()

  // Initialize the map
  map = new kakao.maps.Map(container, options)
  map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT)
  map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT)

  // const geocoder = new kakao.maps.services.Geocoder()
  // let currentCodes = new Set()
  kakao.maps.event.addListener(map, 'bounds_changed', function () {
    if (isMoving) return // 지도 이동 중이라면 함수 실행 중지
    drawApts() // 아파트 정보 그리기 함수 호출
  })

  let isMoving = false
  kakao.maps.event.addListener(map, 'drag', function () {
    isMoving = true
  })

  kakao.maps.event.addListener(map, 'dragend', function () {
    isMoving = false
    drawApts()
  })
}

// 2. 지도 Hide & Show
const changeSize = (size) => {
  const container = document.getElementById('map')
  container.style.width = `${size}px`
  container.style.height = `${size}px`
  map.value.relayout()
}

// 3. Get HouseDealList
const drawApts = () => {
  let range = {
    lngFrom: map.getBounds().getSouthWest().getLng().toString(),
    lngTo: map.getBounds().getNorthEast().getLng().toString(),
    latFrom: map.getBounds().getSouthWest().getLat().toString(),
    latTo: map.getBounds().getNorthEast().getLat().toString()
  }
  mapAPI.getDealList(
    range,
    (response) => {
      console.log(response.data)
      dealList.value = response.data
      console.log(dealList.value)
    },
    () => {
      console.log('실거래가 정보를 불러오는 데 실패했습니다.')
    }
  )
}

// 4. draw Overlay Marker
const drawMarker = (dealList) => {
  let coords, marker
  dealList.value.forEach((deal) => {
    coords = new kakao.maps.LatLng(deal.lat, deal.lng)
    marker = new kakao.maps.Marker({
      map: map,
      position: coords
    })
    console.log(deal.lat, deal.lng, deal.apartment_name)
  })
  // 인포윈도우로 장소에 대한 설명을 표시합니다
  infowindow = new kakao.maps.InfoWindow({
    content: `<div style="width:150px;text-align:center;padding:6px 0;">범위 세대</div>`
  })
  infowindow.open(map, marker)
}

// 4.1 drawList가 변하면, draw Overlay Marker 실행
watch(dealList, () => {
  drawMarker(dealList)
})

// 추후 개발
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
          map: map,
          position
        })
    )

    const bounds = positions.reduce(
      (bounds, latlng) => bounds.extend(latlng),
      new kakao.maps.LatLngBounds()
    )

    map.setBounds(bounds)
  }
}

const displayInfoWindow = () => {
  if (infowindow.value && infowindow.value.getMap()) {
    // Move the map to the position of the infowindow
    map.setCenter(infowindow.value.getPosition())
    return
  }

  const iwContent = '<div style="padding:5px;">Hello World!</div>' // Content for the infowindow
  const iwPosition = new kakao.maps.LatLng(33.450701, 126.570667) // Position of the infowindow
  const iwRemovable = true // Allow closing the infowindow

  infowindow.value = new kakao.maps.InfoWindow({
    map: map, // Associate the infowindow with the map
    position: iwPosition,
    content: iwContent,
    removable: iwRemovable
  })
  map.setCenter(iwPosition)
}

provide('res', {
  map: readonly(map),
  dealList: readonly(dealList),
  infowindow: readonly(infowindow),
  markers: readonly(markers),
  markerPositions1,
  markerPositions2
})

provide('service', {
  // 등록 수정 삭제는 해당 Method를 통해 isReload값을 바꿀 것이다!!
  initMap,
  changeSize,
  displayMarker,
  displayInfoWindow
})
</script>
