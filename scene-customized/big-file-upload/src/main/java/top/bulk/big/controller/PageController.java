package top.bulk.big.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 散装java
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("/{path}")
    public String toPage(@PathVariable String path) {
        return path;
    }
}
