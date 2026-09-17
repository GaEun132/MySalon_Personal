-- 발급 원자 처리. Redis 단일 스레드 + 스크립트 단위 atomic 으로
-- 수용 인원 차감 + 사용자 추가 를 한 덩어리에 묶는다.
--
-- KEYS[1] = event:{eventId}:capacity (수용인원 카운터, 이벤트 생성 시 maxParticipant 로 초기화)
-- KEYS[2] = event:{eventId}:users   (발급된 사용자 set)
-- ARGV[1] = user_id
-- 반환:
--   1   참가 성공
--   0   매진
--  -1   이미 발급된 사용자

if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
  return -1 -- set에 해당 user_id가 존재 -> 중복
end

local remaining = tonumber(redis.call('GET', KEYS[1]) or '0') -- 수용인원 확인
if remaining <= 0 then -- 정원이 다참
  return 0
end

redis.call('DECR', KEYS[1]) -- 수용 인원 차감
redis.call('SADD', KEYS[2], ARGV[1]) -- set에 user_id 추가(sadd = set add)
return 1