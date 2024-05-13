<script setup></script>

<template>
  <!-- Header Section -->
  <header class="bg-gray-800 text-white py-4 px-6 flex justify-between items-center">
    <span>
      <RouterLink to="/" class="ml-2 text-lg">Home</RouterLink>
      <RouterLink to="/about" class="ml-5 text-lg">About</RouterLink>
      <RouterLink to="/notice" class="ml-5 text-lg">Notice</RouterLink>
      <RouterLink to="/map" class="ml-5 text-lg">Map</RouterLink>
    </span>

    <nav class="space-x-4 flex text-lg">
      <RouterLink to="/login" class="ml-5 text-lg">Login</RouterLink>
      <RouterLink to="/register" class="ml-5 text-lg">Register</RouterLink>
    </nav>
  </header>
</template>

<style scoped></style>
