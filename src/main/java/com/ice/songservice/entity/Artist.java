package com.ice.songservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Artist {

    @Id
    @GeneratedValue
    @Column(name = "artist_id", updatable = false, nullable = false)
    private UUID artistId;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "artist_details")
    private String artistDetails;


}
