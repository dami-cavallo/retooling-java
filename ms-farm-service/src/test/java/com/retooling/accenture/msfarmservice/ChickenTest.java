package com.retooling.accenture.msfarmservice;

import com.retooling.accenture.msfarmservice.model.*;
import com.retooling.accenture.msfarmservice.repositories.*;
import com.retooling.accenture.msfarmservice.service.ChickenEggsImpl;
import com.retooling.accenture.msfarmservice.service.CompraVentaImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ChickenTest {

    private Farm granja1;
    private Farm granja2;

    private Farmer farmer1;
    private Farmer farmer2;
    private final List<Chicken> listaGallinasGranja1 =  new ArrayList<>();
    private final List<Egg> listaHuevosGranja1 = new ArrayList<>();
    private final List<Chicken> listaGallinasGranja2 = new ArrayList<>();
    private final List<Egg> listaHuevosGranja2 = new ArrayList<>();

    @InjectMocks
    private CompraVentaImpl compraVentaService;
    @InjectMocks
    private ChickenEggsImpl chickenEggService;
    @Mock
    private FarmRepository farmRepository;
    @Mock
    private ChickenRepository chickenRepository;
    @Mock
    private EggsRepository eggsRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private FarmerRepository farmerRepository;


    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    @Before
    public void setUp() {

        FarmServiceConfig farmServiceConfig;
        Chicken chicken1;
        Chicken chicken2;
        Chicken chicken3;
        Egg egg1;
        Egg egg2;
        Egg egg3;

        // realizar la configuracion inicial para las pruebas
        //se carga la configuracion con los precios y los días
        farmServiceConfig =  new FarmServiceConfig(
                30,
                20,
                50,
                40,
                30,
                2,
                100,
                21
        );

        chickenEggService.setFarmServiceConfig(farmServiceConfig);
        compraVentaService.setChickenEggsService(chickenEggService);

        int diasVida = farmServiceConfig.getChickensDaysToDie();
        int diasParaPonerHuevos = farmServiceConfig.getAmountDaysToPutEggs();
        int cantidadHuevosQuePone = farmServiceConfig.getAmountEggsToPut();
        int diasHuevoParaConvertirseEnGallina = farmServiceConfig.getEggsDaysToBecomeChicken();

        farmer1 = new Farmer(1,"farmer1","farmer1","farmer1@farmer1");
        farmer2 = new Farmer(2,"farmer2","farmer2","farmer2@farmer2");


        granja1 = new Farm("granja1",100,10000.00,farmer1,0,0);
        granja2 = new Farm("granja2",90,9000.00,farmer2,0,0);

        granja1.setId(1);
        granja2.setId(2);

        chicken1 = new Chicken(diasVida,diasParaPonerHuevos,cantidadHuevosQuePone);
        chicken1.setId(1);

        chicken2 = new Chicken(diasVida,diasParaPonerHuevos,cantidadHuevosQuePone);
        chicken2.setId(2);

        chicken3 = new Chicken(diasVida,diasParaPonerHuevos,cantidadHuevosQuePone);
        chicken3.setId(3);

        egg1 = new Egg(diasHuevoParaConvertirseEnGallina);
        egg1.setId(1);

        egg2 = new Egg(diasHuevoParaConvertirseEnGallina);
        egg2.setId(2);

        egg3 = new Egg(diasHuevoParaConvertirseEnGallina);
        egg3.setId(3);

        chicken1.setFarm(granja1);
        chicken2.setFarm(granja1);
        chicken3.setFarm(granja2);

        egg1.setFarm(granja1);
        egg2.setFarm(granja1);
        egg3.setFarm(granja2);

        listaGallinasGranja1.add(chicken1);
        listaGallinasGranja1.add(chicken2);
        listaGallinasGranja2.add(chicken3);

        listaHuevosGranja1.add(egg1);
        listaHuevosGranja1.add(egg2);
        listaHuevosGranja2.add(egg3);

        granja1.setChickens(listaGallinasGranja1);
        granja1.setEggs(listaHuevosGranja1);

        granja2.setChickens(listaGallinasGranja2);
        granja2.setEggs(listaHuevosGranja2);

        granja1.setCantChickens(listaGallinasGranja1.size());
        granja1.setCantEggs(listaHuevosGranja1.size());

        granja2.setCantChickens(listaGallinasGranja2.size());
        granja2.setCantEggs(listaHuevosGranja2.size());



    }

    @Test
    public void cantidadGallinasyHuevosGranjas() {
        assertEquals(2,granja1.getCantChickens());
        assertEquals(2,granja1.getCantEggs());
        assertEquals(1,granja2.getCantChickens());
        assertEquals(1,granja2.getCantEggs());
    }


    @Test
    public void testPasoDelTiempoGranja1() {
        // se configura el comportamiento de los repositorios.
        when(farmRepository.findById(anyInt())).thenReturn(granja1);

        // se llama al metodo.
        chickenEggService.passingTimeChicken(10, 1);

        // se verifica.
        for (Chicken chicken : granja1.getChickens()) {
            assertEquals(90, chicken.getRemainingDaysToDie());
        }

    }

    @Test
    public void testPasoDelTiempoTodasLasGranjas() {
        // se configura el comportamiento de farmRepository para devolver una lista de granjas
        List<Farm> granjas = Arrays.asList(granja1, granja2);
        when(farmRepository.findAll()).thenReturn(granjas);
        //se configura para que la primera iteracion del findall devuelva la granja1 y en la segunda la 2
        when(farmRepository.findById(anyInt())).thenReturn(granja1).thenReturn(granja2);

        // se ejecuta el metodo del paso del tiempo
        chickenEggService.passingTimeFarms(20);


        // se verifica que todas las gallinas se le restan 20 días de los 100 de vida
        // y que los huevos se le resten 20 días de los 21 para convertirse en gallina
        for (Farm farm : granjas) {
            for (Chicken chicken : farm.getChickens()) {
                assertEquals(80, chicken.getRemainingDaysToDie());
            }
            for (Egg egg : farm.getEggs()) {
                assertEquals(1, egg.getDaysToBecomeChicken());
            }
        }

    }

    @Test
    public void testGranja1Nacen2Huevos() {
        // se configura el comportamiento de los repositorios.
        when(farmRepository.findById(anyInt())).thenReturn(granja1);
        // se llama al metodo.
        chickenEggService.passingTimeChicken(21, 1);

        //Al pasar 21 días los 2 huevos de la granja 1 se convierten en gallina con 100 días de vida
        // y quedan también 2 gallinas que existian que van a quedar en 79 días de vida.
        // No ponen ningun huevo por ahora porque ponen cada 30 días. Entonces quedaria con 0 huevos la granja

        // se verifica.

        //filtro las gallinas con 79 días y verifico que sean 2

        List<Chicken> chicken79dias = granja1.getChickens().stream().filter(c->c.getRemainingDaysToDie()==79).collect(Collectors.toList());
        assertEquals(2,chicken79dias.size());

        //filtro las gallinas con 100 días y verifico que sean 2

        List<Chicken> chicken100dias = granja1.getChickens().stream().filter(c->c.getRemainingDaysToDie()==100).collect(Collectors.toList());
        assertEquals(2,chicken100dias.size());

        //ahora verifico que la cantidad de gallinas sean 4 y los huevos 0

        assertEquals(4,granja1.getCantChickens());
        assertEquals(4,granja1.getChickens().size());


        assertEquals(0,granja1.getCantEggs());
        assertEquals(0,granja1.getEggs().size());


    }

    @Test
    public void testGallinasPonen2Huevos() {

        //para este caso en la granja 2 tenemos 1 gallina y 1 huevo
        //Se pasa el tiempo con 30 días

        //el huevo que tiene la granja en el día 21 se convierte en gallina, es decir se crea una nueva con 100 días de vida
        //después al pasar los 9 días restantes la gallina quedaria con 91 días de vida.

        //Para la otra gallina que ya estaba en la granja va a quedar con 70 días de vida. En el día 30 va a colocar
        // 2 huevos que se van a crear con 21 días para convertirse en gallina, pero al pasar el día 30 van a quedar entonces
        // con 20 días para convertirse en gallina para los 2 huevos.


        // se configura el comportamiento de los repositorios.
        when(farmRepository.findById(anyInt())).thenReturn(granja2);
        // se llama al metodo.
        chickenEggService.passingTimeChicken(30, 2);

        //se verifica
        //filtro las gallinas con 70 días y verifico que sea 1

        List<Chicken> chicken70dias = granja2.getChickens().stream().filter(c->c.getRemainingDaysToDie()==70).collect(Collectors.toList());
        assertEquals(1,chicken70dias.size());

        //filtro las gallinas con 91 días y verifico que sea 1

        List<Chicken> chicken91dias = granja2.getChickens().stream().filter(c->c.getRemainingDaysToDie()==91).collect(Collectors.toList());
        assertEquals(1,chicken91dias.size());

        //ahora verifico que la cantidad de huevos sean 2 y los huevos tengan 20 días

        List<Egg> eggs20dias = granja2.getEggs().stream().filter(e->e.getDaysToBecomeChicken()==20).collect(Collectors.toList());
        assertEquals(2,eggs20dias.size());


        assertEquals(2,granja2.getCantChickens());
        assertEquals(2,granja2.getChickens().size());


        assertEquals(2,granja2.getCantEggs());
        assertEquals(2,granja2.getEggs().size());

    }

    @Test
    public void venderGallinaGranja2(){

        // Para este caso tengo la granja 2 con 1 gallina y 1 huevo. Se vende 1 gallina y me tiene que quedar la
        // cantidad de gallinas en 0, la cantidad huevos 1 y la capacidad disponible en 89
        // La granja 1 compra esa gallina y deberia quedar con cantidad gallinas 3 y cantidad huevos 2
        // la capacidad disponible quedaria en 95

        //el dinero de la granja 2 quedaria en 9030 y el de la granja 1 9970

        when(farmRepository.findById(anyInt())).thenReturn(granja2);
        when(compraVentaService.obtenerGallinasVenta(anyInt(),anyInt())).thenReturn(listaGallinasGranja2);
        when(farmRepository.findFarmFromAdmin(anyInt(),anyDouble())).thenReturn(granja1);
        compraVentaService.vender(1,2,"Gallinas");


        int cantidadGallinasGranja2 = granja2.getCantChickens();
        int cantidadHuevosGranja2 = granja2.getCantEggs();
        int capacidadDisponibleGranja2 = granja2.getCapacidadDisponible();

        int cantidadGallinasGranja1 = granja1.getCantChickens();
        int cantidadHuevosGranja1 = granja1.getCantEggs();
        int capacidadDisponibleGranja1 = granja1.getCapacidadDisponible();


        assertEquals(0,cantidadGallinasGranja2);
        assertEquals(1,cantidadHuevosGranja2);
        assertEquals(89,capacidadDisponibleGranja2);
        assertEquals(9030.00,granja2.getMoney());

        assertEquals(3,cantidadGallinasGranja1);
        assertEquals(2,cantidadHuevosGranja1);
        assertEquals(95,capacidadDisponibleGranja1);
        assertEquals(9970.00,granja1.getMoney());
    }

    @Test
    public void comprarGallinasDesdeseGranja2(){

        // Para este caso tengo la granja 2 con 1 gallina y 1 huevo. Se compra 1 gallina y me tiene que quedar la
        // cantidad de gallinas en 2, la cantidad huevos 1 y la capacidad disponible en 87
        // La granja 1 vende esa gallina y deberia quedar con cantidad gallinas 1 y cantidad huevos 2
        // la capacidad disponible quedaria en 97

        //el dinero de la granja 2 quedaria en 9030 y el de la granja 1 9970


        List<Farm> listaGranjasFarmer2 = new ArrayList<>();
        listaGranjasFarmer2.add(granja2);
        farmer2.setFarms(listaGranjasFarmer2);

        when(farmerRepository.findByUserId(anyInt())).thenReturn(farmer2);
        when(compraVentaService.obtenerGallinasCompra(anyInt())).thenReturn(listaGallinasGranja1.subList(0,1));
        when(farmRepository.findById(anyInt())).thenReturn(granja1);


        compraVentaService.comprar(1,2,"Gallinas");


        int cantidadGallinasGranja2 = granja2.getCantChickens();
        int cantidadHuevosGranja2 = granja2.getCantEggs();
        int capacidadDisponibleGranja2 = granja2.getCapacidadDisponible();

        int cantidadGallinasGranja1 = granja1.getCantChickens();
        int cantidadHuevosGranja1 = granja1.getCantEggs();
        int capacidadDisponibleGranja1 = granja1.getCapacidadDisponible();


        assertEquals(2,cantidadGallinasGranja2);
        assertEquals(1,cantidadHuevosGranja2);
        assertEquals(87,capacidadDisponibleGranja2);
        assertEquals(8950.00,granja2.getMoney());

        assertEquals(1,cantidadGallinasGranja1);
        assertEquals(2,cantidadHuevosGranja1);
        assertEquals(97,capacidadDisponibleGranja1);
        assertEquals(10050.00,granja1.getMoney());
    }


}
