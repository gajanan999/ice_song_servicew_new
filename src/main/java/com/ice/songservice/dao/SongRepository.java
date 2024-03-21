package com.ice.songservice.dao;

import com.ice.songservice.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface SongRepository extends JpaRepository<Song, UUID> {

    boolean existsBySongUrl(String emailId);

    boolean existsBySongName(String userName);
}
