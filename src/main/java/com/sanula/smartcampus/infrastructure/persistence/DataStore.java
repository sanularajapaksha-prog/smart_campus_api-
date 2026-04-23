package com.sanula.smartcampus.infrastructure.persistence;

import com.sanula.smartcampus.core.domain.room.Room;
import com.sanula.smartcampus.core.domain.sensor.Sensor;
import com.sanula.smartcampus.core.domain.reading.SensorReading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();

    private DataStore() {
    }
}