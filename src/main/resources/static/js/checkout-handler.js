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

      // ✅ ESTRUCTURA CORRECTA: productId, NO product
      const items = carrito.map(p => ({
        productId: p.id,  // ← Usar productId directamente, NO un objeto anidado
        quantity: parseInt(p.cantidad || p.quantity || 1),
        price: parseFloat(p.price)
      }));

      const order = {
        items: items,  // ← Los items ahora tienen estructura correcta
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

document.addEventListener('DOMContentLoaded', function() {
  console.log('[DOMContentLoaded] Inicializando checkout...');
  
  const loader = document.getElementById('ftco-loader');
  if (loader) {
    console.log('[DOMContentLoaded] Ocultando loader');
    loader.classList.remove('show');
  }

  console.log('[DOMContentLoaded] Llamando cargarResumenCarrito()');
  cargarResumenCarrito();

  const checkoutForm = document.getElementById('checkout-form');
  if (checkoutForm) {
    console.log('[DOMContentLoaded] Agregando listener al formulario');
    checkoutForm.addEventListener('submit', procesarCheckout);
  } else {
    console.error('[DOMContentLoaded] Formulario no encontrado');
  }
});

function cargarResumenCarrito() {
  console.log('[cargarResumenCarrito] Iniciando...');
  
  const carritoJSON = localStorage.getItem('carrito');
  console.log('[cargarResumenCarrito] CarritoJSON:', carritoJSON);
  
  const carrito = carritoJSON ? JSON.parse(carritoJSON) : [];
  console.log('[cargarResumenCarrito] Carrito parseado:', carrito);
  
  const resumen = document.getElementById('productos-resumen');
  const subtotalEl = document.getElementById('subtotal');
  const totalEl = document.getElementById('total');
  
  console.log('[cargarResumenCarrito] Elementos encontrados:', {
    resumen: !!resumen,
    subtotalEl: !!subtotalEl,
    totalEl: !!totalEl
  });
  
  if (!resumen) {
    console.error('[cargarResumenCarrito] Contenedor productos-resumen NO encontrado');
    return;
  }
  
  resumen.innerHTML = '';
  let total = 0;
  
  if (carrito.length === 0) {
    console.log('[cargarResumenCarrito] Carrito vacío');
    resumen.innerHTML = '<p class="text-muted">No hay productos en el carrito</p>';
    if (subtotalEl) subtotalEl.textContent = '$0.00';
    if (totalEl) totalEl.textContent = '$0.00';
    return;
  }
  
  console.log('[cargarResumenCarrito] Procesando', carrito.length, 'productos');
  
  carrito.forEach((item, index) => {
    console.log(`[cargarResumenCarrito] Item ${index}:`, item);
    
    const subtotal = item.price * item.cantidad;
    total += subtotal;
    
    resumen.innerHTML += `
      <div style="display: grid; grid-template-columns: 1fr auto; gap: 15px; margin-bottom: 15px; padding-bottom: 15px; border-bottom: 1px solid #eee; align-items: start;">
        <div style="flex: 1; min-width: 0;">
          <p style="margin: 0 0 5px 0; font-weight: bold;">${item.name}</p>
          <p style="margin: 0; color: #666; font-size: 14px; white-space: nowrap;">$${item.price.toFixed(2)} x ${item.cantidad}</p>
        </div>
        <p style="margin: 0; font-weight: bold; white-space: nowrap;">$${subtotal.toFixed(2)}</p>
      </div>
    `;
    console.log(`[cargarResumenCarrito] Item ${index} agregado, subtotal acumulado: $${total.toFixed(2)}`);
  });
  
  console.log('[cargarResumenCarrito] Total final:', total.toFixed(2));
  if (subtotalEl) subtotalEl.textContent = '$' + total.toFixed(2);
  if (totalEl) totalEl.textContent = '$' + total.toFixed(2);
  
  console.log('[cargarResumenCarrito] Completado');
}

function procesarCheckout(e) {
  e.preventDefault();
  
  console.log('[procesarCheckout] Iniciando...');
  
  const token = localStorage.getItem('jwt');
  if (!token) {
    console.log('[procesarCheckout] No hay token, redirigiendo a registro');
    alert('Debes estar registrado para completar la compra. Redirigiendo a registro...');
    window.location.href = 'register.html';
    return;
  }

  const carrito = JSON.parse(localStorage.getItem('carrito') || '[]');
  if (carrito.length === 0) {
    alert('El carrito está vacío');
    return;
  }

  const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
  if (!paymentMethod) {
    alert('Selecciona un método de pago');
    return;
  }

  const formData = {
    firstname: document.getElementById('firstname').value,
    lastname: document.getElementById('lastname').value,
    emailaddress: document.getElementById('emailaddress').value,
    phone: document.getElementById('phone').value,
    streetaddress: document.getElementById('streetaddress').value,
    apartment: document.getElementById('apartment').value || '',
    department: document.getElementById('department').value,
    postcodezip: document.getElementById('postcodezip').value,
    paymentMethod: paymentMethod.value,
    items: carrito
  };

  console.log('[procesarCheckout] FormData:', formData);

  const loader = document.getElementById('ftco-loader');
  if (loader) {
    loader.classList.add('show');
  }

  // Intentar con POST primero
  fetch('http://localhost:8081/api/orders', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    body: JSON.stringify(formData)
  })
  .then(res => {
    console.log('[procesarCheckout] Response status:', res.status);
    
    if (loader) loader.classList.remove('show');
    
    if (res.status === 401) {
      alert('Sesión expirada. Inicia sesión nuevamente.');
      window.location.href = 'login.html';
      return Promise.reject('Unauthorized');
    }
    
    // Si es 500, intentar con GET como fallback
    if (res.status === 500) {
      console.warn('[procesarCheckout] POST retornó 500, intentando GET...');
      return crearOrdenConGET(formData, token);
    }
    
    const contentType = res.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return res.json();
    } else {
      return res.text().then(text => {
        console.log('[procesarCheckout] Response text:', text);
        if (res.ok) {
          return { success: true, id: 'order_created' };
        }
        throw new Error(text || 'Error del servidor');
      });
    }
  })
  .then(data => {
    console.log('[procesarCheckout] Response data:', data);
    
    if (data && (data.id || data.success)) {
      const orderId = data.id || 'creada';
      alert('Pedido creado exitosamente! Número de pedido: ' + orderId);
      localStorage.removeItem('carrito');
      window.location.href = 'index.html';
    } else {
      alert('Error al procesar el pedido: ' + (data.message || 'Respuesta inválida del servidor'));
    }
  })
  .catch(e => {
    if (loader) loader.classList.remove('show');
    console.error('[procesarCheckout] Error:', e);
    alert('Error al procesar el pedido: ' + e.message);
  });
}

function crearOrdenConGET(formData, token) {
  console.log('[crearOrdenConGET] Intentando crear orden con GET...');
  
  // Construir query string
  const params = new URLSearchParams();
  params.append('firstname', formData.firstname);
  params.append('lastname', formData.lastname);
  params.append('emailaddress', formData.emailaddress);
  params.append('phone', formData.phone);
  params.append('streetaddress', formData.streetaddress);
  params.append('apartment', formData.apartment);
  params.append('department', formData.department);
  params.append('postcodezip', formData.postcodezip);
  params.append('paymentMethod', formData.paymentMethod);
  params.append('items', JSON.stringify(formData.items));
  
  return fetch(`http://localhost:8081/api/orders?${params}`, {
    method: 'GET',
    headers: {
      'Authorization': 'Bearer ' + token
    }
  })
  .then(res => res.json())
  .catch(e => {
    console.error('[crearOrdenConGET] Error:', e);
    throw new Error('No se pudo crear la orden');
  });
}
