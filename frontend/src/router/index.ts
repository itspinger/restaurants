import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import EditProfile from '@/views/EditProfile.vue'
import Notifications from '@/views/Notifications.vue'
import Restaurants from '@/views/Restaurants.vue'
import NewRestaurant from '@/views/NewRestaurant.vue'
import Restaurant from '@/views/Restaurant.vue'
import Find from '@/views/Find.vue'
import EditRestaurant from '@/views/EditRestaurant.vue'
import Users from '@/views/Users.vue'
import EditNotification from '@/views/EditNotification.vue'
import ResetPassword from '@/views/ResetPassword.vue'
import ChangePassword from '@/views/ChangePassword.vue'

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
      path: '/notifications/edit/:id',
      name: 'editNotifications',
      component: EditNotification
    },
    {
      path: '/restaurants',
      name: 'restaurants',
      component: Restaurants,
    },
    {
      path: '/restaurant/:id',
      name: 'restaurant',
      component: Restaurant
    },
    {
      path: '/restaurant/edit/:id',
      name: 'restaurantEdit',
      component: EditRestaurant
    },
    {
      path: '/reset-password',
      name: 'resetPassword',
      component: ResetPassword
    },
    {
      path: '/changePassword',
      name: 'changePassword',
      component: ChangePassword,
    },
    {
      path: '/find',
      name: 'find',
      component: Find
    },
    {
      path: '/manage/users',
      name: 'manageUsers',
      component: Users
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
