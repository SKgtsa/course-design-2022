import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
/* import store from '/store' */
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/display.css'
import 'element-plus/dist/index.css'
import './assets/main.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import qs from 'qs'
import Vue3TouchEvents from "vue3-touch-events";
import VueCropper from "vue-cropper";
const app = createApp(App)
app.config.globalProperties.$qs = qs
app.use(router)

app.use(ElementPlus)

app.use(Vue3TouchEvents)

app.use(VueCropper)

app.mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

