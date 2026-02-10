package my.aiorchestrator.domain.model.vos;

public record OutReviewVO(
        Long reviewId, String userName, String mail, String message, String feeling) {}
