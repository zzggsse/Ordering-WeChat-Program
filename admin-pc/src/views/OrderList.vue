<template>
  <el-card shadow="never">
    <template #header>
      <div class="row">
        <span>订单列表</span>
        <el-radio-group v-model="filter" size="small">
          <el-radio-button label="全部" />
          <el-radio-button label="待接单" />
          <el-radio-button label="制作中" />
          <el-radio-button label="待取餐" />
          <el-radio-button label="已完成" />
        </el-radio-group>
      </div>
    </template>
    <el-table :data="shown">
      <el-table-column prop="no" label="订单号" width="160" />
      <el-table-column prop="period" label="下单时间" width="90" />
      <el-table-column prop="type" label="方式" width="70" />
      <el-table-column prop="items" label="商品明细" />
      <el-table-column prop="amount" label="金额" width="90" align="right">
        <template #default="{ row }">¥ {{ row.amount }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" type="primary" v-if="row.status==='待接单'" @click="accept(row)">接单</el-button>
          <el-button size="small" type="success" v-if="row.status==='制作中'" @click="ready(row)">出杯</el-button>
          <el-button size="small" v-if="row.status==='待取餐'" type="warning" disabled>已叫号</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { orderList } from '../api/mock'
const filter = ref('全部')
const list = ref(orderList)
const shown = computed(() => filter.value === '全部' ? list.value : list.value.filter(o => o.status === filter.value))
const statusType = (s) => ({ 待接单: 'danger', 制作中: 'warning', 待取餐: 'success', 已完成: 'info' }[s])
function accept(row) { row.status = '制作中'; ElMessage.success('已接单') }
function ready(row) { row.status = '待取餐'; ElMessage.success('已出杯，正在叫号') }
</script>
<style scoped>.row{display:flex;justify-content:space-between;align-items:center}</style>
