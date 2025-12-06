package com.cusco.limpio.service;

import com.cusco.limpio.domain.model.Location;

import java.util.List;

public interface LocationService {
	Location createLocation(Location location);
	Location getLocationById(Long id);
	List<Location> getAllLocations();
}
