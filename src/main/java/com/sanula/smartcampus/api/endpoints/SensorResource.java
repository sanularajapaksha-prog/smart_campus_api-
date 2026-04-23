package com.sanula.smartcampus.api.endpoints;

import com.sanula.smartcampus.core.domain.room.Room;
import com.sanula.smartcampus.core.domain.sensor.Sensor;
import com.sanula.smartcampus.infrastructure.persistence.DataStore;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sanula.smartcampus.core.errors.LinkedResourceNotFoundException;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(DataStore.sensors.values());

        if (type != null && !type.isBlank()) {
            return sensors.stream()
                    .filter(sensor -> sensor.getType() != null && sensor.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return sensors;
    }

    @POST
    public Sensor createSensor(Sensor sensor) {
        if (sensor == null ||
                sensor.getId() == null ||
                sensor.getType() == null ||
                sensor.getStatus() == null ||
                sensor.getRoomId() == null) {
            throw new BadRequestException("Invalid sensor data");
        }

        Room room = DataStore.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException("Room does not exist for the given roomId");
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        room.getSensorIds().add(sensor.getId());

        return sensor;
    }
}