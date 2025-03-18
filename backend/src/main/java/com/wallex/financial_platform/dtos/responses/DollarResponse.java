package com.wallex.financial_platform.dtos.responses;

import java.math.BigDecimal;

public record DollarResponse(

     String moneda,
     String casa,
     String nombre,
     BigDecimal compra,
     BigDecimal venta,
     String fechaActualizacion
) {
}
