const path = require('path')
const setting = require('./src/setting')
function resolve(dir) {
    return path.join(__dirname, dir)
}

module.exports = {
    publicPath: '/',
    lintOnSave: false,
    productionSourceMap: false,
    configureWebpack: {
        devtool: 'source-map',
        name: 'Home',
        resolve: {
            alias: {
                '@': resolve('src')
            }
        }
    }
    // 不使用 devServer proxy，后端已配置 CORS，前端 axios 直连后端
    // axios baseURL 由 config.js → setting.proxyUrl (http://localhost:8088)
}
