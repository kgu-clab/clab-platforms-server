package page.clab.api.domain.community.comment.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.community.comment.domain.CommentLike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommentMapper {

    public CommentJpaEntity toJpaEntity(Comment comment) {
        Map<Long, CommentJpaEntity> mappedEntities = new HashMap<>();
        return toJpaEntity(comment, mappedEntities);
    }

    private CommentJpaEntity toJpaEntity(Comment comment, Map<Long, CommentJpaEntity> mappedEntities) {
        if (comment == null) return null;
        if (mappedEntities.containsKey(comment.getId())) {
            return mappedEntities.get(comment.getId());
        }

        CommentJpaEntity parentEntity = toJpaEntity(comment.getParent(), mappedEntities);
        CommentJpaEntity entity = CommentJpaEntity.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .writerId(comment.getWriterId())
                .nickname(comment.getNickname())
                .content(comment.getContent())
                .parent(parentEntity)
                .children(new ArrayList<>())
                .wantAnonymous(comment.isWantAnonymous())
                .likes(comment.getLikes())
                .isDeleted(comment.isDeleted())
                .build();

        mappedEntities.put(comment.getId(), entity);
        for (Comment child : comment.getChildren()) {
            entity.getChildren().add(toJpaEntity(child, mappedEntities));
        }
        return entity;
    }

    public Comment toDomain(CommentJpaEntity entity) {
        Map<Long, Comment> mappedDomains = new HashMap<>();
        return toDomain(entity, mappedDomains);
    }

    private Comment toDomain(CommentJpaEntity entity, Map<Long, Comment> mappedDomains) {
        if (entity == null) return null;
        if (mappedDomains.containsKey(entity.getId())) {
            return mappedDomains.get(entity.getId());
        }

        Comment parentDomain = toDomain(entity.getParent(), mappedDomains);
        Comment domain = Comment.builder()
                .id(entity.getId())
                .boardId(entity.getBoardId())
                .writerId(entity.getWriterId())
                .nickname(entity.getNickname())
                .content(entity.getContent())
                .parent(parentDomain)
                .children(new ArrayList<>())
                .wantAnonymous(entity.isWantAnonymous())
                .likes(entity.getLikes())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .build();

        mappedDomains.put(entity.getId(), domain);
        for (CommentJpaEntity child : entity.getChildren()) {
            domain.getChildren().add(toDomain(child, mappedDomains));
        }
        return domain;
    }

    public CommentLikeJpaEntity toJpaEntity(CommentLike commentLike) {
        return CommentLikeJpaEntity.builder()
                .commentLikeId(commentLike.getCommentLikeId())
                .memberId(commentLike.getMemberId())
                .commentId(commentLike.getCommentId())
                .build();
    }

    public CommentLike toDomain(CommentLikeJpaEntity entity) {
        return CommentLike.builder()
                .commentLikeId(entity.getCommentLikeId())
                .memberId(entity.getMemberId())
                .commentId(entity.getCommentId())
                .build();
    }
}
