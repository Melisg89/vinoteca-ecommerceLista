document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwt');
    
    console.log('🔐 VERIFICANDO ACCESO A PÁGINA ADMIN');
    
    if (!token) {
        console.error('❌ No hay token. Redirigiendo a login...');
        alert('Debes iniciar sesión para acceder');
        window.location.href = 'login.html';
        return;
    }
    
    try {
        // Decodificar JWT
        const parts = token.split('.');
        if (parts.length !== 3) {
            throw new Error('Token inválido');
        }
        
        const payload = JSON.parse(atob(parts[1]));
        console.log('📦 Payload del JWT:');
        console.log('   Email:', payload.email);
        console.log('   Role:', payload.role);
        
        // Verificar si es ADMIN
        if (payload.role !== 'ADMIN') {
            console.error('❌ No eres administrador. Tu rol es:', payload.role);
            alert('❌ Acceso denegado. Solo los administradores pueden acceder a esta página.');
            window.location.href = 'index.html';
            return;
        }
        
        console.log('✅ ACCESO PERMITIDO - Eres ADMIN');
        
    } catch (e) {
        console.error('❌ Error decodificando token:', e);
        alert('Token inválido. Por favor, inicia sesión de nuevo.');
        localStorage.removeItem('jwt');
        window.location.href = 'login.html';
    }
});
