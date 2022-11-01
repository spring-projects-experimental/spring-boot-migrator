/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springboot.example.services;

import com.translation.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springboot.example.controllers.dto.Song;
import org.springboot.example.controllers.dto.SongPlayedRequest;
import org.springboot.example.controllers.dto.TopSongs;
import org.springboot.example.entity.SongStat;
import org.springboot.example.upgrade.RegionConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongStatRepository songStatRepository;
    private final RegionConfig regionConfig;
    private final TranslationService translationService;

    public void songPlayed(SongPlayedRequest songPlayedRequest) {
        Song song = songPlayedRequest.getSong();
        Optional<SongStat> savedSong = songStatRepository.findById(song.getId());

        int songCount = songPlayedRequest.getPlayedTimes();

        if (savedSong.isPresent()) {
            songCount = savedSong.get().getTimesPlayed() + 1;
        }

        songStatRepository.save(new SongStat(
                song.getId(),
                song.getSongName(),
                songCount,
                songPlayedRequest.getRegion()
        ));
    }

    public TopSongs topSongs() {

        List<SongStat> top3Songs = songStatRepository
                .findTop10SongsByRegionOrderByTimesPlayedDesc(regionConfig.getRegionCode());

        List<Song> sortedSongList = top3Songs
                .stream()
                .map(k -> Song.builder().songName(k.getSongName()).id(k.getId()).build())
                .collect(Collectors.toList());

        return TopSongs.builder()
                .region(regionConfig.getRegionCode())
                .title(translationService.translate("Top 10 songs"))
                .songs(sortedSongList)
                .build();

    }
}
