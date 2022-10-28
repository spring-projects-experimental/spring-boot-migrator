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

import lombok.RequiredArgsConstructor;
import org.springboot.example.controllers.dto.Song;
import org.springboot.example.entity.SongStat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongStatRepository songStatRepository;

    public void songPlayed(Song song) {
        Optional<SongStat> savedSong = songStatRepository.findById(song.getId());

        int songCount = 1;

        if (savedSong.isPresent()) {
            songCount = savedSong.get().getCount() + 1;
        }

        songStatRepository.save(new SongStat(song.getId(), song.getSongName(), songCount));
    }

    public List<Song> topSongs() {

        return StreamSupport
                .stream(songStatRepository.findAll().spliterator(), false)
                .sorted((o1, o2) -> o2.getCount() - o1.getCount())
                .map(k -> Song.builder().songName(k.getSongName()).id(k.getId()).build())
                .collect(Collectors.toList());

    }
}
