package ua.kyiv.mapapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kyiv.mapapp.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
