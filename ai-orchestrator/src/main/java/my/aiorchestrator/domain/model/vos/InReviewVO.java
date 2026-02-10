package my.aiorchestrator.domain.model.vos;

public record InReviewVO(
        Long reviewId, String userName, String mail, String message) {}
