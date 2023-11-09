const myFarmsTable = document.getElementById('farmsTable');
const mySalesTable = document.getElementById('salesTable');
const myPurchasesTable = document.getElementById('purchasesTable');
const tablaEstadoGallinas = document.getElementById('tablaEstadoGallinas');
const tablaEstadoHuevos = document.getElementById('tablaEstadoHuevos');
const myFormConfiguration = document.getElementById('formContainer');
const botonEditar = document.getElementById('botonEditar');
const botonGuardar = document.getElementById('botonGuardar');
const configMenu = document.querySelector('.role-menu-item');
const navLinks = document.querySelectorAll('.menu-links li.nav-link');
const sectionText = document.getElementById('sectionText');
const openPopupButton = document.getElementById('openAddFarmFormButton');
const popupOverlay = document.getElementById('popupOverlay');
const closePopupButton = document.getElementById('closePopup');
const cerrarFormButton = document.getElementById('cerrarFormButton');
const addFarmForm = document.getElementById('addFarmForm');
const guardarFormButton = document.getElementById('guardarFormButton');
const openAddChickenButton = document.getElementById('openAddChickenButton');
const openAddEggsButton = document.getElementById('openAddEggsButton');
const passingTimeButton = document.getElementById('passingTimeButton');
const ejecutarCompraButton = document.getElementById('comprarProductosButton');
const compraForm = document.getElementById('compraForm');
const venderProductosButton = document.getElementById('venderProductosButton');
const precioVentaUnitarioGallina = document.getElementById('precioVentaGallina');
const precioVentaUnitarioHuevo = document.getElementById('precioVentaHuevo');
const precioCompraUnitarioGallina = document.getElementById('precioCompraGallina');
const precioCompraUnitarioHuevo = document.getElementById('precioCompraHuevo');
const productoSeleccionadoVenta = document.getElementById("productosVenta");
const productoSeleccionadoCompra = document.getElementById("productosCompra");



var userId;
var userRole;
var userFirstName;
var userLastName;
var farmIdConst;
var urlFarmsUser;
var urlComprasUser;
var urlVentasUser;
var text;
var urlFarmConfig = 'http://localhost:9100/getFarmServiceConfig';




// Mostrar la tabla de "Mis Granjas" por defecto
hideTable(myFarmsTable);
hideTable(mySalesTable);
hideTable(myPurchasesTable);
hideTable(myFormConfiguration);
hideTable(openPopupButton);
hideTable(cerrarFormButton);
hideTable(guardarFormButton);
hideTable(addFarmForm);
hideTable(openAddChickenButton);
hideTable(openAddEggsButton);
hideTable(passingTimeButton);
hideTable(ejecutarCompraButton);
hideTable(tablaEstadoGallinas);
hideTable(tablaEstadoHuevos);
obtenerUserAuth();
getFarmConfig();


