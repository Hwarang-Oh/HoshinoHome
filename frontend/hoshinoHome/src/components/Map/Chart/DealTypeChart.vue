<template>
  <div>
    <Bar :data="chartData" :options="options" />
  </div>
</template>

<script setup>
import { ref, defineProps, watchEffect } from 'vue'
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Tooltip, Legend } from 'chart.js'

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend)

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

const cmykColors = {
  c: 'rgba(0, 31, 63, 0.6)', // #001f3f
  m: 'rgba(0, 86, 163, 0.6)', // #0056a3
  y: 'rgba(51, 153, 204, 0.6)', // #3399cc
  k: 'rgba(0, 0, 0, 0.6)' // 검정색 그대로 유지
}

const cmykBorderColors = {
  c: 'rgba(0, 31, 63, 1)', // #001f3f
  m: 'rgba(0, 86, 163, 1)', // #0056a3
  y: 'rgba(51, 153, 204, 1)', // #3399cc
  k: 'rgba(0, 0, 0, 1)' // 검정색 그대로 유지
}

// Function to set up chart data
function setChartData(data) {
  chartData.value = {
    labels: ['매매', '전세', '월세'],
    datasets: [
      {
        label: '',
        backgroundColor: [cmykColors.c, cmykColors.m, cmykColors.y],
        borderColor: [cmykBorderColors.c, cmykBorderColors.m, cmykBorderColors.y],
        borderWidth: 1,
        hoverBackgroundColor: [cmykBorderColors.c, cmykBorderColors.m, cmykBorderColors.y],
        hoverBorderColor: [cmykBorderColors.c, cmykBorderColors.m, cmykBorderColors.y],
        data: data.counts
      }
    ]
  }
}

watchEffect(() => {
  if (props.data && props.data.labels && props.data.counts) {
    setChartData(props.data)
  }
})

const options = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false
    },
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
        label: (context) => ` ${context.raw} 건`
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
        color: '#333',
        callback: (value) => `${value} 건` // Add '건' to y-axis labels
      },
      grid: {
        display: false
      }
    }
  }
}
</script>

<style scoped></style>
