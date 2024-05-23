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
import { ref, reactive, watch, onMounted, provide, readonly } from 'vue'
import { useRouter } from 'vue-router'
import { useUserInfoStore } from '@/stores/UserInfoStore.js'
import mapAPI from '@/api/map.js'
import MapContent from '@/components/Map/MapContent.vue'
import '@/assets/CustomOverlay.css' // Import the external CSS file

const router = useRouter()
const userInfoStore = useUserInfoStore()
let map = reactive({})
const dealVoList = ref([]) // To store all fetched data
const activeButtons = ref(['apartment', 'multiFamily', 'officetel', 'sale', 'lease', 'monthly'])
const createdMarkers = new Map() // Set에서 Map으로 변경하여 house_code와 overlay를 매핑
const favoritePlaces = ref(new Set()) // 관심 주거지 목록
const selectedOverlay = ref(null) // 선택된 오버레이

const initMap = () => {
  const container = document.getElementById('map')
  const options = {
    center: new kakao.maps.LatLng(userInfoStore.userLocation.lat, userInfoStore.userLocation.lng),
    level: 4
  }
  let mapTypeControl = new kakao.maps.MapTypeControl()
  let zoomControl = new kakao.maps.ZoomControl()

  map = new kakao.maps.Map(container, options)
  map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT)
  map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT)

  drawApts()
  kakao.maps.event.addListener(map, 'bounds_changed', function () {
    if (isMoving) return
    drawApts()
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

const drawApts = () => {
  const houseTypes = []
  const dealTypes = []

  if (activeButtons.value.includes('apartment')) houseTypes.push(1)
  if (activeButtons.value.includes('multiFamily')) houseTypes.push(2)
  if (activeButtons.value.includes('officetel')) houseTypes.push(3)

  if (activeButtons.value.includes('sale')) dealTypes.push(1)
  if (activeButtons.value.includes('lease')) dealTypes.push(2)
  if (activeButtons.value.includes('monthly')) dealTypes.push(3)

  let range = {
    lngFrom: map.getBounds().getSouthWest().getLng().toString(),
    lngTo: map.getBounds().getNorthEast().getLng().toString(),
    latFrom: map.getBounds().getSouthWest().getLat().toString(),
    latTo: map.getBounds().getNorthEast().getLat().toString(),
    houseTypes: houseTypes,
    dealTypes: dealTypes
  }
  userInfoStore.setUserLocation({
    lat: (parseFloat(range.latFrom) + parseFloat(range.latTo)) / 2,
    lng: (parseFloat(range.lngFrom) + parseFloat(range.lngTo)) / 2
  })
  mapAPI.getHouseDealVoList2(
    range,
    (response) => {
      dealVoList.value = response.data
      if (userInfoStore.searchedHouseInfo) {
        const existing = dealVoList.value.find(
          (dealVo) => dealVo.house_code === userInfoStore.searchedHouseInfo.house_code
        )
        if (!existing) {
          let searchedHouse = {
            houseCode: userInfoStore.searchedHouseInfo.house_code,
            houseTypes: houseTypes,
            dealTypes: dealTypes
          }
          mapAPI.getEachHouseDealVo(
            searchedHouse,
            (response) => {
              userInfoStore.setSelectedHouseDealVo(response.data)
              dealVoList.value.push(response.data)
              drawMarker(dealVoList)
              router.push(`/map/houseDetail/${searchedHouse.houseCode}`)
              userInfoStore.searchedHouseInfo = null
            },
            () => {
              console.log('검색한 House의 실거래가 정보를 불러오는 데 실패했습니다.')
            }
          )
        }
      }
      console.log('조건에 맞는 House의 최근 거래를 가져오는데 성공했습니다')
    },
    () => {
      console.log('조건에 맞는 실거래가 정보를 불러오는 데 실패했습니다.')
    }
  )
}

const fetchFavoritePlaces = () => {
  mapAPI.getFavoritePlaces(
    (response) => {
      favoritePlaces.value = new Set(response.data)
    },
    (error) => {
      console.error('Failed to fetch favorite places:', error)
    }
  )
}

const formatAmount = (amount) => {
  const num = parseInt(amount.replace(/,/g, ''))
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '억'
  }
  return num.toLocaleString()
}

const formatArea = (area) => {
  return parseFloat(area).toFixed(2)
}

const drawMarker = (dealVoList) => {
  let coords, content
  const houseTypeMap = { 1: '아파트', 2: '연립/다세대', 3: '오피스텔' }
  const houseTypeColors = { 1: '#001f3f', 2: '#003366', 3: '#005f7f' }
  const dealTypeMap = { 1: '매매', 2: '전세', 3: '월세' }

  dealVoList.value.forEach((dealVo) => {
    if (createdMarkers.has(dealVo.house_code)) return

    coords = new kakao.maps.LatLng(dealVo.lat, dealVo.lng)

    let price = ''
    let dealTypeText = ''
    if (dealVo.deal_type === 1) {
      price = `${formatAmount(dealVo.deal_amount)}`
      dealTypeText = dealTypeMap[1]
    } else if (dealVo.deal_type === 2) {
      price = `${formatAmount(dealVo.deposit_amount)}`
      dealTypeText = dealTypeMap[2]
    } else if (dealVo.deal_type === 3) {
      price = `${formatAmount(dealVo.deposit_amount)} / ${dealVo.monthly_amount}`
      dealTypeText = dealTypeMap[3]
    }

    content = document.createElement('div')
    content.className = 'custom-overlay'
    content.style.backgroundColor = houseTypeColors[dealVo.house_type]
    content.innerHTML = `
      <div class="custom-overlay-top">
        <div class="custom-overlay-title">
          ${houseTypeMap[dealVo.house_type]}
        </div>
        <div class="custom-overlay-price">${price}</div>
        ${favoritePlaces.value.has(dealVo.house_code) ? '<i class="fas fa-heart favorite-icon"></i>' : ''}
      </div>
      <div class="custom-overlay-bottom">
        <div class="custom-overlay-year">${dealVo.deal_year} - ${dealVo.deal_month}</div>
        <div class="custom-overlay-area">${formatArea(dealVo.area)} m²</div>
      </div>
      <div class="custom-overlay-type">${dealTypeText}</div>`

    const customOverlay = new kakao.maps.CustomOverlay({
      map: map,
      position: coords,
      content: content,
      yAnchor: 1
    })

    if (
      userInfoStore.searchedHouseInfo &&
      userInfoStore.searchedHouseInfo.house_code === dealVo.house_code
    ) {
      content.classList.add('selected')
      selectedOverlay.value = customOverlay
    }

    content.addEventListener('click', () => {
      handleOverlayClick(customOverlay)
      userInfoStore.setSelectedHouseDealVo(dealVo)
      router.push(`/map/houseDetail/${dealVo.house_code}`)
    })

    // 마커 생성 후 set에 추가
    createdMarkers.set(dealVo.house_code, customOverlay)
  })
}

// 4.1 Marker Click Event Functions
const handleOverlayClick = (overlay) => {
  if (selectedOverlay.value && selectedOverlay.value !== overlay) {
    const selectedContent = selectedOverlay.value.getContent()
    selectedContent.classList.remove('selected')
  }
  if (selectedOverlay.value === overlay) {
    const selectedContent = selectedOverlay.value.getContent()
    selectedContent.classList.remove('selected')
    selectedOverlay.value = null
  } else {
    const overlayContent = overlay.getContent()
    overlayContent.classList.add('selected')
    selectedOverlay.value = overlay
  }
}

watch(dealVoList, () => {
  drawMarker(dealVoList)
})

watch(activeButtons, () => {
  drawApts()
})

const close = () => {
  if (selectedOverlay.value) {
    const selectedContent = selectedOverlay.value.getContent()
    selectedContent.classList.remove('selected')
    selectedOverlay.value = null
  }
  userInfoStore.closeHouseDetail()
  router.push('/map')
}

provide('res', {
  map: readonly(map),
  activeButtons: activeButtons,
  dealVoList: readonly(dealVoList),
  favoritePlaces: readonly(favoritePlaces), // favoritePlaces 제공
  selectedOverlay, // selectedOverlay 제공
  createdMarkers // createdMarkers 제공
})

provide('service', {
  initMap,
  close,
  drawApts,
  drawMarker,
  handleOverlayClick
})

onMounted(() => {
  const script = document.createElement('script')
  script.src =
    '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4aa94d500c252274b8dc18944a4026f5&libraries=clusterer'
  document.head.appendChild(script)
  fetchFavoritePlaces()
})
</script>
