<script setup>
import { RouterView } from 'vue-router'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import './assets/style.css'
</script>

<template>
  <Header></Header>
  <RouterView />
  <Footer></Footer>
</template>

<style scoped></style>
