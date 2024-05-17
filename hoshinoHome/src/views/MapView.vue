<template>
  <div class="relative h-screen">
    <MapContent />
    <router-view />
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
import { ref, reactive, watch, provide, readonly } from 'vue'
import { RouterView, useRouter } from 'vue-router'
import mapAPI from '@/api/map.js'
import MapContent from '@/components/Map/MapContent.vue'
import MapList from '@/components/Map/HouseDetail.vue'

// Define data as `refs`
// resources in Map View

let map = reactive({})
let markers = ref([])
let infowindow = ref(null)
const router = useRouter()
const dealVoList = ref([])
const detailDealList = ref([])
const selectedHouse = ref({})

// 1. Main 지도를 생성하는 Code
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
  mapAPI.getHouseDealVoList(
    range,
    (response) => {
      dealVoList.value = response.data
      console.log('조건에 맞는 House의 대표값을 가져오는데 성공')
    },
    () => {
      console.log('조건에 맞는 실거래가 정보를 불러오는 데 실패했습니다.')
    }
  )
}

// 4. draw Overlay Marker
const drawMarker = (dealVoList) => {
  let coords, marker
  dealVoList.value.forEach((dealVo) => {
    coords = new kakao.maps.LatLng(dealVo.lat, dealVo.lng)
    marker = new kakao.maps.Marker({
      map: map,
      position: coords
    })
    console.log(dealVo.lat, dealVo.lng, dealVo.apartment_name)
    kakao.maps.event.addListener(marker, 'click', () => {
      getHouseDealList(dealVo.house_code)
      router.push(`/map/houseDetail/${dealVo.house_code}`)
    })
  })
  // 인포윈도우로 장소에 대한 설명을 표시합니다
  infowindow = new kakao.maps.InfoWindow({
    content: `<div style="width:150px;text-align:center;padding:6px 0;">범위 세대</div>`
  })
  infowindow.open(map, marker)
}

// 4.1 drawList에 대한 Watch -> draw Overlay Marker 실행
watch(dealVoList, () => {
  drawMarker(dealVoList)
})

// 5. Marker Click Event Function => Which will get detailDealList of that House Code
const getHouseDealList = (house_code) => {
  mapAPI.getHouseInfo(
    house_code,
    (response) => {
      selectedHouse.value = response.data
      console.log(response.data)
    },
    () => {
      console.log(`해당 집의 기본 정보를 불러오지 못했어요.`)
    }
  ),
    mapAPI.getHouseDealList(
      house_code,
      (response) => {
        detailDealList.value = response.data
        console.log(response.data)
      },
      () => {
        console.error('해당 집의 거래내역을 불러오지 못했어요.')
      }
    )
}

// 6. Map House Detail Implementation Part -> 어쩌면 House Detail에서 전부 할 수도 있음.
// 6.1 House Detail 창에서 close를 누르면, Map으로 돌아온다. ( 매개변수 조절을 통해, 다른 곳에서도 활용이 가능함 )
const close = () => {
  router.push('/map')
}

provide('res', {
  map: readonly(map),
  dealVoList: readonly(dealVoList),
  detailDealList: readonly(detailDealList),
  selectedHouse: readonly(selectedHouse)
})

provide('service', {
  initMap,
  changeSize,
  close
})
</script>

<!-- 추후 개발
/*
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
*/ -->
