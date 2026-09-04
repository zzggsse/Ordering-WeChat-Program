<template>
  <div class="login">
    <div class="card">
      <div class="brand"><el-icon :size="34" color="#ffd04b"><Cup /></el-icon></div>
      <h2>奶茶点单 · 商家后台</h2>
      <el-form :model="form" label-position="top">
        <el-form-item label="账号"><el-input v-model="form.user" placeholder="demo" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.pwd" type="password" show-password placeholder="demo" /></el-form-item>
        <el-button type="primary" style="width:100%" @click="login">登录</el-button>
      </el-form>
      <p class="tip">课程演示：输入任意账号密码即可进入</p>
    </div>
  </div>
</template>
<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const form = reactive({ user: 'demo', pwd: 'demo' })
function login() { router.push('/') }
</script>
<style scoped>
.login { height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg,#232946,#4a4f8a); }
.card { width: 360px; background: #fff; padding: 40px 32px; border-radius: 12px; box-shadow: 0 20px 60px rgba(0,0,0,.3); }
.brand { display:flex; justify-content:center; }
h2 { text-align:center; margin: 12px 0 24px; color:#232946; }
.tip { color:#999; font-size:12px; text-align:center; margin-top:16px; }
</style>
