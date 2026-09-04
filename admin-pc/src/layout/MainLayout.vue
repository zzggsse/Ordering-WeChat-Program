<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <el-icon :size="24" color="#ffd04b"><Cup /></el-icon>
        <span>奶茶点单</span>
      </div>
      <el-menu :default-active="$route.path" router background-color="#232946"
        text-color="#c0c4cc" active-text-color="#ffd04b" class="menu">
        <el-menu-item index="/dashboard"><el-icon><Odometer /></el-icon>仪表盘</el-menu-item>
        <el-menu-item index="/orders"><el-icon><List /></el-icon>订单管理</el-menu-item>
        <el-menu-item index="/menu"><el-icon><Food /></el-icon>菜单管理</el-menu-item>
        <el-menu-item index="/inventory"><el-icon><Box /></el-icon>库存与采购</el-menu-item>
        <el-menu-item index="/members"><el-icon><User /></el-icon>会员管理</el-menu-item>
        <el-menu-item index="/marketing"><el-icon><Present /></el-icon>营销中心</el-menu-item>
        <el-menu-item index="/reports"><el-icon><TrendCharts /></el-icon>数据报表</el-menu-item>
        <el-menu-item index="/staff"><el-icon><Avatar /></el-icon>员工与权限</el-menu-item>
        <el-menu-item index="/settings"><el-icon><Setting /></el-icon>系统设置</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="bread">{{ $route.meta.title }}</div>
        <div class="right">
          <el-tag type="success" effect="light">门店：中心店（示例）</el-tag>
          <el-dropdown @command="onCmd">
            <span class="user"><el-icon><UserFilled /></el-icon> 店长 小倩</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
function onCmd(cmd) {
  if (cmd === 'logout') router.push('/login')
}
</script>

<style scoped>
.layout { height: 100vh; }
.aside { background: #232946; display: flex; flex-direction: column; }
.logo { height: 60px; display: flex; align-items: center; gap: 10px; color: #fff; font-weight: 600; padding-left: 20px; }
.menu { border-right: none; flex: 1; }
.header { background: #fff; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #eee; }
.bread { font-size: 16px; font-weight: 600; }
.right { display: flex; align-items: center; gap: 16px; }
.user { cursor: pointer; display: inline-flex; align-items: center; gap: 6px; color: #333; }
.main { background: #f5f7fa; padding: 16px; overflow: auto; }
</style>
