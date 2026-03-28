<template>
  <el-container class="main-layout">
    <el-aside width="200px" class="sidebar">
      <div class="logo">MES SaaS</div>
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>工作台</span>
        </el-menu-item>
        <el-sub-menu index="sales">
          <template #title>
            <el-icon><ShoppingCart /></el-icon>
            <span>销售管理</span>
          </template>
          <el-menu-item index="/customers">客户管理</el-menu-item>
          <el-menu-item index="/orders">销售订单</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/production">
          <el-icon><Setting /></el-icon>
          <span>生产管理</span>
        </el-menu-item>
        <el-menu-item index="/warehouse">
          <el-icon><Box /></el-icon>
          <span>仓储管理</span>
        </el-menu-item>
        <el-menu-item index="/purchase">
          <el-icon><Document /></el-icon>
          <span>采购管理</span>
        </el-menu-item>
        <el-menu-item index="/delivery">
          <el-icon><Van /></el-icon>
          <span>发货管理</span>
        </el-menu-item>
        <el-menu-item index="/finance">
          <el-icon><Coin /></el-icon>
          <span>财务管理</span>
        </el-menu-item>
        <el-menu-item index="/bi">
          <el-icon><DataAnalysis /></el-icon>
          <span>BI 报表</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <h3>{{ currentTitle }}</h3>
        </div>
        <div class="header-right">
          <span class="username">{{ userInfo?.real_name || userInfo?.username }}</span>
          <el-dropdown @command="handleCommand">
            <el-icon class="avatar"><UserFilled /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '')

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
    })
  }
}
</script>

<style scoped>
.main-layout {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  background-color: #2b3a4b;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}

.header-left h3 {
  margin: 0;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.username {
  color: #606266;
}

.avatar {
  font-size: 24px;
  cursor: pointer;
  color: #409EFF;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
