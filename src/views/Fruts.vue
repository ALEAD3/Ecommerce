<template>
  <v-container>

    <!-- BUSCADOR -->
    <v-toolbar flat>
      <v-text-field
        v-model="search"
        placeholder="Buscar verdura..."
        prepend-inner-icon="mdi-magnify"
        solo
        clearable
        hide-details
        style="max-width:300px"
      />
    </v-toolbar>

    <!-- SIN DATOS -->
    <v-alert v-if="filteredVegetables.length === 0" type="info" class="ma-4">
      No hay productos disponibles
    </v-alert>

    <!-- PRODUCTOS -->
    <v-container fluid v-else>
      <v-row>

        <v-col
          v-for="item in filteredVegetables"
          :key="item.id"
          cols="12"
          sm="6"
          md="4"
        >

          <v-card class="pa-4 product-card" outlined>

            <!-- Imagen -->
            <div class="text-center">
              <v-img :src="item.img" contain height="180"/>
            </div>

            <!-- Nombre / Precio -->
            <div class="mt-4 text-h6 font-weight-medium">{{ item.name }}</div>
            <div class="grey--text text-caption mb-2">{{ item.weight }}</div>
            <div class="text-h5 font-weight-bold mb-3">${{ item.price }}</div>

            <!-- CONTROLES -->
            <div class="d-flex align-center justify-space-between">

              <!-- Cantidad -->
              <div class="d-flex align-center">
                <v-btn icon small @click="decrease(item)">
                  <v-icon>mdi-minus</v-icon>
                </v-btn>

                <span class="mx-2">{{ item.qty }}</span>

                <v-btn icon small @click="increase(item)">
                  <v-icon color="green">mdi-plus</v-icon>
                </v-btn>
              </div>

              <!-- Botones derecha -->
              <div class="d-flex align-center">

                <!-- ❤️ WISHLIST -->
                <v-btn
                  fab
                  small
                  class="wishlist-btn"
                  @click="addToWishlist(item)"
                >
                  <v-icon color="#FF6D59">mdi-heart</v-icon>
                </v-btn>

                <!-- 🛒 CARRITO -->
                <v-btn
                  color="green"
                  dark
                  fab
                  small
                  class="cart-btn"
                  @click="addToCart(item)"
                >
                  <v-icon>mdi-cart</v-icon>
                </v-btn>

              </div>

            </div>

          </v-card>

        </v-col>
      </v-row>
    </v-container>

    <!-- MODAL -->
    <v-dialog v-model="dialog" max-width="400">
      <v-card class="pa-6 text-center" color="#e8f5e9" outlined>
        <v-icon size="64" color="green">mdi-check-circle</v-icon>
        <h2 class="mt-4">{{ dialogMessage }}</h2>
        <v-btn color="green" class="mt-4" @click="dialog = false">
          Cerrar
        </v-btn>
      </v-card>
    </v-dialog>

  </v-container>
</template>

<script>
export default {

  name: "VegetablesPage",

  data(){
    return{
      search:"",
      vegetables:[],
      dialog:false,
      dialogMessage:""
    }
  },

  computed:{
    filteredVegetables(){
      if(!this.search) return this.vegetables
      return this.vegetables.filter(v =>
        (v.name || "").toLowerCase().includes(this.search.toLowerCase())
      )
    }
  },

  methods:{

    increase(item){
      if(item.qty < item.stock) item.qty++
      else alert("No hay más stock disponible")
  },

    decrease(item){
      if(item.qty > 1) item.qty--
    },

    async addToCart(item){

      try{

        const response = await fetch("http://localhost:8081/api/carrito/agregar",{
          method:"POST",
          headers:{ "Content-Type":"application/x-www-form-urlencoded" },
          body:new URLSearchParams({
            nombre:item.name,
            cantidad:item.qty
          })
        })

        if(!response.ok) throw new Error("Error agregando al carrito")

        this.dialogMessage = "🛒 Producto agregado al carrito"
        this.dialog = true
        item.qty = 1

      }catch(error){
        console.error(error)
        alert("No se pudo agregar al carrito")
      }

    },

    addToWishlist(item){

      let wishlist = JSON.parse(localStorage.getItem("wishlist")) || []

      const existe = wishlist.find(p => p.id === item.id)

      if(!existe){

        const producto = {
          id: item.id,
          name: item.name,
          price: item.price,
          weight: item.weight,
          img: item.img
        }

        wishlist.push(producto)

        localStorage.setItem("wishlist", JSON.stringify(wishlist))

        this.dialogMessage = "❤️ Producto agregado a wishlist"
        this.dialog = true

      }else{

        this.dialogMessage = "❤️ Este producto ya está en tu wishlist"
        this.dialog = true

      }

    },

    async loadProducts(){

      try{

        const response = await fetch("http://localhost:8081/api/productos/categoria/1")

        if(!response.ok) throw new Error("Error al obtener productos")

        const data = await response.json()

        this.vegetables = data.map(product => ({
          id:product.id,
          name:product.nombre,
          weight:product.variedad || "Sin variedad",
          price:product.precio,
          stock:product.stock || 0,
          qty:1,
          img: product.imagen ? `/${product.imagen}` : "/default.webp"        }))

      }catch(error){
        console.error(error)
        this.vegetables = []
      }

    }

  },

  mounted(){
    this.loadProducts()
  }

}
</script>

<style scoped>

.product-card{
  transition:0.2s;
}

.product-card:hover{
  transform:translateY(-5px);
  box-shadow:0 8px 20px rgba(0,0,0,0.1);
}

.cart-btn{
  margin-left:8px;
  border:2px solid #e0e0e0;
}

.wishlist-btn{
  border:2px solid #e0e0e0;
  background:white;
}

</style>