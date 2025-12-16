console.log('Cart.js loaded');

function getCarrito() {
  const carritoJSON = localStorage.getItem('carrito');
  console.log('[getCarrito] Raw JSON from localStorage:', carritoJSON);
  
  const carrito = carritoJSON ? JSON.parse(carritoJSON) : [];
  console.log('[getCarrito] Parsed carrito array:', carrito);
  console.log('[getCarrito] Total items in cart:', carrito.length);
  
  return carrito;
}

function renderCarrito() {
  console.log('[renderCarrito] Starting render...');
  
  const carrito = getCarrito();
  const cartItemsContainer = document.getElementById('cart-items');
  const totalElement = document.getElementById('total');
  
  console.log('[renderCarrito] Container found:', !!cartItemsContainer);
  console.log('[renderCarrito] Total element found:', !!totalElement);
  
  if (!cartItemsContainer) {
    console.error('[renderCarrito] cart-items container NOT found!');
    return;
  }
  
  // Limpiar contenedor
  cartItemsContainer.innerHTML = '';
  
  if (carrito.length === 0) {
    console.log('[renderCarrito] Carrito vacío - showing empty message');
    cartItemsContainer.innerHTML = '<tr><td colspan="7" class="text-center py-5">Tu carrito está vacío</td></tr>';
    if (totalElement) totalElement.textContent = '$0.00';
    return;
  }
  
  let total = 0;
  
  carrito.forEach((item, index) => {
    console.log(`[renderCarrito] Processing item ${index}:`, item);
    
    const cantidad = item.cantidad || 1;
    const itemTotal = item.price * cantidad;
    total += itemTotal;
    
    console.log(`[renderCarrito] Item ${index}: ${item.name} x ${cantidad} = $${itemTotal.toFixed(2)}`);
    
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
  console.log('[renderCarrito] Final total:', total.toFixed(2));
  if (totalElement) totalElement.textContent = `$${total.toFixed(2)}`;
  
  // Event listeners para botones
  console.log('[renderCarrito] Setting up event listeners...');
  
  document.querySelectorAll('.btn-minus').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const index = parseInt(e.target.getAttribute('data-index'));
      console.log(`[btn-minus] Clicked for index ${index}`);
      decrementItem(index);
    });
  });
  
  document.querySelectorAll('.btn-plus').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const index = parseInt(e.target.getAttribute('data-index'));
      console.log(`[btn-plus] Clicked for index ${index}`);
      incrementItem(index);
    });
  });
  
  document.querySelectorAll('.close-cart').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const index = parseInt(e.target.getAttribute('data-index'));
      console.log(`[close-cart] Clicked for index ${index}`);
      removeItem(index);
    });
  });
  
  console.log('[renderCarrito] Event listeners set up complete');
  
  // Actualizar contador del navbar
  console.log('[renderCarrito] Updating navbar dropdown...');
  actualizarDropdownCarrito();
}

function incrementItem(index) {
  console.log(`[incrementItem] Incrementing item at index ${index}`);
  
  const carrito = getCarrito();
  
  if (!carrito[index]) {
    console.error(`[incrementItem] Item at index ${index} not found!`);
    return;
  }
  
  carrito[index].cantidad = (carrito[index].cantidad || 1) + 1;
  console.log(`[incrementItem] New quantity: ${carrito[index].cantidad}`);
  
  localStorage.setItem('carrito', JSON.stringify(carrito));
  console.log('[incrementItem] Carrito saved to localStorage');
  
  renderCarrito();
}

function decrementItem(index) {
  console.log(`[decrementItem] Decrementing item at index ${index}`);
  
  const carrito = getCarrito();
  
  if (!carrito[index]) {
    console.error(`[decrementItem] Item at index ${index} not found!`);
    return;
  }
  
  const nuevaCantidad = (carrito[index].cantidad || 1) - 1;
  console.log(`[decrementItem] New quantity would be: ${nuevaCantidad}`);
  
  if (nuevaCantidad > 0) {
    carrito[index].cantidad = nuevaCantidad;
    localStorage.setItem('carrito', JSON.stringify(carrito));
    console.log('[decrementItem] Carrito saved to localStorage');
    renderCarrito();
  } else {
    console.log('[decrementItem] Quantity <= 0, removing item instead');
    removeItem(index);
  }
}

function removeItem(index) {
  console.log(`[removeItem] Removing item at index ${index}`);
  
  const carrito = getCarrito();
  
  if (!carrito[index]) {
    console.error(`[removeItem] Item at index ${index} not found!`);
    return;
  }
  
  const removedItem = carrito.splice(index, 1);
  console.log(`[removeItem] Removed item:`, removedItem);
  
  localStorage.setItem('carrito', JSON.stringify(carrito));
  console.log('[removeItem] Carrito saved to localStorage');
  console.log('[removeItem] Remaining items:', carrito.length);
  
  renderCarrito();
}

function actualizarDropdownCarrito() {
  console.log('[actualizarDropdownCarrito] Starting...');
  
  const carrito = getCarrito();
  const dropdown = document.getElementById('cart-dropdown');
  const countBadge = document.getElementById('cart-count');
  
  console.log('[actualizarDropdownCarrito] Dropdown element:', !!dropdown);
  console.log('[actualizarDropdownCarrito] Count badge element:', !!countBadge);
  console.log('[actualizarDropdownCarrito] Items in cart:', carrito.length);
  
  if (!dropdown || !countBadge) {
    console.error('[actualizarDropdownCarrito] Required elements not found');
    return;
  }
  
  countBadge.textContent = carrito.length;
  dropdown.innerHTML = '';
  
  if (carrito.length === 0) {
    console.log('[actualizarDropdownCarrito] Cart is empty');
    dropdown.innerHTML = '<div class="dropdown-item text-center">Carrito vacío</div>';
  } else {
    console.log('[actualizarDropdownCarrito] Rendering', carrito.length, 'items in dropdown');
    
    carrito.forEach((item, idx) => {
      console.log(`[actualizarDropdownCarrito] Item ${idx}: ${item.name} x ${item.cantidad}`);
      
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
  
  console.log('[actualizarDropdownCarrito] Complete');
}

// Ejecutar cuando DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
  console.log('[DOMContentLoaded] Initializing cart page...');
  
  // Hide loader
  const loader = document.getElementById('ftco-loader');
  if (loader) {
    console.log('[DOMContentLoaded] Hiding loader');
    loader.classList.remove('show');
  } else {
    console.warn('[DOMContentLoaded] Loader element not found');
  }
  
  console.log('[DOMContentLoaded] Calling renderCarrito()');
  renderCarrito();
  
  console.log('[DOMContentLoaded] Cart page initialization complete');
});

// Actualizar cada segundo por si cambia en otra pestaña
setInterval(() => {
  console.log('[interval] Running periodic carrito check...');
  const carritoActual = getCarrito();
  console.log('[interval] Current cart items:', carritoActual.length);
  renderCarrito();
}, 1000);
