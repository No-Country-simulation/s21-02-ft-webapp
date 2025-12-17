package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.SuggestedReserve;
import com.wallex.financial_platform.repositories.SuggestedReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SuggestedReserveDataLoader {

    private final SuggestedReserveRepository suggestedReserveRepository;

    private record ReserveData(String name, String iconUrl, Integer order) {}

    private static final List<ReserveData> DEFAULT_RESERVES = Arrays.asList(
            new ReserveData("Alimentación", "https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755022242/dieta_hcx3xj.png", 1),
            new ReserveData("Educación", "https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274789/aula_1_t93iue.png", 2),
            new ReserveData("Vacaciones", "https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755022397/summer_5127274_nr1l3h.png", 3),
            new ReserveData("Salud", "https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274745/latido-del-corazon_ulcgif.png", 4),
            new ReserveData("Regalo", "https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274769/caja-de-regalo_ejkygl.png", 5),
            new ReserveData("Deporte", "https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755057681/images-play-attention/ufddjzbucebhafumupv6.png", 6),
            new ReserveData("Ocio y Esparcimiento", "https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755273173/images-play-attention/rpx4o2nwlx2yioiadghw.png", 7),
            new ReserveData("Servicios", "https://res.cloudinary.com/dtyp7s5ql/image/upload/v1755274834/canal_brcawd.png", 8)
    );

    public void load() {
        long existingCount = suggestedReserveRepository.count();

        if (existingCount >= DEFAULT_RESERVES.size()) {
            System.out.println("📝 Las reservas sugeridas ya están cargadas");
            return;
        }

        System.out.println("🔄 Cargando reservas sugeridas...");
        List<SuggestedReserve> reservesToCreate = new ArrayList<>();
        int created = 0;

        for (ReserveData reserveData : DEFAULT_RESERVES) {
            boolean exists = suggestedReserveRepository.existsByNameIgnoreCase(reserveData.name);

            if (!exists) {
                SuggestedReserve suggestedReserve = new SuggestedReserve();
                suggestedReserve.setName(reserveData.name);
                suggestedReserve.setIconUrl(reserveData.iconUrl);

                trySetOrder(suggestedReserve, reserveData.order);

                reservesToCreate.add(suggestedReserve);
                created++;
            }
        }

        if (!reservesToCreate.isEmpty()) {
            suggestedReserveRepository.saveAll(reservesToCreate);
            System.out.println("✅ " + created + " reservas sugeridas creadas");
        } else {
            System.out.println("📝 No se necesitó crear nuevas reservas");
        }
    }

    private void trySetOrder(SuggestedReserve reserve, Integer order) {
        try {
            var orderField = reserve.getClass().getDeclaredField("displayOrder");
            orderField.setAccessible(true);
            orderField.set(reserve, order);
        } catch (NoSuchFieldException ignored) {

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo establecer orden para reserva: " + e.getMessage());
        }
    }

    public void checkStatus() {
        long total = suggestedReserveRepository.count();
        System.out.println("📊 Estado de reservas sugeridas: " + total + " registros");

        if (total < DEFAULT_RESERVES.size()) {
            System.out.println("⚠️ Faltan " + (DEFAULT_RESERVES.size() - total) + " reservas por cargar");
        }
    }
}