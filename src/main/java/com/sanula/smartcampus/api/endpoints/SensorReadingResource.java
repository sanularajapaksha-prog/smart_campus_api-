package com.sanula.smartcampus.api.endpoints;

import com.sanula.smartcampus.core.domain.sensor.Sensor;
import com.sanula.smartcampus.core.domain.reading.SensorReading;
import com.sanula.smartcampus.infrastructure.persistence.DataStore;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sanula.smartcampus.core.errors.SensorUnavailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Path("/sensors/{sensorId}/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    @GET
    public List<SensorReading> getReadings(@PathParam("sensorId") String sensorId) {
        if (!DataStore.sensors.containsKey(sensorId)) {
            throw new BadRequestException("Sensor does not exist");
        }

        return DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public SensorReading addReading(@PathParam("sensorId") String sensorId, SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            throw new BadRequestException("Sensor does not exist");
        }

        if (reading == null) {
            throw new BadRequestException("Reading data is required");
        }

        if (reading.getId() == null || reading.getId().isBlank()) {
            reading.setId(UUID.randomUUID().toString());
        }

        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is in maintenance mode and cannot accept readings");
        }

        DataStore.readings.putIfAbsent(sensorId, new ArrayList<>());
        DataStore.readings.get(sensorId).add(reading);

        sensor.setCurrentValue(reading.getValue());

        return reading;
    }
}