<template>
  <div>
    <Bar :data="chartData" :options="options" />
  </div>
</template>

<script setup>
import { ref, defineProps, watch } from 'vue'
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

function processDealData(data, type) {
  const monthlyData = {}

  data.forEach((deal) => {
    const { deal_year, deal_month, deal_amount, deposit_amount, monthly_amount } = deal
    const key = `${deal_year}-${deal_month}`

    let amount = 0
    if (type === 'dealAmount' && deal_amount) {
      amount = parseFloat(deal_amount.replace(/,/g, ''))
    } else if (type === 'depositAmount' && deposit_amount) {
      amount = parseFloat(deposit_amount.replace(/,/g, '') || 0)
    } else if (type === 'monthlyAmount') {
      const deposit = parseFloat(deposit_amount?.replace(/,/g, '') || 0)
      const monthly = parseFloat(monthly_amount?.replace(/,/g, '') || 0)
      amount = deposit + monthly
    }

    if (!monthlyData[key]) {
      monthlyData[key] = { total: 0, count: 0 }
    }

    monthlyData[key].total += amount
    monthlyData[key].count += 1
  })

  const labels = []
  const amounts = []

  for (const key in monthlyData) {
    labels.push(key)
    amounts.push(monthlyData[key].total / monthlyData[key].count)
  }

  chartData.value = {
    labels,
    datasets: [
      {
        label:
          type === 'dealAmount'
            ? 'Average Deal Amount'
            : type === 'depositAmount'
              ? 'Average Deposit Amount'
              : 'Average Deposit + Monthly Amount',
        backgroundColor: '#42A5F5',
        borderColor: '#1E88E5',
        borderWidth: 1,
        hoverBackgroundColor: '#1E88E5',
        hoverBorderColor: '#0D47A1',
        data: amounts
      }
    ]
  }
}

watch(
  () => props.chartType,
  () => {
    processDealData(props.dealData, props.chartType)
  }
)

watch(
  () => props.dealData,
  () => {
    processDealData(props.dealData, props.chartType)
  }
)

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
        label: (context) => ` ${context.raw.toFixed(2)}`
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

<style scoped>
.chart-container {
  position: relative;
  height: 400px;
}
</style>