document.addEventListener("DOMContentLoaded", function () {
    const openAddFarmFormButton = document.getElementById('openAddFarmFormButton');
    const closeChickenEggsPopup = document.getElementById('closeChickenEggsPopup');
    const closeFarmPopup = document.getElementById('closeFarmPopup');
    const closeCompraPopup = document.getElementById('closeCompraPopup');
    const addFarmPopup = document.getElementById('addFarmPopup');
    const cerrarChickenFormButton = document.getElementById('cerrarChickenFormButton');
    const addChickensEggsPopup = document.getElementById('addChickensEggsPopup');
    const guardarChickenFormButton = document.getElementById('guardarChickenFormButton');
    const passingTimePopUp = document.getElementById('passingTimePopUp');
    const passingTimeForm = document.getElementById('passingTimeForm');
    const cerrarPassingTimeButton = document.getElementById('cerrarPassingTimeButton');
    const ejecutarPassingTimeButton = document.getElementById('ejecutarPassingTimeButton');
    const closePassingTime = document.getElementById('closePassingTime');
    const realizarCompraPopUp = document.getElementById('realizarCompraPopup');
    const cerrarCompraButton = document.getElementById('cerrarCompraButton');
    const ejecutarCompraButton = document.getElementById('comprarProductosButton');



    openAddFarmFormButton.addEventListener("click", function () {
        addFarmPopup.style.display = 'block'; // Mostrar el formulario de agregar granjas
        showTable(cerrarFormButton);
        showTable(guardarFormButton);
        showTable(addFarmForm);
    });

    ejecutarCompraButton.addEventListener("click", function () {
        realizarCompraPopUp.style.display = 'block'; // Mostrar el formulario de agregar granjas
        showTable(cerrarCompraButton);
        showTable(ejecutarCompraButton);
        showTable(compraForm);
    });

    openAddChickenButton.addEventListener("click", function () {
        addChickensEggsPopup.style.display = 'block'; // Mostrar el formulario de cargar gallinas
        hideTable(guardarEggFormButton);
        showTable(cerrarChickenFormButton);
        showTable(guardarChickenFormButton);
        showTable(addChickenEggsForm);

    });

    passingTimeButton.addEventListener("click", function () {
        passingTimePopUp.style.display = 'block'; // Mostrar el formulario de paso del tiempo
        showTable(cerrarPassingTimeButton);
        showTable(ejecutarPassingTimeButton);
        showTable(passingTimeForm);
    });

    openAddEggsButton.addEventListener("click", function () {
        addChickensEggsPopup.style.display = 'block'; // Mostrar el formulario de cargar huevos
        hideTable(guardarChickenFormButton);
        showTable(cerrarChickenFormButton);
        showTable(guardarEggFormButton);
        showTable(addChickenEggsForm);

    });
    cerrarChickenFormButton.addEventListener("click", function () {
        addChickensEggsPopup.style.display = 'none'; // Ocultar el formulario de agregar granjas

    });

    cerrarCompraButton.addEventListener("click", function () {
        realizarCompraPopUp.style.display = 'none'; // Ocultar el formulario de agregar granjas

    });

    cerrarFormButton.addEventListener("click", function () {
        addFarmPopup.style.display = 'none'; // Ocultar el formulario de agregar granjas

    });

    cerrarPassingTimeButton.addEventListener("click", function () {
        passingTimePopUp.style.display = 'none'; // Ocultar el formulario de agregar granjas al hacer clic en la cruz
    });

    closeFarmPopup.addEventListener("click", function () {
        addFarmPopup.style.display = 'none'; // Ocultar el formulario de agregar granjas al hacer clic en la cruz
    });

    closeCompraPopup.addEventListener("click", function () {
        realizarCompraPopUp.style.display = 'none'; // Ocultar el formulario de agregar granjas al hacer clic en la cruz
        productoSeleccionado.value=""
    });


    closePassingTime.addEventListener("click", function () {
        passingTimePopUp.style.display = 'none'; // Ocultar el formulario de agregar granjas al hacer clic en la cruz
    });

    closeChickenEggsPopup.addEventListener("click", function () {
        addChickensEggsPopup.style.display = 'none'; // Ocultar el formulario de agregar granjas al hacer clic en la cruz
    });




});







// Escuchar el evento de clic en los enlaces
navLinks.forEach(link => {
    link.addEventListener('click', () => {
        const section = link.getAttribute('data-section');
        if (section === 'granjas') {
            showTable(myFarmsTable);
            hideTable(mySalesTable);
            hideTable(myPurchasesTable);
            hideTable(myFormConfiguration);
            hideTable(openAddChickenButton);
            hideTable(openAddEggsButton);
            hideTable(passingTimeButton);
            cambiarSectionText('Mis granjas');
            showTable(openPopupButton);
            showTable(ejecutarCompraButton);
            showTable(tablaEstadoGallinas);
            showTable(tablaEstadoHuevos);
            obtenerFarmsUser(urlFarmsUser);
        } else if (section === 'ventas') {
            showTable(mySalesTable);
            hideTable(myFarmsTable);
            hideTable(myPurchasesTable);
            hideTable(myFormConfiguration);
            hideTable(openPopupButton);
            hideTable(cerrarFormButton);
            hideTable(guardarFormButton);
            hideTable(openAddChickenButton);
            hideTable(openAddEggsButton);
            hideTable(passingTimeButton);
            hideTable(ejecutarCompraButton);
            cambiarSectionText('Mis Ventas');
            obtenerVentasUser(urlVentasUser);
        } else if (section === 'compras') {
            hideTable(myFarmsTable);
            hideTable(mySalesTable);
            showTable(myPurchasesTable);
            hideTable(myFormConfiguration);
            hideTable(cerrarFormButton);
            hideTable(guardarFormButton);
            hideTable(openPopupButton);
            hideTable(openAddChickenButton);
            hideTable(openAddEggsButton);
            hideTable(passingTimeButton);
            hideTable(ejecutarCompraButton);
            cambiarSectionText('Mis Compras');
            obtenerComprasUser(urlComprasUser);
        } else if (section === 'configuracion') {
            hideTable(myFarmsTable);
            hideTable(mySalesTable);
            hideTable(myPurchasesTable);
            showTable(myFormConfiguration);
            hideTable(cerrarFormButton);
            hideTable(guardarFormButton);
            hideTable(openPopupButton);
            showTable(openAddChickenButton);
            showTable(openAddEggsButton);
            showTable(passingTimeButton);
            hideTable(ejecutarCompraButton);
            cambiarSectionText('Configuración');
        }

    });
});


