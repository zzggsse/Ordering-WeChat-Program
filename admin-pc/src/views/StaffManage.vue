<template>
  <el-card shadow="never">
    <template #header><div class="row"><span>员工与权限</span><el-button type="primary" size="small">新增员工</el-button></div></template>
    <el-table :data="staff">
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="account" label="账号" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="{row}"><el-tag :type="roleType(row.role)" size="small">{{ row.role }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="store" label="所属门店" />
      <el-table-column label="启用" width="90"><template #default="{row}"><el-switch v-model="row.enabled" /></template></el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { ref } from 'vue'
const staff=ref([
  {name:'陈店长',account:'manager',role:'店长',store:'中心店',enabled:true},
  {name:'小李',account:'lixiao',role:'店员',store:'中心店',enabled:true},
  {name:'财务 王姐',account:'wang',role:'财务',store:'多店',enabled:true},
])
const roleType=(r)=>({店长:'warning',店员:'info',财务:'success',老板:'danger'}[r]||'info')
</script>
<style scoped>.row{display:flex;justify-content:space-between}</style>
