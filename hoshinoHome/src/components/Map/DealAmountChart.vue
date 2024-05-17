<template>
  <div>
    <Line :chartData="chartData" :options="options" />
  </div>
</template>

<script setup>
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement
} from 'chart.js'
ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement)

const props = defineProps(['data'])

const chartData = {
  labels: props.data.labels,
  datasets: [
    {
      label: 'Deal Amount',
      data: props.data.amounts,
      backgroundColor: 'rgba(75, 192, 192, 0.2)',
      borderColor: 'rgba(75, 192, 192, 1)',
      borderWidth: 1,
      fill: false
    }
  ]
}

const options = {
  responsive: true,
  maintainAspectRatio: false
}
</script>
