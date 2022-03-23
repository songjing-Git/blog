package com.threeman.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.threeman.common.exception.CreateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/8 20:25
 */
@Component
@Slf4j
public class JwtUtil {



    private final static String KEY = "singJin3";

    private final static long OUT_TIME =60*60*1000;


    public static String getJwtToken(String userId, String username ,String password, List<String> authorities){
        Map<String, Object> map = new HashMap<>(16);
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        Date date = new Date();
        log.info("date:{}",date);
        //创建jwt  id jwt标识
        return JWT.create().withJWTId(userId)
                .withHeader(map)
                //标识收件人
                .withAudience("front-end")
                //发布这个JWT的当事人
                .withIssuer("back-end")
                //关于subject的陈述
                .withSubject(username)
                //载荷
                .withClaim("authority", authorities)
                .withClaim("username",username)
                .withClaim("password",password)
                //标识JWT所颁发的时间
                .withIssuedAt(date)
                //标识了时间点，当早于这个时间点，JWT不会被接受和处理
                .withNotBefore(date)
                //jwt失效时间
                .withExpiresAt(new Date(System.currentTimeMillis()+OUT_TIME))
                //加密方式+盐
                .sign(Algorithm.HMAC256(KEY));
    }

    /**
     * @description 获取jwt载荷信息
     * @param jwtToken
     * @return
     */
    public static Map<String,Claim> getPayload(String jwtToken){
        Map<String,Object> map= new HashMap<>(10);
        if (StringUtils.isEmpty(jwtToken)){
            throw new CreateException(10000,"jwtToken不能为空");
        }
        DecodedJWT decode = JWT.decode(jwtToken);
        return decode.getClaims();
    }


    /**
     * 判断jwt令牌是否失效
     * @param jwtToken
     * @return
     */
    public static boolean isJwtExpire(String jwtToken) {
        Map<String, Claim> payload = getPayload(jwtToken);
        if (payload!=null&&!payload.isEmpty()){
           /* Date exp = payload.get("exp").asDate();
            if (exp.before(new Date())){
                return true;
            }
            return false;*/
            return true;
        }
        return false;
    }




}
