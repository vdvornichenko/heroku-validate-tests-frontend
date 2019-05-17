import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import VueResource from 'vue-resource';
import VueCookies from 'vue-cookies'
Vue.use(VueCookies)

// set default config
VueCookies.config('7d')

// set global cookie
VueCookies.set('theme','default');
VueCookies.set('hover-time','1s');
Vue.use(VueResource);

Vue.config.productionTip = false
export const bus = new Vue()

new Vue({
  render: h => h(App),
}).$mount('#app')
