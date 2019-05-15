<template>
  <v-app id="sandbox" :dark="dark">
    <Header @changeTheme="dark=!dark"/>
    <v-layout row wrap>
      <v-flex lg2></v-flex>
      <v-flex lg8>
        <v-card style="min-height: 100%; margin-bottom:30px">
        <v-container>
            <div style="min-height: 100%; padding-top: 50px; position: relative">
              <AlertComponent />
              <CallbackSpinner />
              <UsersTable />
              <Results />
              <FilesCmp/>
            </div>
        </v-container>
        </v-card>
      </v-flex>
      <v-flex lg2/>
    </v-layout>
    <Footer />
  </v-app>
</template>

<script>
  import UsersTable from './UsersTable'
  import Results from './Results'
  import Header from './Header'
  import AlertComponent from './AlertComponent'
  import CallbackSpinner from './CallbackSpinner'
  import Footer from './FooterCmp'
  import FilesCmp from './FilesComponent'
  export default {
    components: {
      UsersTable,
      Results,
      Header,
      AlertComponent,
      CallbackSpinner,
      Footer,
      FilesCmp
    },
    data: () => ({
      ecosystem: [
        {
          text: 'vuetify-loader',
          href: 'https://github.com/vuetifyjs/vuetify-loader'
        },
        {
          text: 'github',
          href: 'https://github.com/vuetifyjs/vuetify'
        },
        {
          text: 'awesome-vuetify',
          href: 'https://github.com/vuetifyjs/awesome-vuetify'
        }
      ],
      importantLinks: [
        {
          text: 'Documentation',
          href: 'https://vuetifyjs.com'
        },
        {
          text: 'Chat',
          href: 'https://community.vuetifyjs.com'
        },
        {
          text: 'Made with Vuetify',
          href: 'https://madewithvuetifyjs.com'
        },
        {
          text: 'Twitter',
          href: 'https://twitter.com/vuetifyjs'
        },
        {
          text: 'Articles',
          href: 'https://medium.com/vuetify'
        }
      ],
      whatsNext: [
        {
          text: 'Explore components',
          href: 'https://vuetifyjs.com/components/api-explorer'
        },
        {
          text: 'Select a layout',
          href: 'https://vuetifyjs.com/layout/pre-defined'
        },
        {
          text: 'Frequently Asked Questions',
          href: 'https://vuetifyjs.com/getting-started/frequently-asked-questions'
        }

      ],
      dark: true
    })
  }
</script>

<style>

</style>
