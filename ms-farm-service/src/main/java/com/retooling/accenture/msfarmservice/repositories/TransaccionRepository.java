package com.retooling.accenture.msfarmservice.repositories;

import com.retooling.accenture.msfarmservice.model.TipoTransaccionProducto;
import com.retooling.accenture.msfarmservice.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    List<Transaccion> findByTipoTransaccionAndFarmerUserIdOrderByFechaDesc(TipoTransaccionProducto tipoTransaccionProducto, int farmerId);

}
