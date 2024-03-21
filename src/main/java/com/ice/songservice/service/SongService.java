package com.ice.songservice.service;

import com.ice.songservice.constant.ErrorCodeConstant;
import com.ice.songservice.constant.SortConstant;
import com.ice.songservice.dao.SongRepository;
import com.ice.songservice.dto.SongDto;
import com.ice.songservice.entity.Song;
import com.ice.songservice.entity.User;
import com.ice.songservice.exception.NoRecordFound;
import com.ice.songservice.exception.RecordAlreadyExistsException;
import com.ice.songservice.mapper.DtoToEntityMapper;
import com.ice.songservice.mapper.EntityToDtoMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Song is used for CRUD operations on song table
 */
@Service
public class SongService {

    private static final Logger logger = LoggerFactory.getLogger(SongService.class);

    private SongRepository songRepository;

    @Autowired
    MessageSource messageSource;

    private EntityManager entityManager;

    @Autowired
    public SongService(SongRepository songRepository, EntityManager entityManager){
        this.songRepository = songRepository;
        this.entityManager = entityManager;
    }

    public List<SongDto> getAllSongs(Optional<UUID> userId, Optional<String> releaseYear, Optional<String> artistName, Optional<String> sortBy, Optional<List<String>> sortByColumns) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Song> cq = cb.createQuery(Song.class);
        Root<Song> root = cq.from(Song.class);
        List<Predicate> predicates = new ArrayList<>();
        if (userId.isPresent()) {
            Join<Song, User> userJoin = root.join("users");
            predicates.add(cb.equal(userJoin.get("id"), userId.get()));
        }
        if(releaseYear.isPresent() && !releaseYear.get().equals("NaN")){
            predicates.add(cb.equal(root.get("releaseYear"), releaseYear.get()));
        }
        if(artistName.isPresent()){
            predicates.add(cb.equal(root.get("artistName"), artistName.get()));
        }
        if(sortBy.isPresent()){
            if(sortByColumns.isPresent()){
                List<Order> orders = new ArrayList<>();
                if (SortConstant.ASC.toString().equals(sortBy.get())) {
                    for(String columnName: sortByColumns.get()){
                        orders.add(cb.asc(root.get(columnName)));
                    }
                }else{
                    for(String columnName: sortByColumns.get()){
                        orders.add(cb.desc(root.get(columnName)));
                    }
                }
                if (!orders.isEmpty()) {
                    cq.orderBy(orders);
                }
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        List<Song> songs = entityManager.createQuery(cq).getResultList();
        entityManager.clear();
        entityManager.close();
        return EntityToDtoMapper.INSTANCE.songsToSongDtos(songs);
    }

    public SongDto createSong(SongDto songDto) throws RecordAlreadyExistsException {
        Song song = DtoToEntityMapper.INSTANCE.songDtoToSong(songDto);

        // Check if a user with the same email  already exists
        if (songRepository.existsBySongUrl(song.getSongUrl())) {
            throw new RecordAlreadyExistsException(ErrorCodeConstant.ERR_103,messageSource.getMessage(ErrorCodeConstant.ERR_103,null, Locale.ENGLISH));
        }
        song = songRepository.save(song);
        SongDto createdUserDto = EntityToDtoMapper.INSTANCE.songToSongDto(song);
        return createdUserDto;
    }

    public SongDto updateSong(SongDto userDto) throws NoRecordFound {
        Optional<Song> existUser = songRepository.findById(userDto.getSongId());

        if(existUser.isPresent()){
            Song user = DtoToEntityMapper.INSTANCE.songDtoToSong(userDto);
            songRepository.save(user);

        }else{
            throw new NoRecordFound(ErrorCodeConstant.ERR_101,messageSource.getMessage(ErrorCodeConstant.ERR_101,null, Locale.ENGLISH));
        }
        Optional<Song> modifiedUser = songRepository.findById(userDto.getSongId());
        SongDto modifiedUserDto = EntityToDtoMapper.INSTANCE.songToSongDto(modifiedUser.get());
        return modifiedUserDto;
    }

    public void deleteSong(UUID userId) throws NoRecordFound {
        Optional<Song> existUser = songRepository.findById(userId);
        if(!existUser.isPresent()){
            throw new NoRecordFound(ErrorCodeConstant.ERR_101,messageSource.getMessage(ErrorCodeConstant.ERR_101,null, Locale.ENGLISH));
        }else{
            songRepository.deleteById(userId);
        }
    }

    public Long getSongsCount(Optional<UUID> userId) {
        if(userId.isPresent()){
            return 0l;//(long) userSongMappingRepository.findByUserId(userId.get()).size();
        }else{
            return songRepository.count();
        }
    }
}
