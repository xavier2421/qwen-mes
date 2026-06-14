import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '工作台' }
      },
      {
        path: 'customers',
        name: 'Customers',
        component: () => import('@/views/customers/CustomerList.vue'),
        meta: { title: '客户管理' }
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('@/views/orders/OrderList.vue'),
        meta: { title: '销售订单' }
      },
      {
        path: 'production',
        name: 'Production',
        component: () => import('@/views/production/ProductionTask.vue'),
        meta: { title: '生产管理' }
      },
      {
        path: 'warehouse',
        name: 'Warehouse',
        component: () => import('@/views/warehouse/MaterialList.vue'),
        meta: { title: '仓储管理' }
      },
      {
        path: 'purchase',
        name: 'Purchase',
        component: () => import('@/views/purchase/PurchaseOrderList.vue'),
        meta: { title: '采购管理' }
      },
      {
        path: 'delivery',
        name: 'Delivery',
        component: () => import('@/views/delivery/DeliveryList.vue'),
        meta: { title: '发货管理' }
      },
      {
        path: 'finance',
        name: 'Finance',
        component: () => import('@/views/finance/FinanceDashboard.vue'),
        meta: { title: '财务管理' }
      },
      {
        path: 'bi',
        name: 'BI',
        component: () => import('@/views/bi/BIDashboard.vue'),
        meta: { title: 'BI 报表' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
