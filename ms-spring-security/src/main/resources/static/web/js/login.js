var contenido = document.getElementById('contenido');

function traer(event) {
        event.preventDefault(); // Evitar envío del formulario
        fetch('http://localhost:9100/users')
            .then(res => res.json())
            .then(datos => {
                tabla(datos);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

function tabla(datos){
    console.log(datos);
}

function accessLogin(event) {


        if((document.querySelector('form').checkValidity())){

            event.preventDefault();
            var email = document.getElementById('email').value;
            var password = document.getElementById('password').value;

            var url = 'http://localhost:9100/login';
            var loginRequest = {
                email: email,
                userPass: password
            };

            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginRequest)
            })
            .then(response => {
            if (response.status === 200) {
                console.log('Inicio de sesión exitoso');
                // Otras acciones después del inicio de sesión exitoso
                // Por ejemplo, redirigir al usuario a la página de inicio después del inicio de sesión exitoso
                window.location.href = '/web/farmer.html';
            } else {
                response.json().then(data => {
                    var errorElement = document.getElementById('error-message');
                    errorElement.textContent = data.message;
                });
            }
            })
            .catch(error => {
                console.error('Error en el inicio de sesión:', error);
                // Otras acciones en caso de error de red u otro error
            });
    }

}

function registration(event) {

    // Obtener los valores de los campos de entrada
    var nombre = document.getElementById('nombre').value;
    var apellido = document.getElementById('apellido').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var roleId = document.getElementById('roles').value;

    var urlRegistroUser = 'http://localhost:9100/user'
    var urlConParametro = `${urlRegistroUser}?roleId=${roleId}`;



    if(!(document.querySelector('form').checkValidity())) {
        alert('El formulario tiene errores');

     } else if (!(validarFormulario(email, password,event))){
        alert('Mail o contraseña no coinciden');
     }
     
    else{
    // Crear un objeto con los datos del usuario
    var usuario = {
        firstName: nombre,
        lastName: apellido,
        email: email,
        userPass: password
    };

    // Realizar la solicitud POST al servidor utilizando fetch
    fetch(urlConParametro, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuario)
    })
    .then(response => {
        if (response.status === 201) {
            console.log('Registro exitoso');
            alert("Registro exitoso");
            window.location.href = "login.html"
        } else {
            console.error('Error en el registro:', response.statusText);
            // Otras acciones en caso de error
        }
    })
    .catch(error => {
        console.error('Error en el registro:', error);
        // Otras acciones en caso de error
    });

}

}



function validarFormulario(email, password,event) {
    event.preventDefault(); //Evita el envio de formulario
    var confirmarPassword = document.getElementById('confirmarPassword').value;
    var confirmarEmail = document.getElementById('confirmarEmail').value;

    if (password !== confirmarPassword || email !== confirmarEmail) {
        return false;
    }

    return true;
}