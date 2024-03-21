package com.ice.songservice.mapper;

import com.ice.songservice.dto.SongDto;
import com.ice.songservice.entity.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface DtoToEntityMapper {

    DtoToEntityMapper INSTANCE = Mappers.getMapper(DtoToEntityMapper.class);
    @Mapping(source = "songId", target = "songId")
    @Mapping(source = "songName", target = "songName")
    @Mapping(source = "songDetails", target = "songDetails")
    @Mapping(source = "releaseYear", target = "releaseYear")
    @Mapping(source = "songUrl", target = "songUrl")
    @Mapping(source = "artistName", target = "artistName")
    Song songDtoToSong(SongDto songDto);

    List<Song> songDtosToSongs(List<SongDto> songDtos);
}
