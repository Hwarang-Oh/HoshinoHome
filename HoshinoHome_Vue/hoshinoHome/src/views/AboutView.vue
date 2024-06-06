<script setup></script>

<template>
  <div class="about">
    <img src="/Karina.gif" class="gif-image" />
  </div>
</template>

<style>
.about {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.gif-image {
  max-width: 100%;
  height: auto;
}

@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