function actualizarPrecioUnitarioCompra() {
    var productoSeleccionadoValue = productoSeleccionadoCompra.value;
    var precioUnitarioInput = document.getElementById("precioUnitarioCompra");
    var precioTotalInput = document.getElementById("precioTotalCompra");
    var cantidadCompra = document.getElementById("cantidadCompra");
    var cantidadCompraNumber = parseInt(cantidadCompra.value);

    // Obtener los valores numéricos de los elementos de precioVentaUnitario y precioCompraUnitario
    var precioVentaUnitarioGallinaNumber = parseFloat(precioVentaUnitarioGallina.value);
    var precioVentaUnitarioHuevoNumber = parseFloat(precioVentaUnitarioHuevo.value);


    switch (productoSeleccionadoValue) {
        case "Gallinas":
            precioUnitarioInput.value = precioVentaUnitarioGallinaNumber; // Mostrar el valor con dos decimales
            precioTotalInput.value = cantidadCompraNumber * precioVentaUnitarioGallinaNumber; // Calcular y mostrar el precio total
            break;
        case "Huevos":
            precioUnitarioInput.value = precioVentaUnitarioHuevoNumber; // Mostrar el valor con dos decimales
            precioTotalInput.value = cantidadCompraNumber * precioVentaUnitarioHuevoNumber; // Puedes dejar el precio total en blanco para Huevos o calcularlo si es necesario
            break;
        default:
            precioUnitarioInput.value = "";
            precioTotalInput.value = "";
            cantidadCompra.value = "";
    }
}


function actualizarPrecioUnitarioVenta() {
    var productoSeleccionadoValor = productoSeleccionadoVenta.value;
    var precioUnitarioInput = document.getElementById("precioUnitarioVenta");
    var precioTotalInput = document.getElementById("precioTotalVenta");
    var cantidadVenta = document.getElementById("cantidadVenta");
    var cantidadVentaNumber = parseInt(cantidadVenta.value);


    var precioCompraUnitarioGallinaNumber = parseFloat(precioCompraUnitarioGallina.value);
    var precioCompraUnitarioHuevoNumber = parseFloat(precioCompraUnitarioHuevo.value);

    switch (productoSeleccionadoValor) {
        case "Gallinas":
            precioUnitarioInput.value = precioCompraUnitarioGallinaNumber;
            precioTotalInput.value = cantidadVentaNumber * precioCompraUnitarioGallinaNumber;
            break;
        case "Huevos":
            precioUnitarioInput.value = precioCompraUnitarioHuevoNumber;
            precioTotalInput.value = cantidadVentaNumber * precioCompraUnitarioHuevoNumber;
            break;
        default:
            precioUnitarioInput.value = "";
            precioTotalInput.value = "";
            cantidadVenta.value = "";
    }
}









// Función para mostrar una tabla y ocultar otra
function showTable(table) {
    table.classList.remove('hidden');
}

function hideTable(table) {
    table.classList.add('hidden');
}



function setupSidebar() {
    const body = document.querySelector('body'),
      sidebar = body.querySelector('nav'),
      toggle = body.querySelector(".toggle");

      sidebar.classList.remove("close");
      toggle.addEventListener("click" , () =>{
            sidebar.classList.toggle("close");
    });

  }

