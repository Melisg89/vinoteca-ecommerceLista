document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwt');
    
    console.log('🔐 VERIFICANDO ROL ADMIN');
    console.log('Token existente:', token ? '✅ SÍ' : '❌ NO');
    
    if (token) {
        try {
            // Decodificar JWT (sin verificar firma)
            const parts = token.split('.');
            if (parts.length !== 3) {
                console.error('❌ Token JWT inválido (no tiene 3 partes)');
                return;
            }

            const payload = JSON.parse(atob(parts[1]));
            console.log('📦 Payload del JWT:');
            console.log('   userId:', payload.sub);
            console.log('   email:', payload.email);
            console.log('   nombre:', payload.nombre);
            console.log('   role:', payload.role);
            
            const role = payload.role || 'CLIENTE';
            console.log('🛡️  Rol del usuario:', role);
            console.log('¿Es ADMIN?:', role === 'ADMIN' ? '✅ SÍ' : '❌ NO');
            
            // Buscar la navbar
            const navbar = document.querySelector('.navbar-nav');
            if (navbar) {
                if (role === 'ADMIN') {
                    console.log('✅ AGREGANDO DROPDOWN ADMIN');
                    const adminDropdown = `
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="adminDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                🛡️ Admin
                            </a>
                            <div class="dropdown-menu" aria-labelledby="adminDropdown">
                                <a class="dropdown-item" href="admin-products.html">📦 Gestionar Productos</a>
                                <a class="dropdown-item" href="admin-orders.html">📋 Gestionar Pedidos</a>
                            </div>
                        </li>
                    `;
                    navbar.innerHTML += adminDropdown;
                    console.log('✅ Dropdown admin agregado correctamente');
                } else {
                    console.log('ℹ️  Usuario no es admin, no mostrando opciones de admin');
                }
            } else {
                console.error('❌ No se encontró elemento .navbar-nav');
            }
        } catch (e) {
            console.error('❌ Error decodificando token:', e);
            console.error('Token (primeros 50 chars):', token.substring(0, 50) + '...');
        }
    } else {
        console.log('ℹ️  No hay token en localStorage');
    }
});
