import Vue from 'vue'
import Vuelidate from 'vuelidate'
Vue.use(Vuelidate)
import './plugins/vuetify'
import App from './App.vue'
import VueResource from 'vue-resource';
import VueCookies from 'vue-cookies'
import {router} from './routing/router'
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
  router,
  render: h => h(App),
}).$mount('#app')

import Router from 'vue-router'
import goTo from 'vuetify/lib/components/Vuetify/goTo'

export default new Router({
  scrollBehavior: (to, from, savedPosition) => {
    let scrollTo = 0

    if (to.hash) {
      scrollTo = to.hash
    } else if (savedPosition) {
      scrollTo = savedPosition.y
    }

    return goTo(scrollTo)
  },
  routes: [
    //
  ]
})
