package my.aiorchestrator.domain.model.vos;

import java.time.LocalDateTime;

public record InQuestionVO(
        Long userId,
        String filename,
        String originalFilename,
        String question,
        String sessionId,
        String documentId,
        LocalDateTime uploadedAt,
        String aiProvider,
        String aiModel) {}
