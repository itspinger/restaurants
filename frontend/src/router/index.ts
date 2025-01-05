import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Draw from '@/views/Draw.vue'
import Gallery from '@/views/Gallery.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/draw/:id?',
      name: 'draw',
      component: Draw
    },
    {
      path: '/gallery/:page',
      name: 'gallery',
      component: Gallery
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    }
  ]
})

export default router
