package com.example.shopping.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 인증/인가 관련 (40X)
    AUTH_INVALID_CREDENTIALS("AUTH_001", "이메일 또는 비밀번호가 올바르지 않습니다", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_EXPIRED("AUTH_002", "토큰이 만료되었습니다", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_INVALID("AUTH_003", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),
    AUTH_ACCESS_DENIED("AUTH_004", "접근 권한이 없습니다", HttpStatus.FORBIDDEN),
    AUTH_ACCOUNT_LOCKED("AUTH_005", "계정이 잠겨있습니다", HttpStatus.FORBIDDEN),
    USER_UNAUTHORIZED("AUTH_006", "권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    // 검증 관련 (40X)
    VALIDATION_ERROR("VALID_001", "입력값이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    REQUIRED_FIELD_MISSING("VALID_002", "필수 항목이 누락되었습니다", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT("VALID_003", "형식이 올바르지 않습니다", HttpStatus.BAD_REQUEST),

    // 비즈니스 로직 관련 (40X)
    BUSINESS_RULE_VIOLATION("BIZ_001", "비즈니스 규칙 위반입니다", HttpStatus.BAD_REQUEST),
    OPERATION_NOT_ALLOWED("BIZ_002", "허용되지 않은 작업입니다", HttpStatus.BAD_REQUEST),
    //사용자 관련
    USER_NOT_FOUND("USER_001","사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_ID_DUPLICATE("USER_002","중복된 아이디입니다.", HttpStatus.BAD_REQUEST),
    USER_NAME_DUPLICATE("USER_002","중복된 이름입니다.", HttpStatus.BAD_REQUEST),
    //상품 관련
    PRODUCT_NOT_FOUND("PRODUCT_001", "상품을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    STOCK_NOT_ENOUGH("PRODUCT_002", "재고가 부족합니다", HttpStatus.BAD_REQUEST),
    // 리뷰
    REVIEW_NOT_FOUND("REVIEW_001", "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REVIEW_IMAGE_NOT_FOUND("REVIEW_002", "리뷰 이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    // 카테고리
    CATEGORY_NOT_FOUND("CATEGORY_001", "카테고리를 찾을 수 없습니다",HttpStatus.NOT_FOUND),
    // 쿠폰
    COUPON_NOT_FOUND("COUPON_001", "쿠폰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COUPON_NOT_ISSUABLE("COUPON_002", "쿠폰을 발급이 아직 시작되지 않았습니다.", HttpStatus.CONFLICT),
    COUPON_SOLD_OUT("COUPON_003", "쿠폰이 소진되었습니다.", HttpStatus.CONFLICT),
    COUPON_ALREADY_EXIST("COUPON_004", "이미 발급된 쿠폰입니다.", HttpStatus.CONFLICT),
    // 카트
    CART_NOT_FOUND("CART_001", "카트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CART_ALREADY_EXISTS("CART_002", "이미 추가된 상품입니다.", HttpStatus.BAD_REQUEST),
    // 발행
    ISSUANCE_NOT_FOUND("ISSUANCE_001", "발행 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COUPON_ALREADY_USED("ISSUANCE_002", "이미 사용한 쿠폰입니다.", HttpStatus.BAD_REQUEST),
    COUPON_EXPIRED("ISSUANCE_003", "만료된 쿠폰입니다.", HttpStatus.BAD_REQUEST),
    // 큐
    QUEUE_IS_FULL("QUEUE_001", "큐가 가득 찼습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    // 주문
    ALREADY_DELIVERED("ORDER_002","이미 배송완료된 상품은 취소가 불가능합니다.", HttpStatus.BAD_REQUEST),
    // 아이템
    ITEM_NOT_FOUND("ITEM_001","아이템을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    // 주소
    ADDRESS_NOT_FOUND("ADDRESS_002","주소를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    // 이벤트
    EVENT_NOT_FOUND("EVENT_001","이벤트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    // 이벤트 참가
    EVENT_PARTICIPANT_NOT_FOUND("EVENT_001","이벤트 참가내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EVENT_NOT_STARTED("EVENT_002", "이벤트가 아직 시작되지 않았습니다", HttpStatus.CONFLICT),
    EVENT_FULL_BOOKED("EVENT_003", "이벤트 정원이 다찼습니다", HttpStatus.CONFLICT),
    EVENT_ALREADY_PARTICIPATED("EVENT_004", "이미 참여한 이벤트입니다", HttpStatus.CONFLICT),
    EVENT_ENDED("EVENT_005", "이벤트가 종료되었습니다", HttpStatus.CONFLICT),
    //게시물 관련
    POST_NOT_FOUND("POST_001","게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_TYPE_WRONG("POST_002","코디 게시물이 아닙니다.", HttpStatus.BAD_REQUEST),
    // 코디 게시물
    COORDI_POST_NOT_FOUND("COORDI_POST_001","코디 게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 댓글
    COMMENT_NOT_FOUND("COMMENT_001","댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    // 서버 오류 (50X)
    INTERNAL_SERVER_ERROR("SERVER_001", "서버 내부 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR("SERVER_002", "데이터베이스 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    EXTERNAL_API_ERROR("SERVER_003", "외부 API 호출 중 오류가 발생했습니다", HttpStatus.BAD_GATEWAY);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }


}