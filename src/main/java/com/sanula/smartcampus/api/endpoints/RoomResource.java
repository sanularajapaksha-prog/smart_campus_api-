package com.sanula.smartcampus.api.endpoints;

import com.sanula.smartcampus.core.domain.room.Room;
import com.sanula.smartcampus.infrastructure.persistence.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

import com.sanula.smartcampus.core.errors.RoomNotEmptyException;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // GET /api/v1/rooms
    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(DataStore.rooms.values());
    }

    // GET /api/v1/rooms/{id}
    @GET
    @Path("/{id}")
    public Room getRoomById(@PathParam("id") String id) {
        return DataStore.rooms.get(id);
    }

    // POST /api/v1/rooms
    @POST
    public Room createRoom(Room room) {
        if (room.getId() == null || room.getName() == null || room.getCapacity() <= 0) {
            throw new BadRequestException("Invalid room data");
        }

        DataStore.rooms.put(room.getId(), room);
        return room;
    }

    @DELETE
    @Path("/{id}")
    public Room deleteRoom(@PathParam("id") String id) {
        Room room = DataStore.rooms.get(id);

        if (room == null) {
            throw new NotFoundException("Room not found");
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because it still contains sensors");
        }

        return DataStore.rooms.remove(id);
    }
}