function cambiarSectionText(text){
    sectionText.textContent = text;
}


// Llamada a la función para configurar el menú lateral
setupSidebar();

function obtenerUserAuth() {

 fetch('http://localhost:9100/getAuthUser')
    .then(res => res.json())
    .then(datos => {
        userId = datos.id;
        userRole = datos.role.roleName;
        userFirstName = datos.firstName;
        userLastName = datos.lastName;
        urlFarmsUser = 'http://localhost:9100/farmsFromFarmer/' + userId;
        urlComprasUser = 'http://localhost:9100/historial/compra/' + userId;
        urlVentasUser = 'http://localhost:9100/historial/venta/' + userId;
        cambiarSectionText('Bienvenido!' +' '+ userFirstName + ' ' +userLastName);
        if (userRole !== "ADMIN") {
            configMenu.style.display = 'none';
        }
        obtenerFarmsUser(urlFarmsUser);
                })
    .catch(error => {
        console.error('Error:', error);
    });



}


//Obtener datos Granjas
function obtenerFarmsUser() {

    fetch(urlFarmsUser)
    .then(res => {
        if (!res.ok) {
            throw new Error('El servidor no responde');
        }
        return res.json();
    })
    .then(datos => {
        if (datos) {
            tablaFarms(datos);
            granjasDisponibles(datos);
        } else {
            console.warn('La respuesta JSON está vacía.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

//Obtener datos Compras
function obtenerComprasUser() {

    fetch(urlComprasUser)
    .then(res => {
        if (!res.ok) {
            throw new Error('El servidor no responde');
        }
        return res.json();
    })
    .then(datos => {
        if (datos) {
            tablaCompras(datos);
        } else {
            console.warn('La respuesta JSON está vacía.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}


//Obtener datos Ventas
function obtenerVentasUser() {

    fetch(urlVentasUser)
    .then(res => {
        if (!res.ok) {
            throw new Error('El servidor no responde');
        }
        return res.json();
    })
    .then(datos => {
        if (datos) {
            tablaVentas(datos);
        } else {
            console.warn('La respuesta JSON está vacía.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function obtenerEstados(valorFarmId) {
   var urlFarm = 'http://localhost:9100/farm/' + valorFarmId;

    fetch(urlFarm)
    .then(res => {
        if (!res.ok) {
            throw new Error('El servidor no responde');
        }
        return res.json();
    })
    .then(datos => {
        if (datos) {
            completarEstados(datos);
        } else {
            console.warn('La respuesta JSON está vacía.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}


function farmConfigItems(){

    botonEditar.disabled = false;
    botonGuardar.disabled = true;
    const formInputs = document.querySelectorAll('#configForm input');
        formInputs.forEach(input => {
              input.readOnly = true;
              input.classList.add('read-only');
        });


}


function habilitarEdicion(){

        botonEditar.disabled = true;
        botonGuardar.disabled = false;
        const formInputs = document.querySelectorAll('#configForm input');
            formInputs.forEach(input => {
                  input.readOnly = false;
                  input.classList.remove('read-only');
            });


}

function getFarmConfig() {

    fetch(urlFarmConfig)
    .then(res => res.json())
    .then(datos => {
        if (datos !== null) {
            precioCompraUnitarioGallina.value = datos.purchasePriceChicken;
            precioCompraUnitarioHuevo.value = datos.purchasePriceEgg;
            precioVentaUnitarioGallina.value = datos.sellPriceChicken;
            precioVentaUnitarioHuevo.value = datos.sellPriceEgg;
            document.getElementById('diasXHuevos').value = datos.amountDaysToPutEggs;
            document.getElementById('cantHuevosXDias').value = datos.amountEggsToPut;
            document.getElementById('cantDiasMuerte').value = datos.chickensDaysToDie;
            document.getElementById('diasHuevosNacen').value = datos.eggsDaysToBecomeChicken;
            farmConfigItems();
        }
        else {
            habilitarEdicion();
        }
    })
    .catch(error => {
        console.log('Error: ' + error.message);
    });

}

function formatearFecha(fechaTransaccion) {
    const fechaAFormatear = new Date(fechaTransaccion);
    if (!isNaN(fechaAFormatear.getTime())) {
        const opciones = {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        };
        return fechaAFormatear.toLocaleDateString('es-ES', opciones);
    } else {
        return "Fecha inválida";
    }
}

function obtenerValorFarmId(button) {
    // Obtener la fila (tr) que contiene el botón
    var fila = button.closest('tr');

    // Obtener el valor del primer campo (primer td) de la fila
    var valor = fila.cells[0].textContent;
    farmId = valor;
}



function postFarmConfig (event) {


    if(document.getElementById('configForm').checkValidity()){

        event.preventDefault();
        var precioCompraGallina = precioCompraUnitarioGallina.value;
        var precioCompraHuevo = precioCompraUnitarioHuevo.value;
        var precioVentaGallina = precioVentaUnitarioGallina.value;
        var precioVentaHuevo = precioVentaUnitarioHuevo.value;
        var diasXHuevos = document.getElementById('diasXHuevos').value;
        var cantHuevosXDias = document.getElementById('cantHuevosXDias').value;
        var cantDiasMuerte = document.getElementById('cantDiasMuerte').value;
        var diasHuevosNacen = document.getElementById('diasHuevosNacen').value;

        var url = 'http://localhost:9100/createConfig';

        var configFarm = {
            purchasePriceChicken: precioCompraGallina,
            purchasePriceEgg: precioCompraHuevo,
            sellPriceChicken: precioVentaGallina,
            sellPriceEgg: precioVentaHuevo,
            amountDaysToPutEggs: diasXHuevos,
            amountEggsToPut: cantHuevosXDias,
            chickensDaysToDie: cantDiasMuerte,
            eggsDaysToBecomeChicken: diasHuevosNacen

        };

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(configFarm)
        })
        .then(response => {
        if (response.status === 201) {
            console.log('config creada');
            farmConfigItems();

        } else {
            response.json().then(data => {
                console.log(data.message)
            });
        }
        })
        .catch(error => {
            console.error('Error creacion config:', error);
            // Otras acciones en caso de error de red u otro error
        });
    }
}

function postNewFarm () {

    if(document.getElementById('addFarmForm').checkValidity()){

        var nombre = document.getElementById('nombreGranja').value;
        var capacidad = document.getElementById('capacidadGranja').value;
        var dinero = document.getElementById('dineroDisponible').value;

        var url = 'http://localhost:9100/createFarm?farmerId=' + userId;

        var newFarm = {
            name: nombre,
            capacity: capacidad,
            money: dinero
        };

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newFarm)
        })
        .then(response => {
        if (response.status === 201) {
            console.log('granja creada');

        } else {
            response.json().then(data => {
                console.log(data.message)
            });
        }
        })
        .catch(error => {
            console.error('Error creacion granja:', error);
            // Otras acciones en caso de error de red u otro error
        });

    } else {

        document.getElementById('addFarmForm').checkValidity();
    }

    window.location.reload();
}



function addChickens () {

    if(document.getElementById('addChickenEggsForm').checkValidity()){

        var cantidad = document.getElementById('cantidadChickensEggs').value;
        var granjaId = document.getElementById('granjasDisponibles').value;


        var urlAddChickens = 'http://localhost:9100/createChickens'
        var urlConParametro = `${urlAddChickens}?cantidad=${cantidad}&farmId=${granjaId}`;


        fetch(urlConParametro, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
			response.json().then(data => {
				if (response.status === 200) {
					console.log('Se agregaron ' + cantidad + ' gallinas');
                    setTimeout(function() {
                    alert(data.message);
                    window.location.reload();
                    }, 1000);

				} else {
					console.error('Error al agregar:', data.message);
                    setTimeout(function() {
                        alert(data.message);
                    }, 1000);
				}
			});

		})
		.catch(error => {
            console.error('Error al agregar gallinas:', error);
            // Otras acciones en caso de error de red u otro error
        });


    } else {
        document.getElementById('addChickenEggsForm').checkValidity();
    }
}

function realizarCompra () {

    if(compraForm.checkValidity()){

        var cantidad = document.getElementById('cantidadCompra').value;
        var producto = document.getElementById('productosCompra').value;


        var urlComprarProductos = 'http://localhost:9100/comprarProductos'
        var urlConParametro = `${urlComprarProductos}?cantidad=${cantidad}&farmerId=${userId}&producto=${producto}`;


        fetch(urlConParametro, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            response.json().then(data => {
                if (response.status === 200) {
                    console.log('Se compraron ' + cantidad + ' ' + producto);
                    setTimeout(function() {
                    alert(data.message);
                    window.location.reload();
                    }, 1000);
                } else {
                    console.error('Error al comprar:', data.message);
                    setTimeout(function() {
                        alert(data.message);
                    }, 1000);
                }
            });
        })
        .catch(error => {
            console.error('Error al comprar:', error);
            // Otras acciones en caso de error de red u otro error
        });

    } else {
        compraForm.checkValidity();
    }


}

function realizarVenta () {

    if(document.getElementById('ventaForm').checkValidity()){



        var cantidad = document.getElementById('cantidadVenta').value;
        var producto = document.getElementById('productosVenta').value;

        var urlComprarProductos = 'http://localhost:9100/venderProductos'
        var urlConParametro = `${urlComprarProductos}?cantidad=${cantidad}&farmId=${farmIdConst}&producto=${producto}`;


        fetch(urlConParametro, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            response.json().then(data => {
                if (response.status === 200) {
                    console.log('Se vendieron ' + cantidad + ' ' + producto);
                    setTimeout(function() {
                    alert(data.message);
                    window.location.reload();
                    }, 1000);
                } else {
                    console.error('Error al vender:', data.message);
                    setTimeout(function() {
                        alert(data.message);
                    }, 1000);
                }
            });
        })
        .catch(error => {
            console.error('Error al vender:', error);
            // Otras acciones en caso de error de red u otro error
        });

    } else {
        compraForm.checkValidity();
    }


}


function addEggs () {

    if(document.getElementById('addChickenEggsForm').checkValidity()){

        var cantidad = document.getElementById('cantidadChickensEggs').value;
        var granjaId = document.getElementById('granjasDisponibles').value;


        var urlAddEggs = 'http://localhost:9100/createEggs'
        var urlConParametro = `${urlAddEggs}?cantidad=${cantidad}&farmId=${granjaId}`;


        fetch(urlConParametro, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            response.json().then(data => {
                if (response.status === 200) {
                    console.log('Se agregaron ' + cantidad + ' huevos');
                    setTimeout(function() {
                    alert(data.message);
                    window.location.reload();
                    }, 1000);
                } else {
                    console.error('Error al agregar:', data.message);
                    setTimeout(function() {
                        alert(data.message);
                    }, 1000);
                }
            });
        })
        .catch(error => {
            console.error('Error al agregar huevos:', error);
            // Otras acciones en caso de error de red u otro error
        });


    } else {
        document.getElementById('addChickenEggsForm').checkValidity();
    }
}

function passingTime () {

    if(document.getElementById('passingTimeForm').checkValidity()){

        var cantidad = document.getElementById('cantidadDiasPassingTime').value;

        var urlAddChickens = 'http://localhost:9100/passingTimeFarms'
        var urlConParametro = `${urlAddChickens}?cantidad=${cantidad}`;


        fetch(urlConParametro, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
        if (response.status === 200) {
            console.log('Se avanzaron ' + cantidad + ' dias');

        } else {
            response.json().then(data => {
                console.log(data.message)
            });
        }
        })
        .catch(error => {
            console.error('Error al avanzar dias: ', error);
            // Otras acciones en caso de error de red u otro error
        });

    } else {
        document.getElementById('passingTimeForm').checkValidity();
    }
}

function granjasDisponibles(jsonObj) {
    'use strict';
    var options = document.getElementById('granjasDisponibles');

    // Elimina las opciones actuales
    while (options.firstChild) {
        options.removeChild(options.firstChild);
    }

    var granjas = jsonObj.farms;

    granjas.forEach(function(granja) {
        var optionElement = document.createElement('option');
        optionElement.value = granja.id;
        optionElement.textContent = granja.id +' - ' +granja.name + ' - Capacidad Disponible:' + granja.capacidadDisponible;
        options.appendChild(optionElement);
    });
}

function tablaFarms(jsonObj) {
    'use strict';
    var farms = jsonObj.farms;

    var myFarms = document.getElementById('myFarms');

    // Eliminar filas existentes
    while (myFarms.firstChild) {
        myFarms.removeChild(myFarms.firstChild);
    }

    for (var i = 0; i < farms.length; i++) {
        var tr = document.createElement('tr');

        // Crear y configurar celdas de datos
        var td2 = document.createElement('td');
        td2.textContent = farms[i].id;
        var td3 = document.createElement('td');
        td3.textContent = farms[i].name;
        var td4 = document.createElement('td');
        td4.textContent = farms[i].capacity;
        var td5 = document.createElement('td');
        td5.textContent = farms[i].money;
        var td6 = document.createElement('td');
        td6.textContent = farms[i].cantChickens;
        var td7 = document.createElement('td');
        td7.textContent = farms[i].cantEggs;
        var td8 = document.createElement('td');
        td8.textContent = farms[i].capacidadDisponible;
        var td9 = document.createElement('td');
        var td10 = document.createElement('td');

        var mostrarStatusButton = document.createElement('button');
        var venderProductosButton = document.createElement('button');

        mostrarStatusButton.textContent = 'Ver Estado';
        venderProductosButton.textContent = 'Vender Productos';

        mostrarStatusButton.addEventListener('click', function() {
            const tablaEstadosPopup = document.getElementById('tablaEstadosPopup');
            const closeTablaEstadosPopup = document.getElementById('closeTablaEstadosPopup');

            // Obtener el valor de la fila actual
            var fila = this.closest('tr');
            var farmId = fila.cells[0].textContent;

            obtenerEstados(farmId);
            tablaEstadosPopup.style.display = 'block';

            closeTablaEstadosPopup.addEventListener("click", function() {
                tablaEstadosPopup.style.display = 'none';
            });
        });

        venderProductosButton.addEventListener('click', function() {
            const realizarVentaPopup = document.getElementById('realizarVentaPopup');
            const cerrarVentaButton = document.getElementById('cerrarVentaButton');
            const ejecutarVentaButton = document.getElementById('ejecutarVentaButton');
            const ventaForm = document.getElementById('ventaForm');
            const closeVentaPopup = document.getElementById('closeVentaPopup');

            // Obtener el valor de la fila actual
            var fila = this.closest('tr');
            farmIdConst = fila.cells[0].textContent;


            realizarVentaPopup.style.display = 'block';
            showTable(cerrarVentaButton);
            showTable(ejecutarVentaButton);
            showTable(ventaForm);

            cerrarVentaButton.addEventListener("click", function() {
                realizarVentaPopup.style.display = 'none';
            });

            closeVentaPopup.addEventListener("click", function() {
                realizarVentaPopup.style.display = 'none';
            });
        });

        // Agregar el botón a la celda
        td9.appendChild(mostrarStatusButton);
        td10.appendChild(venderProductosButton);

        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        tr.appendChild(td5);
        tr.appendChild(td6);
        tr.appendChild(td7);
        tr.appendChild(td8);
        tr.appendChild(td9);
        tr.appendChild(td10);

        myFarms.appendChild(tr);
    }
}



function tablaCompras(jsonObj) {
    'use strict';
    var compras = jsonObj.transacciones;

    var myPurchases = document.getElementById('myPurchases');

    // Eliminar filas existentes
    while (myPurchases.firstChild) {
        myPurchases.removeChild(myPurchases.firstChild);
    }


    for (var i = 0; i < compras.length; i++) {
        var tr = document.createElement('tr');
        var td2 = document.createElement('td');
        td2.setAttribute('class', 'col-xs-1 col-md-1');
        var td3 = document.createElement('td');
        td3.setAttribute('class', 'col-xs-1 col-md-1');
        var td4 = document.createElement('td');
        td4.setAttribute('class', 'col-xs-2 col-md-2');
        var td5 = document.createElement('td');
        td5.setAttribute('class', 'col-xs-1 col-md-1');
        var td6 = document.createElement('td');
        td6.setAttribute('class', 'col-xs-2 col-md-2');
        var td7 = document.createElement('td');
        td7.setAttribute('class', 'col-xs-1 col-md-1');


        td2.textContent = compras[i].id;
        td3.textContent = formatearFecha(compras[i].fecha);
        td4.textContent = compras[i].tipoProducto;
        td5.textContent = compras[i].precioUnitario;
        td6.textContent = compras[i].cantidad;
        td7.textContent = compras[i].precioTotal;


        tr.append(td2);
        tr.append(td3);
        tr.append(td4);
        tr.append(td5);
        tr.append(td6);
        tr.append(td7);

        myPurchases.append(tr);

    }

}

function tablaVentas(jsonObj) {
    'use strict';
    var ventas = jsonObj.transacciones;

    var mySales = document.getElementById('mySales');

    // Eliminar filas existentes
    while (mySales.firstChild) {
        mySales.removeChild(mySales.firstChild);
    }


    for (var i = 0; i < ventas.length; i++) {
        var tr = document.createElement('tr');
        var td2 = document.createElement('td');
        td2.setAttribute('class', 'col-xs-1 col-md-1');
        var td3 = document.createElement('td');
        td3.setAttribute('class', 'col-xs-1 col-md-1');
        var td4 = document.createElement('td');
        td4.setAttribute('class', 'col-xs-2 col-md-2');
        var td5 = document.createElement('td');
        td5.setAttribute('class', 'col-xs-1 col-md-1');
        var td6 = document.createElement('td');
        td6.setAttribute('class', 'col-xs-2 col-md-2');
        var td7 = document.createElement('td');
        td7.setAttribute('class', 'col-xs-1 col-md-1');


        td2.textContent = ventas[i].id;
        td3.textContent = formatearFecha(ventas[i].fecha);
        td4.textContent = ventas[i].tipoProducto;
        td5.textContent = ventas[i].precioUnitario;
        td6.textContent = ventas[i].cantidad;
        td7.textContent = ventas[i].precioTotal;

        tr.append(td2);
        tr.append(td3);
        tr.append(td4);
        tr.append(td5);
        tr.append(td6);
        tr.append(td7);

        mySales.append(tr);

    }

}


function completarEstados(jsonObj) {
    'use strict';
    var gallinas = jsonObj.farm.chickens;
    var huevos = jsonObj.farm.eggs;

    var tablaEstadosGallinas = document.getElementById('estadosGallinas');
    var tablaEstadosHuevos = document.getElementById('estadosHuevos');

    // Eliminar filas existentes
    while (tablaEstadosGallinas.firstChild) {
        tablaEstadosGallinas.removeChild(tablaEstadosGallinas.firstChild);
    }

    while (tablaEstadosHuevos.firstChild) {
        tablaEstadosHuevos.removeChild(tablaEstadosHuevos.firstChild);
    }


    for (var i = 0; i < gallinas.length; i++) {
        var tr = document.createElement('tr');
        var td2 = document.createElement('td');
        td2.setAttribute('class', 'col-xs-1 col-md-1');
        var td3 = document.createElement('td');
        td3.setAttribute('class', 'col-xs-1 col-md-1');
        var td4 = document.createElement('td');
        td4.setAttribute('class', 'col-xs-2 col-md-2');
        var td5 = document.createElement('td');
        td5.setAttribute('class', 'col-xs-1 col-md-1');



        td2.textContent = gallinas[i].id;
        td3.textContent = gallinas[i].remainingDaysToDie;
        td4.textContent = gallinas[i].remainingDaysToPutEggs;
        td5.textContent = gallinas[i].amountEggsToPut;


        tr.append(td2);
        tr.append(td3);
        tr.append(td4);
        tr.append(td5);


        tablaEstadosGallinas.append(tr);

    }

    for (var i = 0; i < huevos.length; i++) {
        var tr = document.createElement('tr');
        var td2 = document.createElement('td');
        td2.setAttribute('class', 'col-xs-1 col-md-1');
        var td3 = document.createElement('td');
        td3.setAttribute('class', 'col-xs-1 col-md-1');
        td2.textContent = huevos[i].id;
        td3.textContent = huevos[i].daysToBecomeChicken;

        tr.append(td2);
        tr.append(td3);
        tablaEstadosHuevos.append(tr);

    }

}