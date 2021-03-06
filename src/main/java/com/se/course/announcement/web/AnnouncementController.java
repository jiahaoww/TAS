package com.se.course.announcement.web;

//import packages
import com.se.global.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import com.se.course.announcement.service.AnnouncementService;
import com.se.course.announcement.domain.Announcement;
import com.se.notice.service.NoticeService;

/**
 * @author Yusen
 * @version 1.0
 * @since 1.0
 */
@Controller
public class AnnouncementController {
    private AnnouncementService announcementService;
    private NoticeService noticeService;

    @Autowired
    public void setAnnouncementService(AnnouncementService announcementService) { this.announcementService = announcementService; }

    @Autowired
    public void setNoticeService(NoticeService noticeService) { this.noticeService = noticeService; }

    /**
     * 显示公告发布页面
     *
     * @return 公告发布页面逻辑视图名
     */
    @RequestMapping("/course/{courseId}/resource/announcement/to-upload")
    public String uploadPage(@PathVariable int courseId) {
        return "course/announcement/announcement_upload";
    }

    /**
     * 发布公告
     *
     * @param session 当前会话
     * @param courseId 课程id
     * @param title 公告标题
     * @param content 公告内容
     * @param model Model对象
     * @return 成功则重定向到课程功能入口页面，否则返回到公告发布页面
     */
    @RequestMapping("/course/{courseId}/resource/announcement/upload")
    public String upload(HttpSession session, @PathVariable int courseId, @RequestParam("title") String title, @RequestParam("content") String content, Model model) {
        if (announcementService.upload(session, courseId, title, content)) {
            return "redirect:/course/index";
        } else {
            ModelService.setError(model, "发布失败!");
            return "/course/announcement/announcement_upload";
        }
    }

    /**
     * 显示公告列表页面
     *
     * @param session 当前会话
     * @param request 请求
     * @param courseId 课程id
     * @param model Model对象
     * @return 公告列表页面逻辑视图名
     */
    @RequestMapping("/course/{courseId}/resource/announcement/list")
    public String listPage(HttpSession session, HttpServletRequest request, @PathVariable int courseId, Model model) {
        noticeService.removeNotice(session, request);
        ArrayList<Announcement> announcements =  announcementService.getAnnouncements(courseId);
        ModelService.setAnnouncements(model, announcements);
        return "course/announcement/announcement_list";
    }
}
