<script setup>
import Header from '@/components/Map/Detail/Header.vue'
import MiniMap from '@/components/Map/Detail/MiniMap.vue'
import Info_Tab from '@/components/Map/Detail/Info_Tab.vue'
import DealInfo from '@/components/Map/Detail/DealInfo.vue'
import ChartSection from '@/components/Map/Detail/ChartSection.vue'
import TransactionInfo from '@/components/Map/Detail/TransactionInfo.vue'
</script>

<template>
  <!-- Left Side (Property Detail) -->
  <div class="absolute top-0 left-0 w-1/5 h-full bg-white shadow-lg overflow-auto p-4 z-50">
    <!-- Header Section -->
    <Header />
    <hr class="contour mb-4" />
    <!-- SubMap Section -->
    <MiniMap />

    <!-- Info_Tab Section -->
    <Info_Tab />

    <!-- DealInfo Section-->
    <DealInfo />

    <!-- Content Section -->
    <ChartSection />

    <TransactionInfo />
  </div>
</template>

<style scoped>
.contour {
  border: 0;
  height: 1px;
  background: #e5e7eb; /* Gray color */
  margin-bottom: 1rem;
}
</style>
