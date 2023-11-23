package com.retooling.accenture.msfarmservice.service;

import com.retooling.accenture.msfarmservice.exception.ConfiguracionNoExisteException;
import com.retooling.accenture.msfarmservice.exception.GranjaInexistenteException;
import com.retooling.accenture.msfarmservice.exception.ProductoNoEncontradoExpception;
import com.retooling.accenture.msfarmservice.exception.TransaccionNoPermitidaException;
import com.retooling.accenture.msfarmservice.model.*;
import com.retooling.accenture.msfarmservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


//servicio para la logica relacionado con la compra y venta de los productos y el historial.
@Service("CompraVentaService")
public class CompraVentaImpl implements CompraVenta {

    @Autowired
    private ChickenRepository chickenRepository;

    @Autowired
    private EggsRepository eggsRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ChickenEggsImpl chickenEggsService;

    public ChickenEggsImpl getChickenEggsService() {
        return chickenEggsService;
    }

    public void setChickenEggsService(ChickenEggsImpl chickenEggsService) {
        this.chickenEggsService = chickenEggsService;
    }


    //Metodo que busca la cantidad de gallinas para la compra del usuario ordenadas ASC por días restantes de vida
    //para vender las más proximas a morir.
    public List<Chicken> obtenerGallinasCompra(int cantidad, int idGranjaOrigen) {
        return chickenRepository.findByFarmIdCantChickens(cantidad,idGranjaOrigen);
    }

    //Metodo que busca la cantidad de gallinas para la venta del usuario
    //ordenadas DESC por días restantes de vida para priorizar las más lejanas a morir
    public List<Chicken> obtenerGallinasVenta(int cantidad, int farmId) {
        return chickenRepository.findCantChickensFromUser(cantidad, farmId);
    }


    //Metodo que busca la cantidad de huevos para la compra del usuario ordenados DESC por días restantes para
    //convertirse en gallina para vender las más lejanas a nacer.
    public List<Egg> obtenerHuevosCompra(int cantidad, int idGranjaOrigen) {
        return eggsRepository.findCantEggsFromGranjaOrigen(cantidad,idGranjaOrigen);
    }

    //Metodo que busca la cantidad de huevos para la venta del usuario
    //ordenados ASC por días restantes para convertirse en gallinas.
    public List<Egg> obtenerHuevosVenta(int cantidad, int farmId) {
        return eggsRepository.findCantChickensFromUser(cantidad, farmId);
    }


