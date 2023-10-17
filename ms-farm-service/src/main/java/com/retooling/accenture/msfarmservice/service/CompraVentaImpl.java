package com.retooling.accenture.msfarmservice.service;

import com.retooling.accenture.msfarmservice.exception.ConfiguracionNoExisteException;
import com.retooling.accenture.msfarmservice.exception.ProductoNoEncontradoExpception;
import com.retooling.accenture.msfarmservice.model.*;
import com.retooling.accenture.msfarmservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public List<Chicken> obtenerGallinasCompra(int cantidad) {
        return chickenRepository.findCantChickensFromAdmin(cantidad);
    }

    //Metodo que busca la cantidad de gallinas para la venta del usuario a las granjas ADMIN
    //ordenadas DESC por días restantes de vida para priorizar las más lejanas a morir
    public List<Chicken> obtenerGallinasVenta(int cantidad, int farmId) {
        return chickenRepository.findCantChickensFromUser(cantidad, farmId);
    }

    //Metodo que busca la cantidad de huevos para la compra del usuario ordenados DESC por días restantes para
    //convertirse en gallina para vender las más lejanas a nacer.
    public List<Egg> obtenerHuevosCompra(int cantidad) {
        return eggsRepository.findCantEggsFromAdmin(cantidad);
    }

    //Metodo que busca la cantidad de huevos para la venta del usuario a las granjas ADMIN
    //ordenadas ASC por días restantes para convertirse en gallinas.
    public List<Egg> obtenerHuevosVenta(int cantidad, int farmId) {
        return eggsRepository.findCantChickensFromUser(cantidad, farmId);
    }


    //Metodo para que el usuario pueda comprar gallinas segun si la granja tiene dinero y capacidad disponible
    //Genera la transaccion de compra para el usuario y de venta para los admins dueño de cada gallina
    public ResponseEntity comprar(int cantidad, int farmerId,String producto) {

        Farmer farmer = farmerRepository.findByUserId(farmerId);
        //map para guardar el valor con el farmerId y la cantidad de ventas que realizo para después armar la transaccion
        Map<Integer, Integer> cantVentasXFarmer = new HashMap<>();

        TipoTransaccionProducto tipoProducto = TipoTransaccionProducto.valueOf(producto.toUpperCase());
        double precioXUnidad = 0.0;

        try {

            if (chickenEggsService.getFarmServiceConfig() == null) {
                throw new ConfiguracionNoExisteException("No se encontraron valores en la configuracion");
            }

            if (tipoProducto == TipoTransaccionProducto.GALLINAS) {

                List<Chicken> gallinasDisponibles = obtenerGallinasCompra(cantidad);
                if (gallinasDisponibles.isEmpty() || gallinasDisponibles.size() < cantidad) {
                    throw new ProductoNoEncontradoExpception("No se encontraron gallinas disponibles para la compra.");
                }

                precioXUnidad = chickenEggsService.getFarmServiceConfig().getSellPriceChicken();

                double precioCantGallinas = precioXUnidad * cantidad;

                Farm granjaDestino = farmer.getFarms().stream()
                        .filter(f -> f.getMoney() > precioCantGallinas && f.getCapacidadDisponible() > cantidad)
                        .max(Comparator.comparing(Farm::getCapacidadDisponible))
                        .orElseThrow(() -> new ProductoNoEncontradoExpception("No se encontró granja con capacidad o dinero disponible"));

                for (Chicken chicken : gallinasDisponibles) {

                    int farmId = chicken.getFarm().getId();
                    Farm granjaOrigen = farmRepository.findById(farmId);
                    movimientosEntreGranjas(tipoProducto, granjaOrigen, granjaDestino, precioXUnidad, 1);

                    cantVentasXFarmer = armarTransaccionesVentaGranja(cantVentasXFarmer, chicken.getFarm().getFarmer().getUserId());
                    chicken.setFarm(granjaDestino);
                    chickenRepository.save(chicken);
                }
            } else if (tipoProducto == TipoTransaccionProducto.HUEVOS) {

                List<Egg> huevosDisponibles = obtenerHuevosCompra(cantidad);
                if (huevosDisponibles.isEmpty() || huevosDisponibles.size() < cantidad) {
                    throw new ProductoNoEncontradoExpception("No se encontraron huevos disponibles para la compra.");
                }

                precioXUnidad = chickenEggsService.getFarmServiceConfig().getSellPriceEgg();
                double precioCantHuevos = precioXUnidad * cantidad;

                Farm granjaDestino = farmer.getFarms().stream()
                        .filter(f -> f.getMoney() > precioCantHuevos && f.getCapacidadDisponible() > cantidad)
                        .max(Comparator.comparing(Farm::getCapacidadDisponible))
                        .orElseThrow(() -> new ProductoNoEncontradoExpception("No se encontró granja con capacidad o dinero disponible"));

                for (Egg egg : huevosDisponibles) {

                    int farmId = egg.getFarm().getId();
                    Farm granjaOrigen = farmRepository.findById(farmId);
                    movimientosEntreGranjas(tipoProducto, granjaOrigen, granjaDestino, precioXUnidad, 1);

                    cantVentasXFarmer = armarTransaccionesVentaGranja(cantVentasXFarmer, egg.getFarm().getFarmer().getUserId());
                    egg.setFarm(granjaDestino);
                    eggsRepository.save(egg);

                }
            }
            //genera la transaccion de compra
            generarTransaccion(TipoTransaccionProducto.COMPRA, tipoProducto, cantidad, precioXUnidad, farmer);
            //genera la transaccion de venta por cada dueño admin de las gallinas segun la cantidad que se vendieron
            for (Map.Entry<Integer, Integer> map : cantVentasXFarmer.entrySet()) {
                int farmerVenta = map.getKey();
                int cantidadVentas = map.getValue();
                generarTransaccion(TipoTransaccionProducto.VENTA, tipoProducto, cantidadVentas, precioXUnidad, farmerRepository.findByUserId(farmerVenta));
            }

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


    //Arma el map con el farmerId y va actualizando las cantidades que se vendieron
    public Map<Integer,Integer> armarTransaccionesVentaGranja (Map<Integer,Integer> cantVentasXFarmer,int farmerId){

        Map<Integer, Integer> updatedCantVentasXFarmer = new HashMap<>(cantVentasXFarmer);
        // Verifica si el farmer ID ya existe
        if (updatedCantVentasXFarmer.containsKey(farmerId)) {
            // Si existe, obtiene el número actual y lo actualiza
            int cantVentasActual = updatedCantVentasXFarmer.get(farmerId);
            updatedCantVentasXFarmer.put(farmerId, cantVentasActual + 1);
        } else {
            // Si no existe, agrega el farmer ID con la cantidad inicial
            updatedCantVentasXFarmer.put(farmerId, 1);
        }
        return updatedCantVentasXFarmer;
    }


    //Metodo para que el usuario pueda vender gallinas de cualquier de sus granjas
    public ResponseEntity vender(int cantidad, int farmId, String producto) {

        try {

            TipoTransaccionProducto tipoProducto = TipoTransaccionProducto.valueOf(producto.toUpperCase());
            Farm granjaOrigen = farmRepository.findById(farmId);
            Farmer farmerOrigen = granjaOrigen.getFarmer();

            Farmer farmerDestino = null;
            double precioUnidad = 0.0;

            if (tipoProducto == TipoTransaccionProducto.GALLINAS) {

                precioUnidad = chickenEggsService.getFarmServiceConfig().getPurchasePriceChicken();
                double precioCantGallinas = precioUnidad * cantidad;

                List<Chicken> gallinasVenta = obtenerGallinasVenta(cantidad, farmId);

                if (gallinasVenta.isEmpty() || gallinasVenta.size() < cantidad) {
                    throw new ProductoNoEncontradoExpception("No se encontraron gallinas disponibles para la venta.");
                }

                Farm granjaDestino = farmRepository.findFarmFromAdmin(cantidad, precioCantGallinas);

                if (granjaDestino == null) {
                    throw new ProductoNoEncontradoExpception("No se puede realizar la venta de esa cantidad");
                }

                farmerDestino = granjaDestino.getFarmer();

                for (Chicken chicken : gallinasVenta) {
                    chicken.setFarm(granjaDestino);
                    chickenRepository.save(chicken);
                }
                movimientosEntreGranjas(tipoProducto, granjaOrigen, granjaDestino, precioCantGallinas, cantidad);

            } else if (tipoProducto == TipoTransaccionProducto.HUEVOS) {

                precioUnidad = chickenEggsService.getFarmServiceConfig().getPurchasePriceEgg();
                double precioCantHuevos = precioUnidad * cantidad;

                List<Egg> huevosVenta = obtenerHuevosVenta(cantidad, farmId);

                if (huevosVenta.isEmpty() || huevosVenta.size() < cantidad) {
                    throw new ProductoNoEncontradoExpception("No se encontraron huevos disponibles para la venta.");
                }

                Farm granjaDestino = farmRepository.findFarmFromAdmin(cantidad, precioCantHuevos);

                if (granjaDestino == null) {
                    throw new ProductoNoEncontradoExpception("No se puede realizar la venta de esa cantidad");
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
