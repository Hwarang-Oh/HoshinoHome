<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div
    class="bg-white hover:bg-green-500 transition duration-300 shadow p-4 flex justify-between items-center"
  ></div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
