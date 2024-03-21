package com.ice.songservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "user_song_mapping",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    @JsonIgnore
    private Set<Song> songs = new HashSet<>();

    public void addSong(Song song){
        this.songs.add(song);
        if(song.getUsers() != null){
            song.getUsers().add(this);
        }else{
            Set<User> users = new HashSet<>();
            users.add(this);
            song.setUsers(users);
        }
    }

    public void updateSong(Song song, Song existingSong){
        if(existingSong != null){
            //Song existingSong = this.getSongs().stream().filter(s -> s.getSongId().equals(song.getSongId())).findFirst().orElse(null);
            existingSong.setSongDetails(song.getSongDetails());
            existingSong.setSongUrl(song.getSongUrl());
            existingSong.setSongName(song.getSongName());
            existingSong.setArtistName(song.getArtistName());
            existingSong.setReleaseYear(song.getReleaseYear());
            if(existingSong.getUsers() != null){
                existingSong.getUsers().add(this);
                this.songs.add(existingSong);
            }else{
                Set<User> users = new HashSet<>();
                users.add(this);
                existingSong.setUsers(users);
                this.songs.add(existingSong);
            }
        }

    }

    public void removeSong(UUID songId){
        Song song = this.getSongs().stream().filter(s -> s.getSongId().equals(songId)).findFirst().orElse(null);
        if(song != null){
            this.songs.remove(song);
            song.getUsers().remove(this);
        }
    }

}
