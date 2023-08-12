package NestNet.NestNetWebSite.domain.member;

/**
 * 회원 권한 ENUM    회장, 부회장, 임원, 서버관리자, 재학생, 휴학생, 졸업생, 승인대기중
 */
public enum MemberAuthority {
    ADMIN, PRESIDENT, VICE_PRESIDENT, MANAGER, GENERAL_MEMBER, ON_LEAVE_MEMBER, GRADUATED_MEMBER, WAITING_FOR_APPROVAL
}
