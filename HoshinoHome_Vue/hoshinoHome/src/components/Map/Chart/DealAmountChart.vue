<template>
  <div class="chart-container">
    <div v-if="chartType === 'dealAmount'">
      <Line :data="dealChartData" :options="options" />
    </div>
    <div v-else-if="chartType === 'depositAmount'">
      <Line :data="depositChartData" :options="options" />
    </div>
    <div v-else>
      <h3 class="sub-chart-title">보증금</h3>
      <div class="sub-chart-container shadow rounded">
        <Line :data="monthly_depositChartData" :options="options" />
      </div>
      <h3 class="sub-chart-title">월세</h3>
      <div class="sub-chart-container shadow rounded">
        <Line :data="monthly_monthlyChartData" :options="options" />
      </div>
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
  Legend,
  Filler
} from 'chart.js'

ChartJS.register(LineElement, PointElement, CategoryScale, LinearScale, Tooltip, Legend, Filler)

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

const dealChartData = ref({
  labels: [],
  datasets: []
})

const depositChartData = ref({
  labels: [],
  datasets: []
})

const monthly_depositChartData = ref({
  labels: [],
  datasets: []
})

const monthly_monthlyChartData = ref({
  labels: [],
  datasets: []
})

function processDealData(data, type) {
  const monthlyData = {}

  data.forEach((deal) => {
    const { deal_year, deal_month, deal_type, deal_amount, deposit_amount, monthly_amount } = deal
    const key = `${deal_year}-${deal_month}`
    if (!monthlyData[key]) {
      monthlyData[key] = {
        dealTotal: 0,
        depositTotal: 0,
        monthly_depositTotal: 0,
        monthly_monthlyTotal: 0,
        dealCount: 0,
        depositCount: 0,
        monthlyCount: 0
      }
    }
    let dealAmount = 0
    let depositAmount = 0
    let monthly_depositAmount = 0
    let monthly_monthlyAmount = 0

    if (deal_type === 1 && deal_amount) {
      dealAmount = parseFloat(deal_amount.replace(/,/g, ''))
      monthlyData[key].dealTotal += dealAmount
      monthlyData[key].dealCount += 1
    } else if (deal_type === 2 && deposit_amount) {
      depositAmount = parseFloat(deposit_amount.replace(/,/g, '') || 0)
      monthlyData[key].depositTotal += depositAmount
      monthlyData[key].depositCount += 1
    } else if (deal_type === 3 && deposit_amount && monthly_amount) {
      monthly_depositAmount = parseFloat(deposit_amount?.replace(/,/g, '') || 0)
      monthly_monthlyAmount = parseFloat(monthly_amount?.replace(/,/g, '') || 0)
      monthlyData[key].monthly_depositTotal += monthly_depositAmount
      monthlyData[key].monthly_monthlyTotal += monthly_monthlyAmount
      monthlyData[key].monthlyCount += 1
    }
  })

  const sortedKeys = Object.keys(monthlyData).sort((a, b) => new Date(a) - new Date(b))

  const dealLabels = []
  const depositLabels = []
  const monthlyLabels = []
  const dealChart = []
  const depositChart = []
  const monthlyChart_depositData = []
  const monthlyChart_monthlyData = []

  sortedKeys.forEach((key) => {
    const avgDeal = monthlyData[key].dealTotal / (monthlyData[key].dealCount || 1)
    const avgDeposit = monthlyData[key].depositTotal / (monthlyData[key].depositCount || 1)
    const avgMonthly_deposit =
      monthlyData[key].monthly_depositTotal / (monthlyData[key].monthlyCount || 1)
    const avgMonthly_monthly =
      monthlyData[key].monthly_monthlyTotal / (monthlyData[key].monthlyCount || 1)

    if (avgDeal > 0) {
      dealLabels.push(key)
      dealChart.push(avgDeal)
    }
    if (avgDeposit > 0) {
      depositLabels.push(key)
      depositChart.push(avgDeposit)
    }
    if (avgMonthly_deposit > 0 && avgMonthly_monthly > 0) {
      monthlyLabels.push(key)
      monthlyChart_depositData.push(avgMonthly_deposit)
      monthlyChart_monthlyData.push(avgMonthly_monthly)
    }
  })

  // 데이터 포인트가 하나일 경우, 두 배로 만들어서 차트를 정상적으로 표시되도록 처리
  if (dealChart.length === 1) {
    dealLabels.push(dealLabels[0])
    dealChart.push(dealChart[0])
  }
  if (depositChart.length === 1) {
    depositLabels.push(depositLabels[0])
    depositChart.push(depositChart[0])
  }
  if (monthlyChart_depositData.length === 1) {
    monthlyLabels.push(monthlyLabels[0])
    monthlyChart_depositData.push(monthlyChart_depositData[0])
    monthlyChart_monthlyData.push(monthlyChart_monthlyData[0])
  }

  if (type === 'dealAmount') {
    dealChartData.value = {
      labels: dealLabels,
      datasets: [
        {
          label: '평균 거래 금액',
          backgroundColor: 'rgba(66, 165, 245, 0.2)',
          borderColor: 'rgba(66, 165, 245, 1)',
          data: dealChart,
          fill: true,
          pointRadius: 0,
          pointHoverRadius: 0
        }
      ]
    }
  } else if (type === 'depositAmount') {
    depositChartData.value = {
      labels: depositLabels,
      datasets: [
        {
          label: '평균 보증금 금액',
          backgroundColor: 'rgba(66, 165, 245, 0.2)',
          borderColor: 'rgba(66, 165, 245, 1)',
          data: depositChart,
          fill: true,
          pointRadius: 0,
          pointHoverRadius: 0
        }
      ]
    }
  } else {
    monthly_depositChartData.value = {
      labels: monthlyLabels,
      datasets: [
        {
          label: '평균 보증금 금액',
          backgroundColor: 'rgba(66, 165, 245, 0.2)',
          borderColor: 'rgba(66, 165, 245, 1)',
          data: monthlyChart_depositData,
          fill: true,
          pointRadius: 0,
          pointHoverRadius: 0
        }
      ]
    }
    monthly_monthlyChartData.value = {
      labels: monthlyLabels,
      datasets: [
        {
          label: '평균 월세 금액',
          backgroundColor: 'rgba(255, 99, 132, 0.2)',
          borderColor: 'rgba(255, 99, 132, 1)',
          data: monthlyChart_monthlyData,
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
        display: false
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
  margin-bottom: 20px; /* 차트 간 간격 추가 */
  padding: 10px; /* 내부 패딩 추가 */
}
.sub-chart-title {
  font-size: 1rem;
  font-weight: 600;
  text-align: left;
  margin-bottom: 10px;
  color: #1f2937;
}
</style>
