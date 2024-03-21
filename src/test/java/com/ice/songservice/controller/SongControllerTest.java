package com.ice.songservice.controller;

import com.ice.songservice.dto.RestResponse;
import com.ice.songservice.dto.SongDto;
import com.ice.songservice.exception.NoRecordFound;
import com.ice.songservice.exception.RecordAlreadyExistsException;
import com.ice.songservice.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


/**
 *  Song Controller Test
 */
public class SongControllerTest {

    @Mock
    private SongService songService;

    @InjectMocks
    private SongController songController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllSongs() {
        // Mocking input parameters
        Optional<UUID> userId = Optional.of(UUID.randomUUID());
        Optional<String> releaseYear = Optional.of("2022");
        Optional<String> artistName = Optional.empty();
        Optional<String> sortBy = Optional.empty();
        Optional<List<String>> sortByColumns = Optional.of(new ArrayList<>());

        // Mocking songDtos
        List<SongDto> songDtos = new ArrayList<>();


        when(songService.getAllSongs(userId, releaseYear, artistName, sortBy, sortByColumns))
                .thenReturn(songDtos);


        ResponseEntity<?> responseEntity = songController.getAllSongs(userId, releaseYear, artistName, sortBy, sortByColumns);

        verify(songService).getAllSongs(userId, releaseYear, artistName, sortBy, sortByColumns);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(songDtos, responseEntity.getBody());
    }

    @Test
    void testGetSongsCount() {

        Optional<UUID> userId = Optional.of(UUID.randomUUID());

        Long songCount = 10L;

        when(songService.getSongsCount(userId)).thenReturn(songCount);

        ResponseEntity<?> responseEntity = songController.getSongsCount(userId);

        verify(songService).getSongsCount(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(songCount, ((RestResponse) responseEntity.getBody()).getData());
    }

    @Test
    void testCreateSong() throws RecordAlreadyExistsException {

        SongDto songDto = new SongDto();
        songDto.setSongName("Test Song");

        SongDto createdSongDto = new SongDto();
        createdSongDto.setSongId(UUID.randomUUID());
        createdSongDto.setSongName("Test Song");

        when(songService.createSong(songDto)).thenReturn(createdSongDto);

        ResponseEntity<?> responseEntity = songController.createSong(songDto);

        verify(songService).createSong(songDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(createdSongDto, responseEntity.getBody());
    }

    @Test
    void testUpdateSong() throws NoRecordFound {

        SongDto songDto = new SongDto();
        songDto.setSongId(UUID.randomUUID());
        songDto.setSongName("Updated Song");


        SongDto updatedSongDto = new SongDto();
        updatedSongDto.setSongId(songDto.getSongId());
        updatedSongDto.setSongName(songDto.getSongName());

        when(songService.updateSong(songDto)).thenReturn(updatedSongDto);

        ResponseEntity<?> responseEntity = songController.updateSong(songDto);

        verify(songService).updateSong(songDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedSongDto, responseEntity.getBody());
    }

    @Test
    void testDeleteSong() throws NoRecordFound {

        UUID songId = UUID.randomUUID();

        doNothing().when(songService).deleteSong(songId);

        ResponseEntity<?> responseEntity = songController.deleteSong(songId);

        verify(songService).deleteSong(songId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
