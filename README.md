# retooling-java
ejercicio chicken test para el retooling de java

Para agregar la base de datos a docker usar el siguiente comando desde cmd:

docker run --detach --env MYSQL_ROOT_PASSWORD=admin1234 --env MYSQL_USER=chicken-user --env MYSQL_PASSWORD=chicken1234 --env MYSQL_DATABASE=chicken-test-service --name chicken-service --publish 3306:3306 mysql:8-oracle

Una vez creado la base se puede acceder desde mysql con los siguientes datos de conexion

hostname: 127.0.0.1
port: 3306
username: root
pass: admin1234


--------------------------------------
Para iniciar con el chicken test hay que iniciar los proyectos en el siguiente orden:

1- eureka-naming-server
2- api-gateway
3- ms-farm-service
4- ms-spring-security

--------------------------------------
Para empezar hay que registar un usuario con rol admin desde la pagina:

http://localhost:9100/web/registration.html

--------------------------------------
Una vez registrado el usuario con permisos admin, nos podemos logear en:

http://localhost:9100/web/login.html

Con el usuario admin logeado, podemos ingresar a la parte de configuracion y setear los diferentes valores de configuracion.

*Importante: siempre que levantemos los microservicios hay que configurar nuevamente los valores ya que no son guardados en la base. Una vez que esten configurados podemos usar las demas funcionalidades.

-Como primer paso podemos crear en la seccion de "Mis Granjas" las granjas que tiene el admin con los valores de capacidad, dinero y nombre.

-Como segundo paso podemos cargar desde "Configuracion" la cantidad de gallinas y huevos que tienen las granjas del admin.



--------------------------------------
- Para registrar un usuario, ingresar a la pagina de registro y crearlo con el rol de "User"
- Despues nos podemos logear con el usuario y crear las granjas desde "Mis granjas"
- Una vez creadas las granjas podemos empezar a comprar desde el boton "Realizar Compra" ingresando la cantidad y el producto a comprar.
- Tambien podemos vender productos desde cada granja ingresando la cantidad y el producto.

- En cada granja podemos ver el estado para inspeccionar las gallinas y huevos disponibles.

--------------------------------------

Para poder ejecutar la opcion de paso del tiempo, ingresar con usuario admin y en "Configuracion" usar el boton "Ejecutar Paso del Tiempo".
Debemos ingresar la cantidad de dias que queremos avanzar.

Esta funcion permite avanzar la cantidad de dias para todas las granjas de todos los usuarios.


----------------------------------------------------------------------------------------------------
cambios 15/11

-se modifica la forma en que se compra y vende. Ahora los ADMIN solamente van a poder vender a precio de venta y cargar el inventario en las granjas. El USER va a poder realizar compras/ventas. El precio compra venta lo define los ADMIN en la configuracion.
-ahora se debe pasar, al momento de realizar la compra y venta, el id de la granja a quien se le va a vender o comprar.
-se agregaron validaciones correspondientes.
















