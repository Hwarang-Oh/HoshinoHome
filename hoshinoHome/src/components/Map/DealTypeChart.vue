<template>
  <div>
    <Bar :data="chartData" :options="options" />
  </div>
</template>

<script setup>
import { ref, defineProps, watchEffect } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
  Title
} from 'chart.js'

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend, Title)

const props = defineProps({
  data: {
    type: Object,
    required: true,
    default: () => ({ labels: [], counts: [] })
  }
})

const chartData = ref({
  labels: [],
  datasets: []
})

// Function to set up chart data
function setChartData(data) {
  chartData.value = {
    labels: data.labels,
    datasets: [
      {
        label: 'Deal Type Count',
        backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726'],
        borderColor: ['#1E88E5', '#43A047', '#FB8C00'],
        borderWidth: 1,
        hoverBackgroundColor: ['#1E88E5', '#43A047', '#FB8C00'],
        hoverBorderColor: ['#0D47A1', '#1B5E20', '#E65100'],
        data: data.counts
      }
    ]
  }
}

watchEffect(() => {
  if (props.data && props.data.labels && props.data.counts) {
    setChartData(props.data)
    console.log('chartData updated:', chartData.value)
  }
})

const options = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: true,
      labels: {
        font: {
          size: 14,
          family: 'Arial',
          weight: 'bold'
        },
        color: '#333'
      }
    },
    // title: {
    //   display: true,
    //   text: 'Deal Type Distribution',
    //   font: {
    //     size: 18,
    //     family: 'Arial',
    //     weight: 'bold'
    //   },
    //   color: '#333'
    // },
    tooltip: {
      backgroundColor: 'rgba(0, 0, 0, 0.7)',
      titleFont: {
        size: 16,
        family: 'Arial',
        weight: 'bold'
      },
      bodyFont: {
        size: 14,
        family: 'Arial',
        weight: 'normal'
      },
      footerFont: {
        size: 12,
        family: 'Arial',
        weight: 'normal'
      },
      callbacks: {
        label: (context) => ` ${context.raw} deals`
      }
    }
  },
  scales: {
    x: {
      ticks: {
        font: {
          size: 12,
          family: 'Arial',
          weight: 'bold'
        },
        color: '#333'
      },
      grid: {
        display: false
      }
    },
    y: {
      ticks: {
        font: {
          size: 12,
          family: 'Arial',
          weight: 'bold'
        },
        color: '#333'
      },
      grid: {
        color: 'rgba(200, 200, 200, 0.2)'
      }
    }
  }
}
</script>

<style scoped></style>
