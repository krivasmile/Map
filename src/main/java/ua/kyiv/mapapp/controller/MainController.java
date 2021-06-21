package ua.kyiv.mapapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.kyiv.mapapp.model.Coordinates;
import ua.kyiv.mapapp.model.Location;

import ua.kyiv.mapapp.model.UserRole;
import ua.kyiv.mapapp.service.LocationService;

@Controller
public class MainController {
    private LocationService locationService;

    @Autowired
    public MainController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(value = { "/", "/welcome" })
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/map")
    public ModelAndView main(@RequestParam("role") String role) throws JsonProcessingException {
        ModelAndView map = new ModelAndView("map");
        UserRole userRole = role.equals("user") ? UserRole.USER : UserRole.ADMIN;
        map.addObject("userRole", userRole);
        map.addObject("coordinates", new Coordinates());
        map.addObject("locations", locationService.showAllLocations());
        ObjectMapper mapper = new ObjectMapper();
        map.addObject("locJSON", mapper.writeValueAsString(locationService.showAllLocations()));
        return map;
    }

    @PostMapping("/addLocation")
    public String addLocation(@ModelAttribute("coordinates") Coordinates coordinates, RedirectAttributes redirectAttributes){
        boolean checkCity = locationService.showAllLocations().stream().anyMatch((c) -> c.getCity().equals(coordinates.getCity()));
        Location location = new Location();
        if (checkCity){
            location = locationService.showAllLocations().stream().filter((c) -> c.getCity().equals(coordinates.getCity())).findFirst().get();
            coordinates.setLocation(location);
            location.getCoordinatesList().add(coordinates);
            locationService.save(location);
        }
        else {
            coordinates.setLocation(location);
            location.setCity(coordinates.getCity());
            location.getCoordinatesList().add(coordinates);
            locationService.save(location);
        }
        redirectAttributes.addAttribute("role", "admin");
        return "redirect:/map";
    }
}
