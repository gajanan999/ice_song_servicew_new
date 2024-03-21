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
public class Movie {

    @Id
    @GeneratedValue
    @Column(name = "movie_id", updatable = false, nullable = false)
    private UUID movieId;

    @Column(name = "movie_name")
    private String songName;

    @Column(name = "movie_details")
    private String movieDetails;

    @Column(name = "release_year")
    private Integer releaseYear;

//    @OneToMany(mappedBy = "movie")
//    private Set<Song> songs;
}
