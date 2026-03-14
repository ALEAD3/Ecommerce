import Vue from "vue"
import VueRouter from "vue-router"

import Home from "../views/Home.vue"
import Cart from "@/views/Cart.vue"
import Wishlist from "@/views/Wishlist.vue"
import Fruts from "@/views/Fruts.vue"
import Vegetables from "@/views/Vegetables.vue"
import Checkout from "@/views/Checkout.vue"
import PagoExitoso from "@/components/PagoExitoso.vue"
import Pedidos from "@/views/Pedidos.vue"
import AdminPanel from "@/views/AdminPanel.vue"

Vue.use(VueRouter)

const routes = [
  { path: "/", name: "Home", component: Home },
  { path: "/cart", name: "Cart", component: Cart },
  { path: "/wishlist", name: "Wishlist", component: Wishlist },

  // categorías
  { path: "/category/fruits", name: "Fruits", component: Fruts },
  { path: "/category/vegetables", name: "Vegetables", component: Vegetables },

  // compra
  { path: "/checkout", name: "Checkout", component: Checkout },
  { path: "/pago-exitoso", name: "PagoExitoso", component: PagoExitoso },

  // pedidos
  { path: "/pedidos", name: "Pedidos", component: Pedidos },

  // admin
  { path: "/admin", name: "AdminPanel", component: AdminPanel }
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
})

export default router