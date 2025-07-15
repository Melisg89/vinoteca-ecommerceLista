function fetchProductos() {
    fetch('/api/products')  // Realiza una solicitud GET a /api/productos
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();  // Convierte la respuesta a JSON
        })
        .then(data => {
            // Aquí puedes hacer algo con los datos recibidos
            console.log(data);  // Muestra los datos en la consola
            displayProductos(data);  // Llama a una función para mostrar los productos
        })
        .catch(error => console.error('Error:', error));  // Maneja errores
}

// Función para mostrar los productos en la página
function displayProductos(productos) {
    const productosContainer = document.getElementById('productos-container');
    productos.forEach(producto => {
        const productoElement = document.createElement('div');
        productoElement.className = 'producto';
        productoElement.innerHTML = `
            <h2>${producto.nombre}</h2>
            <p>${producto.descripcion}</p>
            <p>Precio: $${producto.precio}</p>
        `;
        productosContainer.appendChild(productoElement);
    });
}

// Llama a la función cuando el documento está listo
document.addEventListener('DOMContentLoaded', fetchProductos);