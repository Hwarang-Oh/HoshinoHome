import { defineStore } from 'pinia'

export const useUserInfoStore = defineStore('userInfo', {
  state: () => ({
    userLocation: {
      lat: 37.5012767241426,
      lng: 127.039600248343
    },
    searchedHouseInfo: null // 추가된 상태
  }),
  actions: {
    setUserLocation(location) {
      this.userLocation = location
    },
    setSearchedHouseInfo(houseInfo) {
      // 추가된 액션
      this.searchedHouseInfo = houseInfo
    }
  }
})
