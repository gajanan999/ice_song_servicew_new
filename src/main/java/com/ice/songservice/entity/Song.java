package com.ice.songservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Song {

    @Id
    @GeneratedValue
    @Column(name = "song_id", updatable = false, nullable = false)
    private UUID songId;

    @Column(name = "song_name")
    private String songName;

    @Column(name = "song_details")
    private String songDetails;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "song_url")
    private String songUrl;

    @Column(name = "artist_name")
    private String artistName;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "songs")
    Set<User> users;



}
