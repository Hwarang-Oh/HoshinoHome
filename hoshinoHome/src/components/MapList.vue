<script setup>
import { inject } from 'vue'

const { dealList } = inject('res')
</script>

<template>
  <div class="absolute top-0 left-0 w-1/4 h-full bg-white bg-opacity-90 p-4 overflow-auto z-10">
    <table class="w-full text-sm text-gray-700">
      <thead>
        <tr class="text-left">
          <th class="p-2">Apartment</th>
          <th class="p-2">Floor</th>
          <th class="p-2">Area</th>
          <th class="p-2">Price</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(deal, index) in dealList" :key="index" class="hover:bg-green-100">
          <td class="p-2">{{ deal.apartment_name }}</td>
          <td class="p-2">{{ deal.floor }}</td>
          <td class="p-2">{{ deal.area }}</td>
          <td class="p-2">{{ deal.deal_amount }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<style scoped></style>
