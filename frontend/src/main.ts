import { createApp } from 'vue'
import { createVfm } from 'vue-final-modal'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import axios from 'axios'

import '@f3ve/vue-notify/dist/style.css';

import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

import PulseLoader from 'vue-spinner/src/PulseLoader.vue'

import 'vue-final-modal/style.css'
import "./assets/style.css";

// Set the default base url for the API
axios.defaults.baseURL = 'http://localhost:8084';

const vuetify = createVuetify({
    components,
    directives,
    icons: {
        defaultSet: 'mdi',
    },
})

const app = createApp(App)
const vfm = createVfm()

app.component('pulse-loader', PulseLoader);
app.use(createPinia())
app.use(router)
app.use(vfm)
app.use(vuetify)

app.mount('#app')
