import { defineStore } from 'pinia'

export const useUserInfoStore = defineStore('userInfo', {
  state: () => ({
    userLocation: {
      lat: 37.5012767241426,
      lng: 127.039600248343
    },
    searchedHouseInfo: null, // 추가된 상태
    selectedHouseDealVo: null,
    isHouseDetailOpen: false
  }),
  actions: {
    setUserLocation(location) {
      this.userLocation = location
    },
    setSearchedHouseInfo(houseInfo) {
      // 추가된 액션
      this.searchedHouseInfo = houseInfo
    },
    setSelectedHouseDealVo(houseDealVo) {
      this.selectedHouseDealVo = houseDealVo
      this.isHouseDetailOpen = true
    },
    closeHouseDetail() {
      this.isHouseDetailOpen = false
      this.selectedHouseDealVo = null
    }
  },
  persist: {
    key: 'user-info-store',
    storage: sessionStorage, // 세션 스토리지에 저장
    paths: ['userLocation', 'selectedHouseDealVo', `isHouseDetailOpen`] // 저장할 상태의 경로
  }
})
