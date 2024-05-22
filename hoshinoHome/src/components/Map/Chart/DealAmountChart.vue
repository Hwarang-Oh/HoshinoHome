<template>
  <div class="chart-container">
    <div v-if="chartType === 'monthlyAmount'">
      <div class="sub-chart-container">
        <Line :data="depositChartData" :options="options" />
      </div>
      <div class="sub-chart-container">
        <Line :data="monthlyChartData" :options="options" />
      </div>
    </div>
    <div v-else>
      <Line :data="chartData" :options="options" />
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, watch } from 'vue'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
} from 'chart.js'

ChartJS.register(LineElement, PointElement, CategoryScale, LinearScale, Tooltip, Legend)

const props = defineProps({
  dealData: {
    type: Array,
    required: true
  },
  chartType: {
    type: String,
    required: true
  }
})

const chartData = ref({
  labels: [],
  datasets: []
})

const depositChartData = ref({
  labels: [],
  datasets: []
})

const monthlyChartData = ref({
  labels: [],
  datasets: []
})

function processDealData(data, type) {
  const monthlyData = {}

  data.forEach((deal) => {
    const { deal_year, deal_month, deal_amount, deposit_amount, monthly_amount } = deal
    const key = `${deal_year}-${deal_month}`

    let deposit = 0
    let monthly = 0
    if (type === 'dealAmount' && deal_amount) {
      deposit = parseFloat(deal_amount.replace(/,/g, ''))
    } else if (type === 'depositAmount' && deposit_amount) {
      deposit = parseFloat(deposit_amount.replace(/,/g, '') || 0)
    } else if (type === 'monthlyRentAmount') {
      deposit = parseFloat(deposit_amount?.replace(/,/g, '') || 0)
      monthly = parseFloat(monthly_amount?.replace(/,/g, '') || 0)
    }

    if (!monthlyData[key]) {
      monthlyData[key] = { depositTotal: 0, monthlyTotal: 0, count: 0 }
    }

    monthlyData[key].depositTotal += deposit
    monthlyData[key].monthlyTotal += monthly
    monthlyData[key].count += 1
  })

  const sortedKeys = Object.keys(monthlyData).sort((a, b) => new Date(a) - new Date(b))

  const labels = []
  const depositAmounts = []
  const monthlyAmounts = []

  sortedKeys.forEach((key) => {
    const avgDeposit = monthlyData[key].depositTotal / monthlyData[key].count
    const avgMonthly = monthlyData[key].monthlyTotal / monthlyData[key].count
    if (avgDeposit > 0 || avgMonthly > 0) {
      labels.push(key)
      depositAmounts.push(avgDeposit)
      monthlyAmounts.push(avgMonthly)
    }
  })

  if (type === 'monthlyRentAmount') {
    depositChartData.value = {
      labels,
      datasets: [
        {
          label: '평균 보증금 금액',
          backgroundColor: 'rgba(66, 165, 245, 0.2)',
          borderColor: 'rgba(66, 165, 245, 1)',
          data: depositAmounts,
          fill: true,
          pointRadius: 0,
          pointHoverRadius: 0
        }
      ]
    }
    monthlyChartData.value = {
      labels,
      datasets: [
        {
          label: '평균 월세 금액',
          backgroundColor: 'rgba(255, 99, 132, 0.2)',
          borderColor: 'rgba(255, 99, 132, 1)',
          data: monthlyAmounts,
          fill: true,
          pointRadius: 0,
          pointHoverRadius: 0
        }
      ]
    }
  } else {
    chartData.value = {
      labels,
      datasets: [
        {
          label: type === 'dealAmount' ? '평균 거래 금액' : '평균 보증금 금액',
          backgroundColor: 'rgba(66, 165, 245, 0.2)',
          borderColor: 'rgba(66, 165, 245, 1)',
          data: depositAmounts,
          fill: true,
          pointRadius: 0,
          pointHoverRadius: 0
        }
      ]
    }
  }
}

watch(
  () => props.chartType,
  () => {
    processDealData(props.dealData, props.chartType)
  },
  { immediate: true }
)

watch(
  () => props.dealData,
  () => {
    processDealData(props.dealData, props.chartType)
  },
  { immediate: true }
)

const options = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false // 범례 숨김
    },
    tooltip: {
      backgroundColor: 'rgba(0, 0, 0, 0.7)',
      titleFont: {
        size: 16,
        family: 'Arial',
        weight: 'bold'
      },
      bodyFont: {
        size: 12,
        family: 'Arial',
        weight: 'normal'
      },
      footerFont: {
        size: 10,
        family: 'Arial',
        weight: 'normal'
      },
      callbacks: {
        label: (context) => {
          const value = context.raw
          if (value >= 10000) {
            return ` ${value / 10000} 억`
          }
          return ` ${value} 만`
        }
      }
    }
  },
  scales: {
    x: {
      ticks: {
        font: {
          size: 10,
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
          size: 10,
          family: 'Arial',
          weight: 'bold'
        },
        color: '#333',
        callback: (value) => {
          if (value >= 10000) {
            return `${(value / 10000).toFixed(1)} 억`
          }
          return `${value} 만`
        }
      },
      grid: {
        color: 'rgba(200, 200, 200, 0.2)'
      }
    }
  }
}
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 100%; /* 차트의 높이를 조정 */
}

.sub-chart-container {
  position: relative;
  height: 50%; /* 차트의 높이를 조정 */
  margin-bottom: 10px; /* 차트 간 간격 추가 */
}
</style>
