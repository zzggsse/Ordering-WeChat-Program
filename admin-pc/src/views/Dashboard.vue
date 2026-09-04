<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="6" v-for="s in stats" :key="s.label">
        <el-card shadow="hover" class="stat">
          <div class="stat-inner">
            <el-icon :size="34" :color="s.color"><component :is="s.icon" /></el-icon>
            <div>
              <div class="v">{{ s.value }}</div>
              <div class="l">{{ s.label }} <span class="t" :style="{color:s.color}">{{ s.trend }}</span></div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>近 7 日营收趋势</template>
          <div ref="chartEl" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>热门商品 Top 5</template>
          <el-table :data="top" size="small">
            <el-table-column type="index" label="#" width="50" />
            <el-table-column prop="name" label="商品" />
            <el-table-column prop="category" label="分类" width="110" />
            <el-table-column prop="sales" label="销量" width="90" align="right">
              <template #default="{ row }"><el-tag size="small" type="warning">{{ row.sales }}</el-tag></template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import { dashboardStats, menuList } from '../api/mock'
const stats = dashboardStats
const top = [...menuList].sort((a, b) => b.sales - a.sales).slice(0, 5)
const chartEl = ref()
onMounted(() => {
  const chart = echarts.init(chartEl.value)
  chart.setOption({
    grid: { left: 40, right: 16, top: 20, bottom: 30 },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: ['8/25','8/26','8/27','8/28','8/29','8/30','今日'] },
    yAxis: { type: 'value' },
    series: [{
      data: [2480, 2910, 2620, 3120, 3385, 2980, 3268],
      type: 'line', smooth: true, areaStyle: { opacity: .2 }, itemStyle: { color: '#ff9f43' }
    }]
  })
})
</script>
<style scoped>
.stat-inner { display: flex; align-items: center; gap: 16px; }
.v { font-size: 26px; font-weight: 700; color:#303133; }
.l { color:#909399; font-size:13px; }
.t { font-size:12px; margin-left:6px; }
</style>
