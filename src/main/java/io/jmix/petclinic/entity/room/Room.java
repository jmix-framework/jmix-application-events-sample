package io.jmix.petclinic.entity.room;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.petclinic.entity.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@JmixEntity
@Table(name = "PETCLINIC_ROOM")
@Entity(name = "petclinic_Room")
public class Room extends NamedEntity {
    @Column(name = "ROOM_NUMBER", nullable = false)
    @NotNull
    private String roomNumber;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}