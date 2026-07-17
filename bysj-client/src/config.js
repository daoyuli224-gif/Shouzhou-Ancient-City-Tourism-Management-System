var setting = require('./setting')

// 开发环境：axios baseURL 指向后端直连（后端已配置 CORS）
// 生产环境：前端打包后与后端同源部署，baseURL 为空
const config = {
    service_url: process.env.NODE_ENV === 'production' ? '' : setting.proxyUrl,
}

export default config
