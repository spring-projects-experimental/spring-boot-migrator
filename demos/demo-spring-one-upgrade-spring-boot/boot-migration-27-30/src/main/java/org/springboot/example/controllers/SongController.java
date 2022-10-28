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

package org.springboot.example.controllers;

import lombok.RequiredArgsConstructor;
import org.springboot.example.controllers.dto.Song;
import org.springboot.example.controllers.dto.TopSongs;
import org.springboot.example.upgrade.RegionConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SongController {

    private final RegionConfig regionConfig;

    @GetMapping("/top-songs")
    public List<TopSongs> getTopSongs() {
        return List.of(
                TopSongs.builder()
                        .region(
                                regionConfig.getRegionCode()
                        )
                        .songs(List.of(
                                        Song.builder()
                                                .id(UUID.randomUUID().toString())
                                                .songName("someName")
                                                .build()
                                )
                        )
                        .build());
    }
}
