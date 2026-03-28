<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">12</div>
              <div class="stat-label">待处理订单</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">85%</div>
              <div class="stat-label">本月完工率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">3</div>
              <div class="stat-label">延期预警</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥128,500</div>
              <div class="stat-label">本月产值</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>订单趋势</span>
            </div>
          </template>
          <div ref="orderChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>生产状态</span>
            </div>
          </template>
          <div ref="productionChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>待办事项</span>
        </div>
      </template>
      <el-table :data="todoList" style="width: 100%">
        <el-table-column prop="type" label="类型" width="100" />
        <el-table-column prop="content" label="内容" />
        <el-table-column prop="deadline" label="截止时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'urgent' ? 'danger' : 'warning'">
              {{ row.status === 'urgent' ? '紧急' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const orderChartRef = ref(null)
const productionChartRef = ref(null)

const todoList = ref([
  { type: '订单', content: 'A 客户 - 精密零件加工订单需要排产', deadline: '2024-01-15 18:00', status: 'urgent' },
  { type: '采购', content: '钢材库存低于安全值，需要采购', deadline: '2024-01-16 12:00', status: 'normal' },
  { type: '生产', content: 'PT202401150001 工序完成确认', deadline: '2024-01-15 17:00', status: 'urgent' },
])

onMounted(() => {
  // 订单趋势图
  const orderChart = echarts.init(orderChartRef.value)
  orderChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['1 月', '2 月', '3 月', '4 月', '5 月', '6 月']
    },
    yAxis: { type: 'value' },
    series: [{
      data: [820, 932, 901, 1234, 1290, 1330],
      type: 'line',
      smooth: true,
      areaStyle: {}
    }]
  })

  // 生产状态图
  const productionChart = echarts.init(productionChartRef.value)
  productionChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: '50%',
      data: [
        { value: 25, name: '生产中' },
        { value: 15, name: '待排产' },
        { value: 40, name: '已完成' },
        { value: 5, name: '已延期' }
      ]
    }]
  })

  window.addEventListener('resize', () => {
    orderChart.resize()
    productionChart.resize()
  })
})
</script>

<style scoped>
.stat-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 28px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
