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

let map = reactive({})
let clusterer = ref(null)
const router = useRouter()
const dealVoList = ref([]) // To store all fetched data
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
    latTo: map.getBounds().getNorthEast().getLat().toString(),
    houseTypes: [1],
    dealTypes: [3]
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

// 4.0 draw Overlay Marker
const formatAmount = (amount) => {
  const num = parseInt(amount.replace(/,/g, ''))
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '억'
  }
  return amount
}

const drawMarker = (dealVoList) => {
  let coords, marker, customOverlay, content
  const houseTypeMap = { 1: '아파트', 2: '연립/다세대', 3: '오피스텔' }
  const dealTypeMap = { 1: '매매', 2: '전세', 3: '월세' }
  const markers = []

  dealVoList.value.forEach((dealVo) => {
    coords = new kakao.maps.LatLng(dealVo.lat, dealVo.lng)
    console.log(dealVo.lat, dealVo.lng, dealVo.apartment_name)

    let price = ''
    if (dealVo.deal_type === 1) {
      price = `매매: ${formatAmount(dealVo.deal_amount)}`
    } else if (dealVo.deal_type === 2) {
      price = `전세: ${formatAmount(dealVo.deposit_amount)}`
    } else if (dealVo.deal_type === 3) {
      price = `월세: ${formatAmount(dealVo.deposit_amount)} / ${dealVo.monthly_amount}`
    }

    // Custom Overlay
    content = document.createElement('div')
    content.innerHTML = `
      <div class="custom-overlay">
        <div class="custom-overlay-top">
          <div class="custom-overlay-title">${houseTypeMap[dealVo.house_type]}</div>
          <div class="custom-overlay-price">${price}</div>
        </div>
        <div class="custom-overlay-bottom">
          <div class="custom-overlay-year">${dealVo.deal_year}년 ${dealVo.deal_month}월</div>
          <div class="custom-overlay-area">${dealVo.area} m²</div>
        </div>
      </div>`

    customOverlay = new kakao.maps.CustomOverlay({
      map: map,
      position: coords,
      content: content,
      yAnchor: 1
    })

    content.addEventListener('click', () => {
      getHouseDealList(dealVo.house_code)
      router.push(`/map/houseDetail/${dealVo.house_code}`)
    })
    markers.push(customOverlay)
  })
  clusterer.value.addMarkers(markers)
}

// 4.1 Custom Overlay Style
const style = document.createElement('style')
style.innerHTML = `
  .custom-overlay {
    position: relative;
    width: 70px; /* Adjusted width */
    border-radius: 6px;
    box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.1);
    text-align: center;
    font-size: 10px; /* Smaller font size */
    color: #000; /* Black text color */
    white-space: nowrap;
  }
  .custom-overlay-top {
    background: #00C853; /* Green background */
    padding: 2px 0; /* Adjusted padding */
    border-top-left-radius: 6px;
    border-top-right-radius: 6px;
    border-bottom: 2px solid #00C853; /* Green bottom border */
    color: white; /* White text for the top part */
  }
  .custom-overlay-title {
    font-weight: bold;
    margin-bottom: 2px;
  }
  .custom-overlay-price {
    font-size: 12px; /* Smaller font size */
  }
  .custom-overlay-bottom {
    background: #fff; /* White background */
    padding: 2px 0; /* Adjusted padding */
    border-bottom-left-radius: 6px;
    border-bottom-right-radius: 6px;
    border-top: 2px solid #00C853; /* Green top border */
    color: #000; /* Black text for the bottom part */
  }
  .custom-overlay-year,
  .custom-overlay-area {
    font-size: 8px; /* Smaller font size */
  }
`
document.head.appendChild(style)

// 4.2 drawList에 대한 Watch -> draw Overlay Marker 실행
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

// 6. DealList Search Button Group

// 7. DealList Search Input

// 8. Map House Detail Implementation Part -> 어쩌면 House Detail에서 전부 할 수도 있음.
// 8.1 House Detail 창에서 close를 누르면, Map으로 돌아온다. ( 매개변수 조절을 통해, 다른 곳에서도 활용이 가능함 )
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

onMounted(() => {
  const script = document.createElement('script')
  script.src =
    '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4aa94d500c252274b8dc18944a4026f5&libraries=clusterer'
  document.head.appendChild(script)
})
</script>
