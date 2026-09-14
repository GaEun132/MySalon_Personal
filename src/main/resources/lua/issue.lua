local remaining = tonumber(redis.call('GET', KEYS[1]) or '0')
-- 매개변수 키로 값(발급 수량)을 받아옴
if remaining <= 0 then -- 재고가 없으면 0을 리턴
  return 0
end
redis.call('DECR', KEYS[1]) -- 재고를 차감
return 1