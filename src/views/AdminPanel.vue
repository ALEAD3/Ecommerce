<template>
<v-container>

<h1 class="mb-6">Panel Admin - Productos</h1>

<v-btn
color="green"
class="mb-6"
@click="abrirDialogo"
>
Agregar Producto
</v-btn>

<v-row>

<v-col
v-for="p in productos"
:key="p.id"
cols="12"
sm="6"
md="4"
lg="3"
>

<v-card elevation="3">

<v-card-title>
{{ p.nombre }}
</v-card-title>

<v-card-text>

<p><b>Precio:</b> ${{ p.precio }}</p>
<p>{{ p.descripcion }}</p>

</v-card-text>

<v-card-actions>

<v-btn
color="blue"
text
@click="editarProducto(p)"
>
Editar
</v-btn>

<v-btn
color="red"
text
@click="eliminarProducto(p.id)"
>
Eliminar
</v-btn>

</v-card-actions>

</v-card>

</v-col>

</v-row>

<!-- DIALOGO -->

<v-dialog v-model="dialog" max-width="500">

<v-card>

<v-card-title>
{{ editando ? "Editar Producto" : "Nuevo Producto" }}
</v-card-title>

<v-card-text>

<v-text-field
v-model="producto.nombre"
label="Nombre"
/>

<v-text-field
v-model="producto.precio"
label="Precio"
type="number"
/>

<v-textarea
v-model="producto.descripcion"
label="Descripción"
/>

</v-card-text>

<v-card-actions>

<v-spacer/>

<v-btn color="grey" @click="dialog=false">
Cancelar
</v-btn>

<v-btn color="green" @click="guardarProducto">
Guardar
</v-btn>

</v-card-actions>

</v-card>

</v-dialog>

</v-container>
</template>

<script>
export default {

data(){
return{

productos:[],

dialog:false,

editando:false,

producto:{
id:null,
nombre:"",
precio:0,
descripcion:""
}

}
},

mounted(){
this.cargarProductos()
},

methods:{

async cargarProductos(){

const res = await fetch("http://localhost:8081/api/productos",{
credentials:"include"
})

this.productos = await res.json()

},

abrirDialogo(){

this.editando=false

this.producto={
id:null,
nombre:"",
precio:0,
descripcion:""
}

this.dialog=true

},

editarProducto(item){

this.editando=true
this.producto={...item}
this.dialog=true

},

async guardarProducto(){

const url = this.editando
? `http://localhost:8081/api/productos/${this.producto.id}`
: `http://localhost:8081/api/productos`

const method = this.editando ? "PUT" : "POST"

await fetch(url,{
method:method,
credentials:"include",
headers:{
"Content-Type":"application/json"
},
body:JSON.stringify(this.producto)
})

this.dialog=false
this.cargarProductos()

},

async eliminarProducto(id){

if(!confirm("¿Eliminar producto?")) return

await fetch(`http://localhost:8081/api/productos/${id}`,{
method:"DELETE",
credentials:"include"
})

this.cargarProductos()

}

}

}
</script>