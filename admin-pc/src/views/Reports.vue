<template>
  <el-row :gutter="16">
    <el-col :span="16">
      <el-card shadow="never">
        <template #header><div class="row"><span>近 7 日销售走势</span><el-radio-group v-model="dim" size="small"><el-radio-button label="销售额"/><el-radio-button label="订单量"/></el-radio-group></div></template>
        <div ref="el" style="height:320px"></div>
      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card shadow="never">
        <template #header>销售时段分布</template>
        <div ref="pieEl" style="height:320px"></div>
      </el-card>
    </el-col>
  </el-row>
</template>
<script setup>
import { ref, watch, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
const dim = ref('销售额')
const el = ref(); const pieEl = ref()
let line, pie
const renderLine = () => {
  line ||= echarts.init(el.value)
  line.setOption({
    grid:{left:40,right:16,top:20,bottom:30},
    tooltip:{trigger:'axis'},
    xAxis:{type:'category',data:['8/25','8/26','8/27','8/28','8/29','8/30','今日']},
    yAxis:{type:'value'},
    series:[{data: dim.value==='销售额'?[2480,2910,2620,3120,3385,2980,3268]:[142,166,150,178,196,169,186], type:'bar', itemStyle:{color:'#409eff',borderRadius:[4,4,0,0]}}]
  })
}
onMounted(async ()=>{ await nextTick(); renderLine(); pie=echarts.init(pieEl.value); pie.setOption({
  tooltip:{trigger:'item'}, legend:{bottom:0},
  series:[{type:'pie',radius:['45%','70%'],data:[
    {value:64,name:'10:00-12:00'}, {value:45,name:'12:00-14:00'},
    {value:88,name:'14:00-16:00'}, {value:52,name:'16:00-18:00'}, {value:21,name:'其他'}]}]
})})
watch(dim, async()=>{ await nextTick(); renderLine() })
</script>
<style scoped>.row{display:flex;justify-content:space-between}</style>
