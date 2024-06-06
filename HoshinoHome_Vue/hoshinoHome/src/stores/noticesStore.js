import { defineStore } from 'pinia'

export const useNoticesStore = defineStore('notices', {
  state: () => ({
    selectedNotice: null // 현재 선택된 notice 객체를 저장할 상태
  }),
  actions: {
    setSelectedNotice(notice) {
      this.selectedNotice = notice
    }
  }
})
