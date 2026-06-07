package com.example.blogsystemconsumer.controller;

import com.example.blogsystem.entity.User;
import com.example.blogsystemconsumer.service.ArticleProviderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
public class PageController {
    // Page access follows the same session-scoped login state used by API calls.
    private static final String SESSION_USER = "user";

    @Resource
    private ArticleProviderService articleProviderService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOGIN = "login";

    private static final String ERROR = "error";

    private static final Logger logger = LoggerFactory.getLogger(PageController.class);

    @RequestMapping(value = "getLogin")
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView(LOGIN);
        modelAndView.addObject("data", "Hello World!!!I ma the Login page!");
        return modelAndView;
    }

    @RequestMapping(value = "getRegister")
    public ModelAndView getRegister() {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "getIndex")
    public ModelAndView getIndex(HttpSession session) {
        return userPage(session, "index");
    }

    @RequestMapping(value = "getProfile")
    public ModelAndView getProfile(HttpSession session) {
        return userPage(session, "profile");
    }

    @RequestMapping(value = "getArticleShow")
    public ModelAndView getArticleShow(HttpSession session) {
        User user = getSessionUser(session);
        if (user == null) {
            return new ModelAndView(LOGIN);
        }
        logger.info(user.getUserId());
        return new ModelAndView("article-show")
                .addObject("user", user)
                .addObject("articleList", articleProviderService.getArticleListById(user.getUserId()));
    }

    @RequestMapping(value = "getForgetPassword")
    public ModelAndView getForgetPassword() {
        return new ModelAndView("forget-password");
    }

    @RequestMapping(value = "getResetPassword")
    public ModelAndView getResetPassword(@RequestParam("token") String token) {
        if (StringUtils.isBlank(token)) {
            return new ModelAndView(ERROR).addObject("message", "非法访问");
        }
        // Each reset link owns its own Redis entry, so concurrent reset requests do not overwrite each other.
        String resetKey = "resetPwd:" + token;
        if (redisTemplate.getExpire(resetKey) == -2) {
            return new ModelAndView(ERROR).addObject("message", "修改时间已经过期!");
        }
        return new ModelAndView("reset-password").addObject("token", token);
    }

    @RequestMapping(value = "getArticle")
    public ModelAndView getArticle(HttpSession session) {
        return userPage(session, "article");
    }

    @RequestMapping(value = "getSettings")
    public ModelAndView getSettings(HttpSession session) {
        return userPage(session, "settings");
    }

    private ModelAndView userPage(HttpSession session, String viewName) {
        User user = getSessionUser(session);
        if (user == null) {
            return new ModelAndView(LOGIN);
        }
        logger.info(user.getUserId());
        return new ModelAndView(viewName).addObject("user", user);
    }

    private User getSessionUser(HttpSession session) {
        Object user = session.getAttribute(SESSION_USER);
        return user instanceof User ? (User) user : null;
    }
}
