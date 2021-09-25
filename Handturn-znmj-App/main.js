import Vue from 'vue'
import App from './App'
import https from './utils/https.js'
import login from './utils/login.js'
import store from './utils/store.js'
import pageStatus from './pages/components/pageStatus/pageStatus.vue'
import globalData from './utils/global_data.js'
import interfaces from './utils/interfaces.js'

Vue.config.productionTip = false
Vue.prototype.https = https
Vue.prototype.login = login
Vue.prototype.store = store
Vue.prototype.interfaces = interfaces
Vue.prototype.globalData = globalData
Vue.prototype.$eventHub = new Vue();

// 全局注册组件
Vue.component('page-status',pageStatus)

App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()
