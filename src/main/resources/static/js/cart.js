function getCarrito() {
  const carritoJSON = localStorage.getItem('carrito');
  return carritoJSON ? JSON.parse(carritoJSON) : [];
}

function renderCarrito() {
  const carrito = getCarrito();
  const cartItemsContainer = document.getElementById('cart-items');
  const totalElement = document.getElementById('total');
  
  // Limpiar contenedor
  cartItemsContainer.innerHTML = '';
  
  if (carrito.length === 0) {
    cartItemsContainer.innerHTML = '<tr><td colspan="7" class="text-center py-5">Tu carrito está vacío</td></tr>';
    totalElement.textContent = '$0.00';
    return;
  }
  
  let total = 0;
  
  carrito.forEach((item, index) => {
    const cantidad = item.cantidad || 1;
    const itemTotal = item.price * cantidad;
    total += itemTotal;
    
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>
        <label class="checkbox-wrap checkbox-primary">
          <input type="checkbox" checked>
          <span class="checkmark"></span>
        </label>
      </td>
      <td>
        <div class="img" style="background-image: url('${item.image}'); width: 80px; height: 80px; background-size: cover; background-position: center; border-radius: 8px;"></div>
      </td>
      <td>
        <p>${item.name}</p>
      </td>
      <td>
        <p>$${item.price.toFixed(2)}</p>
      </td>
      <td>
        <div class="input-group quantity">
          <button class="btn-minus" data-index="${index}">-</button>
          <input type="text" class="quantity-input" value="${cantidad}" readonly>
          <button class="btn-plus" data-index="${index}">+</button>
        </div>
      </td>
      <td>
        <p>$${itemTotal.toFixed(2)}</p>
      </td>
      <td>
        <p class="close-cart" data-index="${index}" style="cursor: pointer; color: red; font-weight: bold;">&times;</p>
      </td>
    `;
    
    cartItemsContainer.appendChild(row);
  });
  
  // Actualizar total
  totalElement.textContent = `$${total.toFixed(2)}`;
  
  // Event listeners para botones
  document.querySelectorAll('.btn-minus').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const index = parseInt(e.target.getAttribute('data-index'));
      decrementItem(index);
    });
  });
  
  document.querySelectorAll('.btn-plus').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const index = parseInt(e.target.getAttribute('data-index'));
      incrementItem(index);
    });
  });
  
  document.querySelectorAll('.close-cart').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const index = parseInt(e.target.getAttribute('data-index'));
      removeItem(index);
    });
  });
  
  // Actualizar contador del navbar
  actualizarDropdownCarrito();
}

function incrementItem(index) {
  const carrito = getCarrito();
  if (carrito[index]) {
    carrito[index].cantidad = (carrito[index].cantidad || 1) + 1;
    localStorage.setItem('carrito', JSON.stringify(carrito));
    renderCarrito();
  }
}

function decrementItem(index) {
  const carrito = getCarrito();
  if (carrito[index]) {
    const nuevaCantidad = (carrito[index].cantidad || 1) - 1;
    if (nuevaCantidad > 0) {
      carrito[index].cantidad = nuevaCantidad;
      localStorage.setItem('carrito', JSON.stringify(carrito));
      renderCarrito();
    } else {
      removeItem(index);
    }
  }
}

function removeItem(index) {
  const carrito = getCarrito();
  carrito.splice(index, 1);
  localStorage.setItem('carrito', JSON.stringify(carrito));
  renderCarrito();
}

function actualizarDropdownCarrito() {
  const carrito = getCarrito();
  const dropdown = document.getElementById('cart-dropdown');
  const countBadge = document.getElementById('cart-count');
  
  if (!dropdown || !countBadge) return;
  
  countBadge.textContent = carrito.length;
  dropdown.innerHTML = '';
  
  if (carrito.length === 0) {
    dropdown.innerHTML = '<div class="dropdown-item text-center">Carrito vacío</div>';
  } else {
    carrito.forEach(item => {
      const itemHTML = `
        <div class="dropdown-item d-flex align-items-start">
          <div class="img" style="background-image: url(${item.image}); width: 60px; height: 60px; background-size: cover; background-position: center; border-radius: 4px;"></div>
          <div class="text pl-3">
            <h4>${item.name}</h4>
            <p class="mb-0"><a href="#" class="price">$${item.price.toFixed(2)}</a><span class="quantity ml-3">Cantidad: ${String(item.cantidad || 1).padStart(2, '0')}</span></p>
          </div>
        </div>
      `;
      dropdown.innerHTML += itemHTML;
    });
    
    dropdown.innerHTML += `
      <a class="dropdown-item text-center btn-link d-block w-100" href="cart.html">
        Ver Todo
        <span class="fa fa-long-arrow-right"></span>
      </a>
    `;
  }
}

// Ejecutar cuando DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
  renderCarrito();
  
  // Actualizar cada segundo por si cambia en otra pestaña
  setInterval(renderCarrito, 1000);
});
