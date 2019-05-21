<template>
  <v-app>
      <router-view></router-view>
  </v-app>
</template>

<script>
import StartPage from './components/StartPage'

export default {
  name: 'App',
  components: {
      StartPage
  },
  data () {
    return {
      //
    }
  }
}
</script>
