<template>
  <div>
    <Bar :chartData="chartData" :options="options" />
  </div>
</template>

<script setup>
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

const props = defineProps(['data'])

const chartData = {
  labels: ['Deal Type 1', 'Deal Type 2', 'Deal Type 3'],
  datasets: [
    {
      label: 'Count',
      data: props.data,
      backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726']
    }
  ]
}

const options = {
  responsive: true,
  maintainAspectRatio: false
}
</script>
