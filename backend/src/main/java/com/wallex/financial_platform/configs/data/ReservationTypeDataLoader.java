package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.ReservationType;
import com.wallex.financial_platform.repositories.ReservationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationTypeDataLoader {

    private final ReservationTypeRepository reservationTypeRepository;

    public void load() {
        if (reservationTypeRepository.count() == 0) {
            ReservationType alimentacion = new ReservationType();
            alimentacion.setName("Alimentación");
            alimentacion.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755022242/dieta_hcx3xj.png");

            ReservationType educacion = new ReservationType();
            educacion.setName("Educación");
            educacion.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274789/aula_1_t93iue.png");

            ReservationType vacaciones = new ReservationType();
            vacaciones.setName("Vacaciones");
            vacaciones.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755022397/summer_5127274_nr1l3h.png");

            ReservationType salud = new ReservationType();
            salud.setName("Salud");
            salud.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274745/latido-del-corazon_ulcgif.png");

            ReservationType regalos = new ReservationType();
            regalos.setName("Regalo");
            regalos.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274769/caja-de-regalo_ejkygl.png");

            ReservationType deporte = new ReservationType();
            deporte.setName("Deporte");
            deporte.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755057681/images-play-attention/ufddjzbucebhafumupv6.png");

            ReservationType ocio = new ReservationType();
            ocio.setName("Ocio y Esparcimiento");
            ocio.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755273173/images-play-attention/rpx4o2nwlx2yioiadghw.png");

            ReservationType servicios = new ReservationType();
            servicios.setName("Servicios");
            servicios.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274834/canal_brcawd.png");
            reservationTypeRepository.saveAll(List.of(alimentacion, educacion, vacaciones, salud, regalos, deporte, ocio, servicios));
        }
    }
}
