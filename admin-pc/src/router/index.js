import { createRouter, createWebHashHistory } from 'vue-router'
import MainLayout from '../layout/MainLayout.vue'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '仪表盘' } },
      { path: 'orders', component: () => import('../views/OrderList.vue'), meta: { title: '订单管理' } },
      { path: 'menu', component: () => import('../views/MenuManage.vue'), meta: { title: '菜单管理' } },
      { path: 'inventory', component: () => import('../views/Inventory.vue'), meta: { title: '库存与采购' } },
      { path: 'members', component: () => import('../views/MemberList.vue'), meta: { title: '会员管理' } },
      { path: 'marketing', component: () => import('../views/Marketing.vue'), meta: { title: '营销中心' } },
      { path: 'reports', component: () => import('../views/Reports.vue'), meta: { title: '数据报表' } },
      { path: 'staff', component: () => import('../views/StaffManage.vue'), meta: { title: '员工与权限' } },
      { path: 'settings', component: () => import('../views/Settings.vue'), meta: { title: '系统设置' } },
    ],
  },
]

const router = createRouter({ history: createWebHashHistory(), routes })
router.beforeEach((to) => {
  document.title = (to.meta?.title ? to.meta.title + ' · ' : '') + '奶茶商家后台'
  return true
})
export default router
