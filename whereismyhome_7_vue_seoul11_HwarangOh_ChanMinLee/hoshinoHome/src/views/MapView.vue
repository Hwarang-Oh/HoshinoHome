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
import { RouterView, useRouter } from 'vue-router'
import mapAPI from '@/api/map.js'
import MapContent from '@/components/Map/MapContent.vue'

// Define data as `refs`
// resources in Map View
const router = useRouter()
let map = reactive({})
let clusterer = ref(null)
const dealVoList = ref([]) // To store all fetched data
const detailDealList = ref([])
const selectedHouse = ref({})
const activeButtons = ref(['apartment', 'multiFamily', 'officetel', 'sale', 'lease', 'monthly'])
const selectedDealVo = ref({}) // To store the selected dealVo


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

  clusterer.value = new kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: true,
    minLevel: 6
  })
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
      console.log(dealVoList.value)
      console.log('조건에 맞는 House의 대표값을 가져오는데 성공')
    },
    () => {
      console.log('조건에 맞는 실거래가 정보를 불러오는 데 실패했습니다.')
    }
  )
}

// 3 draw Overlay Marker
const formatAmount = (amount) => {
  const num = parseInt(amount.replace(/,/g, ''))
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '억'
  }
  return amount
}

const drawMarker = (dealVoList) => {
  let coords, customOverlay, content
  const houseTypeMap = { 1: '아파트', 2: '연립/다세대', 3: '오피스텔' }
  const houseTypeColors = { 1: '#2c3e50', 2: '#34495e', 3: '#3b5998' } // Adjusted colors with lower saturation
  const markers = []
 

  dealVoList.value.forEach((dealVo) => {
    coords = new kakao.maps.LatLng(dealVo.lat, dealVo.lng)
    console.log(dealVo.lat, dealVo.lng, dealVo.apartment_name)

    let price = ''
    if (dealVo.deal_type === 1) {
      price = `${formatAmount(dealVo.deal_amount)}`
    } else if (dealVo.deal_type === 2) {
      price = `${formatAmount(dealVo.deposit_amount)}`
    } else if (dealVo.deal_type === 3) {
      price = `${formatAmount(dealVo.deposit_amount)} / ${dealVo.monthly_amount}`
    }

    // Custom Overlay
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
        <div class="custom-overlay-area">${dealVo.area} m²</div>
      </div>`

    customOverlay = new kakao.maps.CustomOverlay({
      map: map,
      position: coords,
      content: content,
      yAnchor: 1
    })

    content.addEventListener('click', () => {
      handleOverlayClick(content)
      getHouseDealList(dealVo.house_code)
      selectedDealVo.value = dealVo
      router.push(`/map/houseDetail/${dealVo.house_code}`)
    })

    markers.push(customOverlay)
  })
  clusterer.value.addMarkers(markers)
}

const handleOverlayClick = (content) => {
  if (selectedOverlay && selectedOverlay !== content) {
    selectedOverlay.classList.remove('selected')
  }
  if (selectedOverlay === content) {
    selectedOverlay.classList.remove('selected')
    selectedOverlay = null
  } else {
    content.classList.add('selected')
    selectedOverlay = content
  }
}

// 3.1 Custom Overlay Style
const style = document.createElement('style')
style.innerHTML = `
  .custom-overlay {
    position: relative;
    width: 70px; /* Smaller width */
    height: 70px; /* Smaller height */
    border-radius: 6px;
    box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.1);
    text-align: center;
    font-size: 10px; /* Smaller font size */
    color: black; /* Black text color */
    white-space: nowrap;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    transition: width 0.3s, height 0.3s, font-size 0.3s; /* Smooth transition */
  }
  .custom-overlay.selected {
    width: 100px; /* Larger width */
    height: 100px; /* Larger height */
    font-size: 12px; /* Larger font size */
  }
  .custom-overlay-top {
    padding: 4px 0; /* Increased padding */
    border-top-left-radius: 6px;
    border-top-right-radius: 6px;
    color: white; /* White text for the top part */
  }
  .custom-overlay-title {
    font-weight: bold;
    color: #FFD700; /* Yellow color */
    font-size: 12px; /* Adjusted font size */
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .custom-overlay.selected .custom-overlay-title {
    font-size: 14px; /* Larger font size when selected */
  }
  .custom-overlay-price {
    font-size: 12px; /* Adjusted font size */
  }
  .custom-overlay.selected .custom-overlay-price {
    font-size: 14px; /* Larger font size when selected */
  }
  .custom-overlay-bottom {
    background: #fff; /* White background */
    padding: 4px 0; /* Increased padding */
    border-bottom-left-radius: 6px;
    border-bottom-right-radius: 6px;
    color: #000; /* Black text for the bottom part */
  }
  .custom-overlay-year {
    font-size: 10px; /* Smaller font size */
    font-weight: bold;
  }
  .custom-overlay.selected .custom-overlay-year {
    font-size: 12px; /* Larger font size when selected */
  }
  .custom-overlay-area {
    font-size: 10px; /* Smaller font size */
  }
  .custom-overlay.selected .custom-overlay-area {
    font-size: 12px; /* Larger font size when selected */
  }
`
document.head.appendChild(style)

// 3.2 drawList에 대한 Watch -> draw Overlay Marker 실행
watch(dealVoList, () => {
  drawMarker(dealVoList)
})

// 4. Marker Click Event Function => Which will get detailDealList of that House Code
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

// 5. DealList Search Button Group

// 6. DealList Search Input

// 8. Map House Detail Implementation Part -> 어쩌면 House Detail에서 전부 할 수도 있음.
// 8.1 House Detail 창에서 close를 누르면, Map으로 돌아온다. ( 매개변수 조절을 통해, 다른 곳에서도 활용이 가능함 )
const close = () => {
  router.push('/map')
}

provide('res', {
  map: readonly(map),
  activeButtons: activeButtons,
  dealVoList: readonly(dealVoList),
  detailDealList: readonly(detailDealList),
  selectedHouse: readonly(selectedHouse),
  selectedDealVo: readonly(selectedDealVo)
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
