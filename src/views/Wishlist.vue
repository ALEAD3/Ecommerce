```vue
<template>
<v-container>

<h1 class="mb-6">Mis favoritos ❤️</h1>

<v-alert v-if="wishlist.length === 0" type="info">
No tienes productos guardados
</v-alert>

<v-card
v-for="item in wishlist"
:key="item.id"
class="wishlist-item mb-3 px-3 py-2"
outlined
>

<v-row align="center">

<!-- Imagen pequeña -->
<v-col cols="1" class="d-flex justify-center">
<v-img
:src="item.img || '/default.webp'"
max-height="35"
max-width="35"
contain
/>
</v-col>

<!-- Nombre -->
<v-col cols="5">
<div class="font-weight-medium">{{ item.name }}</div>
<div class="grey--text text-caption">{{ item.weight }}</div>
</v-col>

<!-- Precio -->
<v-col cols="2" class="font-weight-medium">
${{ item.price }}
</v-col>

<!-- Botones -->
<v-col cols="4" class="d-flex align-center">

<v-btn
color="green"
small
class="mr-2"
@click="addToCart(item)"
>
<v-icon left small>mdi-cart</v-icon>
Agregar
</v-btn>

<v-btn icon small @click="remove(item.id)">
<v-icon color="red">mdi-delete</v-icon>
</v-btn>

</v-col>

</v-row>

</v-card>

<!-- NOTIFICACIÓN BONITA -->
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

data(){
return{
wishlist:[],
dialog:false,
dialogMessage:""
}
},

methods:{

loadWishlist(){
this.wishlist = JSON.parse(localStorage.getItem("wishlist")) || []
},

remove(id){

this.wishlist = this.wishlist.filter(p => p.id !== id)

localStorage.setItem("wishlist", JSON.stringify(this.wishlist))

},

async addToCart(item){

try{

const response = await fetch("http://localhost:8081/api/carrito/agregar",{
method:"POST",
headers:{ "Content-Type":"application/x-www-form-urlencoded" },
body:new URLSearchParams({
nombre:item.name,
cantidad:1
})
})

if(!response.ok) throw new Error("Error agregando al carrito")

this.dialogMessage = "🛒 Producto agregado al carrito"
this.dialog = true

}catch(error){
console.error(error)
alert("No se pudo agregar al carrito")
}

}

},

mounted(){
this.loadWishlist()
}

}
</script>

<style scoped>

.wishlist-item{
border-radius:10px;
min-height:60px;
}

</style>
```
