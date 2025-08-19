package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.SuggestedReserve;
import com.wallex.financial_platform.repositories.SuggestedReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SuggestedReserveDataLoader {

    private final SuggestedReserveRepository reservationSuggestedReserveRepository;

    public void load() {
        if (reservationSuggestedReserveRepository.count() == 0) {
            SuggestedReserve alimentacion = new SuggestedReserve();
            alimentacion.setName("Alimentación");
            alimentacion.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755022242/dieta_hcx3xj.png");

            SuggestedReserve educacion = new SuggestedReserve();
            educacion.setName("Educación");
            educacion.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274789/aula_1_t93iue.png");

            SuggestedReserve vacaciones = new SuggestedReserve();
            vacaciones.setName("Vacaciones");
            vacaciones.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755022397/summer_5127274_nr1l3h.png");

            SuggestedReserve salud = new SuggestedReserve();
            salud.setName("Salud");
            salud.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274745/latido-del-corazon_ulcgif.png");

            SuggestedReserve regalos = new SuggestedReserve();
            regalos.setName("Regalo");
            regalos.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274769/caja-de-regalo_ejkygl.png");

            SuggestedReserve deporte = new SuggestedReserve();
            deporte.setName("Deporte");
            deporte.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755057681/images-play-attention/ufddjzbucebhafumupv6.png");

            SuggestedReserve ocio = new SuggestedReserve();
            ocio.setName("Ocio y Esparcimiento");
            ocio.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755273173/images-play-attention/rpx4o2nwlx2yioiadghw.png");

            SuggestedReserve servicios = new SuggestedReserve();
            servicios.setName("Servicios");
            servicios.setIconUrl("https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274834/canal_brcawd.png");
            reservationSuggestedReserveRepository.saveAll(List.of(alimentacion, educacion, vacaciones, salud, regalos, deporte, ocio, servicios));
        }
    }
}
