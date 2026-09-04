<template>
  <el-card shadow="never">
    <template #header><div class="row"><span>营销中心</span><el-button type="primary" size="small">新建活动</el-button></div></template>
    <el-timeline>
      <el-timeline-item timestamp="全场满 30 减 5 · 进行中" type="success">进店领券，下单自动抵扣</el-timeline-item>
      <el-timeline-item timestamp="新客首杯立减 3 元 · 进行中" type="primary">仅限首次下单用户</el-timeline-item>
      <el-timeline-item timestamp="下午茶时段 14:00-16:00 第二杯半价 · 预告" type="warning">活动将于今日 14:00 生效</el-timeline-item>
      <el-timeline-item timestamp="储值满 100 送 15 · 已结束" type="info">累计核销 ¥8,320</el-timeline-item>
    </el-timeline>
  </el-card>
</template>
<script setup>
</script>
<style scoped>.row{display:flex;justify-content:space-between}</style>
