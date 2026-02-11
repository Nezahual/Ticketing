package my.aiorchestrator.domain.model.vos;

public record OutReviewVO(
        Long reviewId,
        Long userId,
        OutFeelingScoreVO feeling,
        int promptTokens,
        int completionTokens,
        String model) {}
