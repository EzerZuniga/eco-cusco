package com.cusco.limpio.service.impl;

import com.cusco.limpio.domain.model.Location;
import com.cusco.limpio.exception.ResourceNotFoundException;
import com.cusco.limpio.repository.LocationRepository;
import com.cusco.limpio.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
