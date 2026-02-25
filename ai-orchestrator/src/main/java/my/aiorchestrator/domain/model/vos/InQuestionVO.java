package my.aiorchestrator.domain.model.vos;

import java.time.LocalDateTime;

public record InQuestionVO(
        Long user,
        String fileName,
        String originalFileName,
        String question,
        String sessionId,
        String documentId,
        LocalDateTime uploadedAt,
        String aiProvider,
        String aiModel) {}
