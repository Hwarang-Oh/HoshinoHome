<template>
  <div class="relative h-screen">
    <MapContent :isHouseDetailOpen="isHouseDetailOpen" />
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
import { RouterView, useRouter } from 'vue-router'
import { useUserInfoStore } from '@/stores/UserInfoStore.js'
import mapAPI from '@/api/map.js'
import MapContent from '@/components/Map/MapContent.vue'
import '@/assets/CustomOverlay.css' // Import the external CSS file

// Define data as `refs`
// resources in Map View
const userInfoStore = useUserInfoStore()
const router = useRouter()
let map = reactive({})
const dealVoList = ref([]) // To store all fetched data
const detailDealList = ref([])
const selectedHouse = ref({})
const activeButtons = ref(['apartment', 'multiFamily', 'officetel', 'sale', 'lease', 'monthly'])
const selectedDealVo = ref({}) // To store the selected dealVo
const isHouseDetailOpen = ref(false) // 상태를 추가하여 House Detail 창이 열렸는지 여부를 관리
const createdMarkers = new Set()

// 1. Main 지도를 생성하는 Code
const initMap = () => {
  const container = document.getElementById('map')
  const options = {
    // center Location
    center: new kakao.maps.LatLng(userInfoStore.userLocation.lat, userInfoStore.userLocation.lng),
    level: 4
  }
  // 지도 타입 변경 Control 요소 정의
  let mapTypeControl = new kakao.maps.MapTypeControl()
  let zoomControl = new kakao.maps.ZoomControl()

  // Initialize the map
  map = new kakao.maps.Map(container, options)
  map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT)
  map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT)

  drawApts()
  // Moving Map and get House Deal Data
  kakao.maps.event.addListener(map, 'bounds_changed', function () {
    if (isMoving) return // 지도 이동 중이라면 함수 실행 중지
    drawApts() // Filter and display deals within the current bounds
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

// 2. Get HouseDealList
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

  mapAPI.getHouseDealVoList2(
    range,
    (response) => {
      dealVoList.value = response.data
      // 검색된 HouseInfo가 있는 경우, dealVoList에 추가
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
              dealVoList.value.push(response.data)
              drawMarker(dealVoList)
              selectedHouse.value = userInfoStore.searchedHouseInfo
              selectedDealVo.value = response.data
              getHouseDealList(response.data.house_code)
                .then(() => {
                  selectedDealVo.value = response.data
                  isHouseDetailOpen.value = true
                })
                .catch((error) => {
                  console.error(error)
                })
              userInfoStore.searchedHouseInfo = null
            },
            () => {
              console.log('검색된 House의 실거래가 정보를 불러오는 데 실패했습니다.')
            }
          )
        }
      }
      console.log('조건에 맞는 House의 대표값을 가져오는데 성공')
    },
    () => {
      console.log('조건에 맞는 실거래가 정보를 불러오는 데 실패했습니다.')
    }
  )
}

// 3 draw Custom Overlay Marker
const formatAmount = (amount) => {
  const num = parseInt(amount.replace(/,/g, ''))
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '억'
  }
  return num.toLocaleString() // Use locale string for better formatting
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
    // 마커가 이미 생성되었는지 확인
    if (createdMarkers.has(dealVo.house_code)) return

    coords = new kakao.maps.LatLng(dealVo.lat, dealVo.lng)
    console.log(dealVo.lat, dealVo.lng, dealVo.apartment_name)

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
      selectedOverlay = customOverlay
    }

    content.addEventListener('click', () => {
      handleOverlayClick(customOverlay)
      getHouseDealList(dealVo.house_code)
        .then(() => {
          selectedDealVo.value = dealVo
          isHouseDetailOpen.value = true
          router.push(`/map/houseDetail/${dealVo.house_code}`)
        })
        .catch((error) => {
          console.error(error)
        })
    })

    // 마커 생성 후 set에 추가
    createdMarkers.add(dealVo.house_code)
  })
}

let selectedOverlay = null
const handleOverlayClick = (overlay) => {
  if (selectedOverlay && selectedOverlay !== overlay) {
    const selectedContent = selectedOverlay.getContent()
    selectedContent.classList.remove('selected')
  }
  if (selectedOverlay === overlay) {
    const selectedContent = selectedOverlay.getContent()
    selectedContent.classList.remove('selected')
    selectedOverlay = null
  } else {
    const overlayContent = overlay.getContent()
    overlayContent.classList.add('selected')
    selectedOverlay = overlay
  }
}

// 3.1 drawList에 대한 Watch -> draw Overlay Marker 실행
watch(dealVoList, () => {
  drawMarker(dealVoList)
})

// 4. Marker Click Event Function => Which will get detailDealList of that House Code
const getHouseDealList = (house_code) => {
  return new Promise((resolve, reject) => {
    mapAPI.getHouseInfo(
      house_code,
      (response) => {
        selectedHouse.value = response.data
        console.log(response.data)
      },
      () => {
        console.log(`해당 집의 기본 정보를 불러오지 못했어요.`)
        reject('Failed to fetch house info')
      }
    ),
      mapAPI.getHouseDealList(
        house_code,
        (response) => {
          detailDealList.value = response.data
          console.log(response.data)
          resolve('Fetched data successfully')
        },
        () => {
          console.error('해당 집의 거래내역을 불러오지 못했어요.')
          reject('Failed to fetch deal list')
        }
      )
  })
}

// 6. Map House Detail Implementation Part -> 어쩌면 House Detail에서 전부 할 수도 있음.
// 8.1 House Detail 창에서 close를 누르면, Map으로 돌아온다. ( 매개변수 조절을 통해, 다른 곳에서도 활용이 가능함 )
const close = () => {
  if (selectedOverlay) {
    const selectedContent = selectedOverlay.getContent()
    selectedContent.classList.remove('selected')
    selectedOverlay = null
  }
  router.push('/map')
}

provide('res', {
  map: readonly(map),
  activeButtons: activeButtons,
  dealVoList: readonly(dealVoList),
  detailDealList: readonly(detailDealList),
  selectedHouse: readonly(selectedHouse),
  selectedDealVo: readonly(selectedDealVo),
  isHouseDetailOpen: readonly(isHouseDetailOpen)
})

provide('service', {
  initMap,
  close
})

onMounted(() => {
  const script = document.createElement('script')
  script.src =
    '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4aa94d500c252274b8dc18944a4026f5&libraries=clusterer'
  document.head.appendChild(script)
})
</script>
