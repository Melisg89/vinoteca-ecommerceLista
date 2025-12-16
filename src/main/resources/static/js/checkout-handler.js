console.log('✅ Checkout handler cargado');

function verificarAutenticacion() {
  const token = localStorage.getItem('jwt');
  console.log('🔐 Verificando autenticación...');
  console.log('Token encontrado:', token ? 'SÍ' : 'NO');
  if (!token) {
    console.error('❌ No hay token JWT en localStorage');
    alert('Debes iniciar sesión para proceder al checkout.');
    window.location.href = 'login.html';
    return false;
  }
  console.log('✅ Token válido, primeros 20 caracteres:', token.substring(0, 20) + '...');
  return true;
}

function calcularTotal() {
  const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
  const resumenContainer = document.getElementById("productos-resumen");
  
  console.log('📦 Carrito actual:', carrito);
  
  resumenContainer.innerHTML = "";
  
  if (carrito.length === 0) {
    console.warn('⚠️ Carrito vacío');
    resumenContainer.innerHTML = '<p class="text-center text-muted">No hay productos</p>';
    document.getElementById("subtotal").innerText = "$0.00";
    document.getElementById("total").innerText = "$0.00";
    return;
  }
  
  let subtotal = 0;
  
  carrito.forEach((item, index) => {
    const cantidad = item.cantidad || item.quantity || 1;
    const totalProducto = item.price * cantidad;
    subtotal += totalProducto;
    console.log(`📍 Item ${index + 1}:`, item.name, `- Precio: $${item.price}, Cantidad: ${cantidad}, Total: $${totalProducto}`);
    
    const productoHTML = `
      <div class="d-flex justify-content-between align-items-center mb-3 pb-3" style="border-bottom: 1px solid #eee;">
        <div>
          <p class="mb-1"><strong>${item.name}</strong></p>
          <p class="mb-0 text-muted small">$${item.price.toFixed(2)} x ${cantidad}</p>
        </div>
        <p class="mb-0"><strong>$${totalProducto.toFixed(2)}</strong></p>
      </div>
    `;
    
    resumenContainer.innerHTML += productoHTML;
  });
  
  console.log('💰 Subtotal calculado:', `$${subtotal.toFixed(2)}`);
  document.getElementById("subtotal").innerText = `$${subtotal.toFixed(2)}`;
  document.getElementById("total").innerText = `$${subtotal.toFixed(2)}`;
}

// Asegúrate de que este código se ejecute UNA SOLA VEZ
if (document.getElementById('checkout-form')) {
    document.getElementById('checkout-form').addEventListener('submit', async function(e) {
      e.preventDefault();
      console.log('📝 Formulario enviado - Iniciando validación...');

      if (!verificarAutenticacion()) {
        console.error('❌ Autenticación fallida');
        return;
      }

      const acepta = document.getElementById("aceptaTerminos").checked;
      if (!acepta) {
        console.warn('⚠️ Usuario no aceptó términos');
        alert("Debe aceptar los términos y condiciones.");
        return;
      }
      console.log('✅ Términos aceptados');

      const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
      if (carrito.length === 0) {
        console.warn('⚠️ Carrito vacío');
        alert("No hay productos para comprar.");
        return;
      }
      console.log('✅ Carrito tiene productos:', carrito.length);

      const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
      if (!paymentMethod) {
        console.warn('⚠️ Método de pago no seleccionado');
        alert("Debe seleccionar un método de pago.");
        return;
      }
      console.log('✅ Método de pago seleccionado:', paymentMethod.parentElement.textContent.trim());

      const subtotal = carrito.reduce((acc, p) => acc + (p.price * (p.cantidad || p.quantity || 1)), 0);
      console.log('✅ Subtotal:', `$${subtotal.toFixed(2)}`);

      const items = carrito.map(p => ({
        product: { id: p.id },
        quantity: parseInt(p.cantidad || p.quantity || 1),
        price: parseFloat(p.price)
      }));

      const order = {
        items: items,
        firstname: document.getElementById("firstname").value,
        lastname: document.getElementById("lastname").value,
        department: document.getElementById("department").value,
        streetaddress: document.getElementById("streetaddress").value,
        apartment: document.getElementById("apartment").value,
        postcodezip: document.getElementById("postcodezip").value,
        phone: document.getElementById("phone").value,
        emailaddress: document.getElementById("emailaddress").value,
        paymentMethod: paymentMethod.parentElement.textContent.trim(),
        totalAmount: subtotal
      };

      console.log('📋 Orden completa:', order);

      const token = localStorage.getItem('jwt');
      
      if (!token) {
        console.error('❌ Token no encontrado en localStorage');
        alert('Token no encontrado. Por favor, inicia sesión de nuevo.');
        window.location.href = 'login.html';
        return;
      }
      
      console.log('🔑 Token para envío:', token.substring(0, 30) + '...');
      console.log('📡 Enviando petición a /api/checkout');
      console.log('Headers:', {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token.substring(0, 20) + "..."
      });

      try {
        const response = await fetch("/api/checkout", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
          },
          body: JSON.stringify(order)
        });

        console.log('📊 Respuesta del servidor:');
        console.log('Status:', response.status, response.statusText);

        // Leer el texto primero para debuggear
        const responseText = await response.text();
        console.log('Response text:', responseText);
        
        let data;
        try {
          data = JSON.parse(responseText);
        } catch (e) {
          console.error('❌ Response no es JSON válido:', responseText);
          alert(`Error del servidor (${response.status}): ${responseText.substring(0, 200)}`);
          return;
        }

        if (response.ok) {
          console.log('✅ Compra realizada exitosamente');
          alert("¡Compra realizada con éxito!");
          localStorage.removeItem("carrito");
          window.location.href = "index.html";
        } else {
          console.error('❌ Error del servidor:', data);
          const errorMsg = data.message || "Error desconocido";
          alert(`Error: ${errorMsg}`);
        }
      } catch (err) {
        console.error('❌ Error de conexión/red:', err);
        alert("Error de conexión. Intenta de nuevo.");
      }
    });
}

window.addEventListener('DOMContentLoaded', () => {
  console.log('🚀 Página de checkout cargada');
  verificarAutenticacion();
  calcularTotal();
});
