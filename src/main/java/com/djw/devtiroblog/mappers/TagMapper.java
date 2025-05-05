package com.djw.devtiroblog.mappers;

import com.djw.devtiroblog.domain.PostStatus;
import com.djw.devtiroblog.domain.dtos.TagDto;
import com.djw.devtiroblog.domain.dtos.TagResponse;
import com.djw.devtiroblog.domain.entities.Post;
import com.djw.devtiroblog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagDto toTagResponse(Tag tag);

    List<TagResponse> toTagResponseList(List<Tag> tags);

    @Named("calculatePostCount")
    default Integer calculatePostCount(List<Post> posts){
        if(posts == null)
            return 0;

        return (int) posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
