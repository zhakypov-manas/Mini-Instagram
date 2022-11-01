package com.zhakypov.lesson.utility;

import com.zhakypov.lesson.dao.UserDao;
import com.zhakypov.lesson.dto.UserAddDataBaseDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class Utils {
    public static String readCookie(HttpServletRequest request, UserDao dao) {
        if (Objects.nonNull(request.getCookies())){
            List<Cookie> cookies = List.of(request.getCookies());
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("user-email")){

                    String value = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    dao.checkUserByEmail(value);
                    return value;
                }
            }
        }
        return " ";
    }
}
