import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import EditProfile from '@/views/EditProfile.vue'
import Notifications from '@/views/Notifications.vue'
import Restaurants from '@/views/Restaurants.vue'
import NewRestaurant from '@/views/NewRestaurant.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/notifications',
      name: 'notifications',
      component: Notifications,
    },
    {
      path: '/restaurants',
      name: 'restaurants',
      component: Restaurants,
    },
    {
      path: '/restaurants/new',
      name: 'newRestaurant',
      component: NewRestaurant
    },
    {
      path: '/profile',
      name: 'profile',
      component: EditProfile
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    }
  ]
})

export default router
