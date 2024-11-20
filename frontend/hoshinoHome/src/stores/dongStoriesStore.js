// src/stores/dongStoriesStore.js

import { defineStore } from 'pinia'

export const useDongStoriesStore = defineStore('dongStories', {
  state: () => ({
    selectedDongStory: null,
  }),
  actions: {
    setSelectedDongStory(dongStory) {
      this.selectedDongStory = dongStory;
    },
  },
})