    //Metodo para que el usuario con rol "USER" pueda comprar gallinas segun si la granja tiene dinero y capacidad disponible
    //Genera la transaccion de compra para el usuario y de venta con el ID de granja origen que le vende los productos
    public ResponseEntity comprar(int cantidad, int farmerId,String producto, int idGranjaOrigen) {

        Farmer farmerComprador= farmerRepository.findByUserId(farmerId);
        TipoTransaccionProducto tipoProducto = TipoTransaccionProducto.valueOf(producto.toUpperCase());
        double precioXUnidad = 0.0;

        try {

            //se verifica que está cargada la configuracion
            if (chickenEggsService.getFarmServiceConfig() == null) {
                throw new ConfiguracionNoExisteException("No se encontraron valores en la configuracion");
            }

            //se verifica que la granja a la que se le compra no sea la misma del usuario
            if (farmRepository.findById(idGranjaOrigen) == null){
                throw new GranjaInexistenteException("No se encontro el identificador de granja");
            }

            if (farmRepository.findById(idGranjaOrigen).getFarmer().getUserId() == farmerId){
                throw new TransaccionNoPermitidaException("No se puede concretar la compra. El identificador de granja corresponde al usuario logeado");
            }

            Farm granjaOrigen = farmRepository.findById(idGranjaOrigen);
            Farmer farmerVendedor = granjaOrigen.getFarmer();

            if (tipoProducto == TipoTransaccionProducto.GALLINAS) {

                List<Chicken> gallinasDisponibles = obtenerGallinasCompra(cantidad,idGranjaOrigen);

                if (gallinasDisponibles.isEmpty() || gallinasDisponibles.size() < cantidad) {
                    throw new ProductoNoEncontradoExpception("No se encontraron gallinas disponibles para la compra.");
                }

                precioXUnidad = chickenEggsService.getFarmServiceConfig().getSellPriceChicken();

                double precioCantGallinas = precioXUnidad * cantidad;

                Farm granjaDestino = farmerComprador.getFarms().stream()
                        .filter(f -> f.getMoney() > precioCantGallinas && f.getCapacidadDisponible() > cantidad)
                        .max(Comparator.comparing(Farm::getCapacidadDisponible))
                        .orElseThrow(() -> new ProductoNoEncontradoExpception("No se encontró granja con capacidad o dinero disponible"));


                for (Chicken chicken : gallinasDisponibles) {
                    movimientosEntreGranjas(tipoProducto, granjaOrigen, granjaDestino, precioXUnidad, 1);
                    chicken.setFarm(granjaDestino);
                    chickenRepository.save(chicken);
                }

            } else if (tipoProducto == TipoTransaccionProducto.HUEVOS) {

                List<Egg> huevosDisponibles = obtenerHuevosCompra(cantidad,idGranjaOrigen);
                if (huevosDisponibles.isEmpty() || huevosDisponibles.size() < cantidad) {
                    throw new ProductoNoEncontradoExpception("No se encontraron huevos disponibles para la compra.");
                }

                precioXUnidad = chickenEggsService.getFarmServiceConfig().getSellPriceEgg();
                double precioCantHuevos = precioXUnidad * cantidad;

                Farm granjaDestino = farmerComprador.getFarms().stream()
                        .filter(f -> f.getMoney() > precioCantHuevos && f.getCapacidadDisponible() > cantidad)
                        .max(Comparator.comparing(Farm::getCapacidadDisponible))
                        .orElseThrow(() -> new ProductoNoEncontradoExpception("No se encontró granja con capacidad o dinero disponible"));


                for (Egg egg : huevosDisponibles) {

                    movimientosEntreGranjas(tipoProducto, granjaOrigen, granjaDestino, precioXUnidad, 1);
                    egg.setFarm(granjaDestino);
                    eggsRepository.save(egg);

                }
            }
            //genera la transaccion de compra para la granja destino
            generarTransaccion(TipoTransaccionProducto.COMPRA, tipoProducto, cantidad, precioXUnidad, farmerComprador);

            //genera la transaccion de venta para la granja origen
            generarTransaccion(TipoTransaccionProducto.VENTA, tipoProducto, cantidad, precioXUnidad, farmerVendedor);

            return ResponseEntity.ok().body("Se realizo la compra correctamente.");

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public void movimientosEntreGranjas (TipoTransaccionProducto tipoProducto, Farm granjaOrigen, Farm granjaDestino, double dinero, int cantidad){

        granjaOrigen.setMoney(granjaOrigen.getMoney() + dinero);
        granjaDestino.setMoney(granjaDestino.getMoney() - dinero);

        if (tipoProducto == TipoTransaccionProducto.GALLINAS){
            granjaOrigen.setCantChickens(granjaOrigen.getCantChickens() - cantidad);
            granjaDestino.setCantChickens(granjaDestino.getCantChickens() + cantidad);

        } else if (tipoProducto == TipoTransaccionProducto.HUEVOS){
            granjaOrigen.setCantEggs(granjaOrigen.getCantEggs() - cantidad);
            granjaDestino.setCantEggs(granjaDestino.getCantEggs() + cantidad);
        }

        granjaOrigen.setCapacidadDisponible();
        granjaDestino.setCapacidadDisponible();
        farmRepository.save(granjaDestino);
        farmRepository.save(granjaOrigen);

    }


    //Metodo para generar las transacciones tipoOperacion puede ser COMPRA o VENTA y tipoProducto HUEVOS o GALLINAS
    public void generarTransaccion(TipoTransaccionProducto tipoOperacion, TipoTransaccionProducto tipoProducto, int cantidad, double precioVentaUnitario, Farmer farmer){
        Transaccion transaccion = new Transaccion(tipoOperacion,cantidad,precioVentaUnitario,
                cantidad*precioVentaUnitario,tipoProducto, LocalDateTime.now());
        transaccion.setFarmer(farmer);
        transaccionRepository.save(transaccion);
    }

    //Metodo para que el usuario pueda vender gallinas de cualquier de sus granjas. Para el rol ADMIN se diferencia
    // el precio, se utiliza el de venta.
    // Para el rol USER, se utiliza para la venta el precio de compra de la configuracion

    public ResponseEntity vender(int cantidad, int farmId, String producto, int idGranjaDestino) {

        try {

            TipoTransaccionProducto tipoProducto = TipoTransaccionProducto.valueOf(producto.toUpperCase());
            Farm granjaOrigen = farmRepository.findById(farmId);
            Farmer farmerOrigen = granjaOrigen.getFarmer();
            String roleFarmer = farmerRepository.findRoleByFarmerId(farmId);


            double precioUnidad = 0.0;

            //se verifica que está cargada la configuracion
            if (chickenEggsService.getFarmServiceConfig() == null) {
                throw new ConfiguracionNoExisteException("No se encontraron valores en la configuracion");
            }

            //se verifica que la granja a la que se le compra no sea la misma del usuario
            if (farmRepository.findById(idGranjaDestino) == null){
                throw new GranjaInexistenteException("No se encontro el identificador de granja");
            }

            if (farmRepository.findById(idGranjaDestino).getFarmer().getUserId() == farmerOrigen.getUserId()){
                throw new TransaccionNoPermitidaException("No se puede concretar la venta. El identificador de granja corresponde al usuario logeado");
            }

            Farmer farmerDestino = null;

            if (tipoProducto == TipoTransaccionProducto.GALLINAS) {

                if (roleFarmer.equalsIgnoreCase("ADMIN")) {
                    precioUnidad = chickenEggsService.getFarmServiceConfig().getSellPriceChicken();
                } else {
                    precioUnidad = chickenEggsService.getFarmServiceConfig().getPurchasePriceChicken();
                }
                double precioCantGallinas = precioUnidad * cantidad;

                List<Chicken> gallinasVenta = obtenerGallinasVenta(cantidad, farmId);

                if (gallinasVenta.isEmpty() || gallinasVenta.size() < cantidad) {
                    throw new ProductoNoEncontradoExpception("No se encontraron gallinas disponibles para la venta.");
                }

                Farm granjaDestino = farmRepository.findFarmDestinoVenta(cantidad, precioCantGallinas, idGranjaDestino);

                if (granjaDestino == null) {
                    throw new ProductoNoEncontradoExpception("La granja compradora no tiene capacidad o dinero disponible");
                }

                farmerDestino = granjaDestino.getFarmer();

                for (Chicken chicken : gallinasVenta) {
                    chicken.setFarm(granjaDestino);
                    chickenRepository.save(chicken);
                }
                movimientosEntreGranjas(tipoProducto, granjaOrigen, granjaDestino, precioCantGallinas, cantidad);

            } else if (tipoProducto == TipoTransaccionProducto.HUEVOS) {

                if (roleFarmer.equalsIgnoreCase("ADMIN")) {
                    precioUnidad = chickenEggsService.getFarmServiceConfig().getSellPriceEgg();
                } else {
                    precioUnidad = chickenEggsService.getFarmServiceConfig().getPurchasePriceEgg();
                }
                
                double precioCantHuevos = precioUnidad * cantidad;

                List<Egg> huevosVenta = obtenerHuevosVenta(cantidad, farmId);

                if (huevosVenta.isEmpty() || huevosVenta.size() < cantidad) {
                    throw new ProductoNoEncontradoExpception("No se encontraron huevos disponibles para la venta.");
                }
                
                Farm granjaDestino = farmRepository.findFarmDestinoVenta(cantidad, precioCantHuevos, idGranjaDestino);

                if (granjaDestino == null) {
                    throw new ProductoNoEncontradoExpception("La granja compradora no tiene capacidad o dinero disponible");
                }

                farmerDestino = granjaDestino.getFarmer();

                for (Egg egg : huevosVenta) {
                    egg.setFarm(granjaDestino);
                    eggsRepository.save(egg);
                }
                movimientosEntreGranjas(tipoProducto, granjaOrigen, granjaDestino, precioCantHuevos, cantidad);
            }

            generarTransaccion(TipoTransaccionProducto.VENTA, TipoTransaccionProducto.valueOf(producto.toUpperCase()), cantidad, precioUnidad, farmerOrigen);
            generarTransaccion(TipoTransaccionProducto.COMPRA, TipoTransaccionProducto.valueOf(producto.toUpperCase()), cantidad, precioUnidad, farmerDestino);

            return ResponseEntity.ok().body("Se vendio correctamente");

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

}
