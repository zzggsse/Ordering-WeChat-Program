<template>
  <el-card shadow="never">
    <template #header>
      <div class="row"><span>库存与采购</span>
        <el-button type="primary" size="small" @click="alert('课程演示：可在此创建采购单')">创建采购单</el-button>
      </div>
    </template>
    <el-table :data="list">
      <el-table-column prop="name" label="材料" />
      <el-table-column prop="spec" label="规格" width="100" />
      <el-table-column prop="stock" label="当前库存" width="110">
        <template #default="{row}"><b :style="{color: low(row)?'#f56c6c':'#303133'}">{{ row.stock }}</b></template>
      </el-table-column>
      <el-table-column prop="threshold" label="预警阈值" width="100" />
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column label="状态" width="110">
        <template #default="{row}">
          <el-tag v-if="low(row)" type="danger" size="small">库存偏低</el-tag>
          <el-tag v-else type="success" size="small">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="adjust(row, 10)">入库 +10</el-button>
          <el-button size="small" type="warning" @click="adjust(row, -5)">出库 -5</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { inventoryList } from '../api/mock'
const list = ref(inventoryList)
const low = (row) => row.stock <= row.threshold
const adjust = (row, n) => { row.stock = Math.max(0, row.stock + n); ElMessage.success(`已更新 ${row.name} 库存`) }
</script>
<style scoped>.row{display:flex;justify-content:space-between}</style>
