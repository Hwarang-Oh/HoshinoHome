import { defineStore } from 'pinia'

export const useMapInfoStore = defineStore('mapInfo', {
  state: () => ({}),
  actions: {},
  persist: {
    key: 'map-info-store',
    storage: sessionStorage, // 세션 스토리지에 저장
    paths: [] // 저장할 상태의 경로
  }
})
