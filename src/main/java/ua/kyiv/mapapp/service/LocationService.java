package ua.kyiv.mapapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kyiv.mapapp.model.Location;
import ua.kyiv.mapapp.repo.LocationRepository;

import java.util.List;

@Service
public class LocationService {
    private LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void save(Location location){
        locationRepository.save(location);
    }

    public List<Location> showAllLocations(){
        List<Location> locations = locationRepository.findAll();
        locations.sort((l1, l2) -> l1.getCity().compareTo(l2.getCity()));
        return locations;
    }
}
