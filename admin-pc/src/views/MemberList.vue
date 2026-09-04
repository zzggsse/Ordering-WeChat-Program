<template>
  <el-card shadow="never">
    <template #header><div class="row"><span>会员管理</span><el-button type="primary" size="small">新增会员</el-button></div></template>
    <el-table :data="members">
      <el-table-column prop="name" label="昵称" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="level" label="等级" width="100">
        <template #default="{row}"><el-tag :type="row.level==='钻石'?'danger':row.level==='金牌'?'warning':'info'" size="small">{{ row.level }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="points" label="积分" width="90" align="right" />
      <el-table-column prop="balance" label="储值余额" width="110" align="right" />
      <el-table-column prop="orders" label="下单数" width="90" align="right" />
      <el-table-column label="操作" width="120"><template #default="{row}"><el-button size="small" text type="primary" @click="open(row)">详情</el-button></template></el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
const members = ref([
  { name: '小鱼', phone: '138****2211', level: '钻石', points: 3280, balance: 156.5, orders: 42 },
  { name: '阿May', phone: '189****7703', level: '金牌', points: 1240, balance: 0, orders: 18 },
  { name: '大白兔', phone: '157****9908', level: '银牌', points: 620, balance: 80, orders: 9 },
  { name: '七分糖', phone: '133****4521', level: '新人', points: 120, balance: 0, orders: 2 },
])
const open = (row) => ElMessage.info(`查看 ${row.name} 的明细`)
</script>
<style scoped>.row{display:flex;justify-content:space-between}</style>
