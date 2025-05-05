package com.djw.devtiroblog.services.impl;

import com.djw.devtiroblog.domain.entities.Tag;
import com.djw.devtiroblog.repositories.TagRepository;
import com.djw.devtiroblog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);

        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        List<Tag> savedTags = new ArrayList<>();
        if(!newTags.isEmpty())
            savedTags = tagRepository.saveAll(newTags);

        savedTags.addAll(existingTags);

        return savedTags;
    }

    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
            if(!tag.getPosts().isEmpty())
                throw new IllegalStateException("Cannot Delete Tag with Posts");
            tagRepository.deleteById(id);
        });
    }
}
