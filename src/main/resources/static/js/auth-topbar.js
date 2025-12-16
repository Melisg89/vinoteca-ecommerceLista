document.addEventListener('DOMContentLoaded', function () {
  // Busca el bloque de login/registro en la topbar
  var regDiv = document.querySelector('.reg');
  if (!regDiv) return;

  var token = localStorage.getItem('jwt');
  
  if (token) {
    let email = '';
    let nombre = '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      email = payload.email || '';
      nombre = payload.nombre || payload.name || '';
    } catch (e) {
      console.error('Error decodificando JWT:', e);
    }

    regDiv.innerHTML = `
      <div class="dropdown" style="display: inline-block; position: relative; z-index: 1050;">
        <a href="#" class="dropdown-toggle" id="userDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="font-size:24px; color:#fff; text-decoration:none;">
          <span class="fa fa-user-circle"></span>
        </a>
        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown" style="background-color:#fff; color:#333; position: absolute; top: 100%; right: 0; z-index: 1051; min-width: 250px; padding: 10px 0;">
          <div class="dropdown-header" style="padding: 10px 15px;">
            <strong>${nombre || 'Usuario'}</strong><br>
            <small style="color:#888;">${email}</small>
          </div>
          <div class="dropdown-divider" style="margin: 5px 0;"></div>
          <a href="#" class="dropdown-item" id="logoutBtn" style="color:#333; display: block; padding: 10px 15px; text-decoration: none;">Cerrar sesión</a>
        </div>
      </div>
    `;

    document.getElementById('logoutBtn').addEventListener('click', function (e) {
      e.preventDefault();
      localStorage.removeItem('jwt');
      window.location.reload();
    });
  } else {
    regDiv.innerHTML = `<p class="mb-0"><a href="login.html" class="btn btn-primary btn-sm mr-2">Ingresar</a> <a href="register.html" class="btn btn-outline-light btn-sm">Registrarse</a></p>`;
  }
});
