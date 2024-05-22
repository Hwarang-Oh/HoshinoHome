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
  c: 'rgba(0, 255, 255, 0.6)',
  m: 'rgba(255, 0, 255, 0.6)',
  y: 'rgba(255, 255, 0, 0.6)',
  k: 'rgba(0, 0, 0, 0.6)'
}

const cmykBorderColors = {
  c: 'rgba(0, 255, 255, 1)',
  m: 'rgba(255, 0, 255, 1)',
  y: 'rgba(255, 255, 0, 1)',
  k: 'rgba(0, 0, 0, 1)'
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
