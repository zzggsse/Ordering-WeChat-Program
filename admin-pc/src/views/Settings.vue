<template>
  <el-card shadow="never">
    <el-form :model="form" label-width="120px" style="max-width:560px">
      <el-divider content-position="left">门店信息</el-divider>
      <el-form-item label="门店名称"><el-input v-model="form.name" /></el-form-item>
      <el-form-item label="营业时间"><el-time-picker v-model="form.times" is-range start-placeholder="开门" end-placeholder="打烊" /></el-form-item>
      <el-divider content-position="left">打印与大屏</el-divider>
      <el-form-item label="启用叫号大屏"><el-switch v-model="form.screen" /></el-form-item>
      <el-form-item label="自动打印小票"><el-switch v-model="form.print" /></el-form-item>
      <el-divider content-position="left">支付</el-divider>
      <el-form-item label="支付方式"><el-checkbox-group v-model="form.pay"><el-checkbox label="微信支付" disabled /></el-checkbox-group></el-form-item>
      <el-form-item><el-button type="primary" @click="save">保存设置</el-button></el-form-item>
    </el-form>
  </el-card>
</template>
<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
const form = reactive({ name:'中心店', times:[new Date(2000,0,1,9,0),new Date(2000,0,1,22,0)], screen:true, print:true, pay:['微信支付'] })
const save=()=>ElMessage.success('设置已保存（演示）')
</script>
