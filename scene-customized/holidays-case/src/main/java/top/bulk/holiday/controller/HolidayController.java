package top.bulk.holiday.controller;

import top.bulk.holiday.entity.HolidayView;
import top.bulk.holiday.service.HolidayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 节假日控制类
 *
 * @author 散装java
 * @date 2022-05-27
 */
@Controller
public class HolidayController {
    @Resource
    HolidayService service;

    @GetMapping("/")
    public ModelAndView test() {
        List<HolidayView> nextHolidays = service.getNextHolidays();
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("nextHolidays", nextHolidays);
        return new ModelAndView("index.html", map);
    }
}
