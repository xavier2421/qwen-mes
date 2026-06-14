import request from './request'

// 登录
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/auth/me',
    method: 'get'
  })
}

// 客户相关
export function getCustomers(params) {
  return request({
    url: '/customers',
    method: 'get',
    params
  })
}

export function createCustomer(data) {
  return request({
    url: '/customers',
    method: 'post',
    data
  })
}

export function updateCustomer(id, data) {
  return request({
    url: `/customers/${id}`,
    method: 'put',
    data
  })
}

export function deleteCustomer(id) {
  return request({
    url: `/customers/${id}`,
    method: 'delete'
  })
}

// 订单相关
export function getOrders(params) {
  return request({
    url: '/orders',
    method: 'get',
    params
  })
}

export function createOrder(data) {
  return request({
    url: '/orders',
    method: 'post',
    data
  })
}

export function getOrderDetail(id) {
  return request({
    url: `/orders/${id}`,
    method: 'get'
  })
}

// 生产相关
export function getProductionTasks(params) {
  return request({
    url: '/production/tasks',
    method: 'get',
    params
  })
}

export function startWork(data) {
  return request({
    url: '/production/work-record/start',
    method: 'post',
    data
  })
}

export function endWork(recordId, data) {
  return request({
    url: `/production/work-record/end/${recordId}`,
    method: 'post',
    data
  })
}
