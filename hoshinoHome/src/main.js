// import './assets/main.css'
import './assets/style.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

const app = createApp(App)
app.use(createPinia())
app.use(router)

app.mount('#app')
window.Kakao.init("b6fc8f0d4cfc8c0ade4b040f2f2daf35");